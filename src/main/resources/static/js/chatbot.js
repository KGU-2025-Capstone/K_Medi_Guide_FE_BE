document.addEventListener("DOMContentLoaded", function () {
    const chatMessages = document.getElementById("chatMessages");
    const sendButton = document.querySelector(".send-button");
    const messageInput = document.querySelector(".chat-input");
    const chatContainer = document.querySelector(".chat-container");
    let currentStep = "start_message";
    let next = "start"; //다음 요청 주소

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
                bubble.textContent = message.substring(0, index);
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
                    index = 0; // 텍스트 끝에 도달하면 index를 0으로 초기화하여 반복
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
        createBotTempMessage("답변을 생성 중 이에요...")
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
                            createBotMessage("이용해주셔서 감사합니다!")
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
            createBotChoiceMessage("버튼을 클릭해주세요!", candidates);
        }
    }

    async function nextStep(text) {
        if(next === "start"){
            await createBotMessage("안녕하세요, K-MediGuide입니다!"); // 메시지 타이핑 완료까지 대기
            await new Promise(resolve => setTimeout(resolve, 100)); // 약간의 딜레이 추가
            createBotChoiceMessage("아래에서 필요한 정보가 있으시다면 클릭해주세요!", [
                {
                    label: "이 약에 대해 알고 싶어요!",
                    onClick: () => {
                        addChatUI("약", next);
                    }
                },
                {
                    label: "증상에 맞는 약을 알고 싶어요!",
                    onClick: () => {
                        addChatUI("증상", next);
                    }
                },
            ]);
            await createBotMessage("또는 직접 원하는 질문을 할 수 있어요!");
        } else {
            addChatUI(text, next);
        }
    }

    sendButton.addEventListener("click", () => {
        const text = messageInput.value.trim();
        if (text) {
            messageInput.value = "";
            nextStep(text);
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
                next: next
            })
        })
        .then(response => response.json())
        .then(data => {
            return data;
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

    newSession();
    // 초기화
    nextStep();
})