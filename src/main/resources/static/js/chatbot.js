document.addEventListener("DOMContentLoaded", function () {
    const chatMessages = document.getElementById("chatMessages");
    const sendButton = document.querySelector(".send-button");
    const messageInput = document.querySelector(".chat-input");
    const chatContainer = document.querySelector(".chat-container");
    let currentStep = "start_message";
    let next = "start"; //다음 요청 주소
    let locale;

    async function fetchLocale() {
        try {
            const response = await fetch('/api/current-locale');
            const localeInfo = await response.text();
            locale = localeInfo;
        } catch (error) {
            console.error('언어 정보 불러오기 실패:', error);
        }
    }

    async function createBotChoiceMessage(text, choices) {
        const messageDiv = document.createElement("div");
        messageDiv.classList.add("chat-message", "bot");

        const bubble = document.createElement("div");
        bubble.classList.add("bubble", "combined-bubble");
        bubble.innerHTML = text;

        const btnGroup = document.createElement("div");
        btnGroup.classList.add("button-group");

        choices.forEach((choice) => {
            const button = document.createElement("button");
            button.classList.add("chat-option-button");
            button.textContent = choice.label;
            button.onclick = choice.onClick;
            btnGroup.appendChild(button);
        });

        bubble.appendChild(btnGroup);
        messageDiv.appendChild(bubble);
        chatMessages.appendChild(messageDiv);
        setTimeout(() => {
            chatContainer.scrollTop = chatContainer.scrollHeight;
        }, 0);
    }

    async function createBotMessage(message) {
        const messageDiv = document.createElement("div");
        messageDiv.classList.add("chat-message", "bot");

        const bubble = document.createElement("div");
        bubble.classList.add("bubble");
        bubble.textContent = ""; // 초기 텍스트 비움

        messageDiv.appendChild(bubble);
        chatMessages.appendChild(messageDiv);

        return new Promise((resolve) => { // Promise 반환
            let index = 0;
            const typingInterval = setInterval(() => {
                bubble.innerHTML = message.substring(0, index);
                index++;
                // 타이핑 중 스크롤 아래로 이동
                chatContainer.scrollTop = chatContainer.scrollHeight;
                if (index > message.length) {
                    clearInterval(typingInterval);
                    resolve(); // 타이핑 완료 후 Promise resolve
                }
            }, 20);
            // 초기 스크롤 (애니메이션 시작 후 한 번 실행해도 충분)
            chatContainer.scrollTop = chatContainer.scrollHeight;
        });
    }

    function createBotTempMessage(message) {
        const messageDiv = document.createElement("div");
        messageDiv.classList.add("chat-message", "bot", "temp");

        const bubble = document.createElement("div");
        bubble.classList.add("bubble");
        bubble.textContent = ""; // 초기 텍스트 비움

        messageDiv.appendChild(bubble);
        chatMessages.appendChild(messageDiv);

        let index = 0;
        let typingInterval;

        function startRepeatingTyping() {
            index = 0;
            typingInterval = setInterval(() => {
                bubble.textContent = message.substring(0, index);
                index++;
                // 타이핑 중 스크롤을 아래로 이동
                chatContainer.scrollTop = chatContainer.scrollHeight;
                if (index > message.length) {
                    index = message.length-3; // 텍스트 끝에 도달하면 index를 0으로 초기화하여 반복
                }
            }, 80);
        }

        startRepeatingTyping();

        const observer = new MutationObserver((mutationsList, observer) => {
            for (const mutation of mutationsList) {
                const removedNodesArray = Array.from(mutation.removedNodes);
                if (removedNodesArray.includes(messageDiv)) {
                    clearInterval(typingInterval);
                    observer.disconnect();
                    break;
                }
            }
        });
        observer.observe(chatMessages, { childList: true });

        // 초기 스크롤 시도 (애니메이션 시작 후 한 번 실행해도 충분)
        setTimeout(() => {
            chatContainer.scrollTop = chatContainer.scrollHeight;
        }, 0);
    }


    function addUserMessage(text) {
        const messageDiv = document.createElement("div");
        messageDiv.classList.add("chat-message", "user");

        const bubble = document.createElement("div");
        bubble.classList.add("bubble");
        bubble.textContent = text;

        messageDiv.appendChild(bubble);
        chatMessages.appendChild(messageDiv);
        chatContainer.scrollTop = chatContainer.scrollHeight;
    }

    async function addChatUI(input, requestNext) {
        if(input){
            addUserMessage(input)
        }
        if(locale === "ko"){
            createBotTempMessage("답변을 생성 중 이에요...")
        }
        else if(locale === "ja"){
            createBotTempMessage("回答を生成しています...")
        }
        else if(locale === "zh"){
            createBotTempMessage("正在生成回复...")
        }
        else createBotTempMessage("Generating response...")

        const data = await sendRequest(input, requestNext);
        document.querySelector(".temp").remove();

        if(data.message){
            await createBotMessage(data.message); // 메시지 타이핑 완료까지 대기
        }

        if(data.detail_message){

        }
        next = data.next;

        if(data.addMessage){
            createBotChoiceMessage(data.addMessage,[
                {
                    label: "YES",
                    onClick: () => {
                        if(next === "/start"){
                            next = "start";
                            nextStep();
                        }
                        else{
                            addChatUI("YES", next);
                        }
                    }
                },
                {
                    label: "NO",
                    onClick: () => {
                        if(next === "/start"){
                            next = "start";
                            if(locale === "ko"){
                                createBotMessage("이용해주셔서 감사합니다!")
                            }
                            else if(locale === "ja"){
                                createBotMessage("ご利用いただきありがとうございます！")
                            }
                            else if(locale === "zh"){
                                createBotMessage("感谢您的使用！")
                            }
                            else createBotMessage("Thank you for using our service!")

                        }
                        else{
                            addChatUI("NO", next);
                        }
                    }
                }
            ])
        }

        if(data.error){
            createBotMessage(data.error);
        }

        if(data.medicine_candidates){
            const candidates =
                data.medicine_candidates.map(candidate => ({
                    label: candidate.itemName,
                    onClick: () => {
                        addChatUI(candidate.name_ko, "/select"); // 혹은 현재 next
                    }
                }));
            if(locale === "ko"){
                createBotChoiceMessage("버튼을 클릭해주세요!", candidates);
            }
            else if(locale === "ja"){
                createBotChoiceMessage("ボタンをクリックしてください！" , candidates)
            }
            else if(locale === "zh"){
                createBotChoiceMessage("请点击按钮！" , candidates)
            }
            else createBotChoiceMessage("Please click the button!" , candidates)

        }

        if(data.response_type === "name_all_fail" || data.response_type === "symptom_all_fail"){
            createBotChoiceMessage("", [
                {
                    label: "OK",
                    onClick: () => {
                        next = "start";
                        nextStep();
                    }
                }
            ]);
        }
    }

    async function nextStep(text) {
        let message1
        let message2
        let message3
        let message4
        let message5
        let op1
        let op2

        if(locale === "ko"){
            message1 = "안녕하세요, K-MediGuide입니다!"
            message2 = "아래에서 필요한 정보가 있으시다면 클릭해주세요!"
            message3 = "이 약에 대해 알고 싶어요!"
            message4 = "증상에 맞는 약을 알고 싶어요!"
            message5 = "또는 직접 원하는 질문을 할 수 있어요!"
            op1 = "약"
            op2 = "증상"
        }
        else if(locale === "ja"){
            message1 = "こんにちは、K-MediGuideです！"
            message2 = "下の中に必要な情報があればクリックしてください！"
            message3 = "この薬について知りたいです！"
            message4 = "症状に合った薬を知りたいです！"
            message5 = "または、直接質問することもできます！"
            op1 = "薬"
            op2 = "症状"
        }
        else if(locale === "zh"){
            message1 = "您好，这里是 K-MediGuide！"
            message2 = "如果您需要以下信息，请点击！"
            message3 = "我想了解这款药！"
            message4 = "我想知道适合我症状的药！"
            message5 = "或者您也可以直接提问！"
            op1 = "药"
            op2 = "症状"
        }
        else {
            message1 = "Hello, this is K-MediGuide!"
            message2 = "If you see any information you need below, please click!"
            message3 = "I want to know more about this medicine!"
            message4 = "I want to find medicine that matches my symptoms!"
            message5 = "Or you can also ask your own question directly!"
            op1 = "Medicine"
            op2 = "Symptom"
        }

        if(next === "start"){
            await createBotMessage(message1); // 메시지 타이핑 완료까지 대기
            await new Promise(resolve => setTimeout(resolve, 100)); // 약간의 딜레이 추가
            createBotChoiceMessage(message2, [
                {
                    label: message3,
                    onClick: () => {
                        addChatUI(op1, next);
                    }
                },
                {
                    label: message4,
                    onClick: () => {
                        addChatUI(op2, next);
                    }
                },
            ]);
            await createBotMessage(message5);
        } else {
            next = "/start";
        }
    }

    sendButton.addEventListener("click", () => {
        const text = messageInput.value.trim();
        if (text) {
            messageInput.value = "";
            addChatUI(text, next);
        }
    });

    messageInput.addEventListener("keypress", (e) => {
        if (e.key === "Enter") {
            sendButton.click();
        }
    });

    function sendRequest(userInput, next) {
        const cookies = document.cookie; // 현재 도메인의 쿠키 문자열 읽기
        const headers = {
            'Content-Type': 'application/json'
        };

        if (cookies) {
            headers['Cookie'] = cookies; // Cookie 헤더에 쿠키 값 설정
        }

        // Ajax 요청
        return fetch('/api/chatbot', {
            method: 'POST',
            headers: headers,
            body: JSON.stringify({
                input: userInput,
                next: next,
                lang: locale
            })
        })
        .then(response => response.json())
        .then(data => {
            // 응답에 메시지 속성이 있고, 줄바꿈 문자 처리
            if (!data) {
                throw new Error('응답 데이터에 "message" 속성이 없습니다.');
            }
            if(data.message){
                // 메시지에서 줄바꿈 문자 (\n)을 <br>로 변환
                data.message = data.message.replace(/\n/g, '<br>');
            }

            return data; // 변환된 메시지를 반환
        })
        .catch(error => console.error('Error:', error));
    }

    function newSession() {
        // Ajax 요청
        return fetch('/api/chatbot/new', {
            method: 'POST',
        })
        .then(response => {
            // 새로운 세션이 생성되면 서버가 Set-Cookie 헤더를 통해 쿠키를 전달할 것입니다.
            // 브라우저는 이 쿠키를 자동으로 저장합니다.
            return response.text(); // 또는 response.json() 등 응답 형식에 따라 처리
        })
        .catch(error => console.error('Error:', error));
    }

    async function startProcess() {
        await fetchLocale();  // fetchLocale 함수가 끝날 때까지 기다림
        newSession();          // fetchLocale이 완료된 후 실행
        nextStep();           // fetchLocale이 완료된 후 실행
    }

    startProcess();  // startProcess 함수 실행
})