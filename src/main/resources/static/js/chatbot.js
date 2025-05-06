document.addEventListener("DOMContentLoaded", function () {
    const chatMessages = document.getElementById("chatMessages");
    const sendButton = document.querySelector(".send-button");
    const messageInput = document.querySelector(".chat-input");
  
    let currentStep = "intro";
    let selectedDrug = "";
  
    function createBotChoiceMessage(text, choices) {
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
      chatMessages.scrollTop = chatMessages.scrollHeight;
    }
  
    function createBotMessage(message) {
      const messageDiv = document.createElement("div");
      messageDiv.classList.add("chat-message", "bot");
  
      const bubble = document.createElement("div");
      bubble.classList.add("bubble");
      bubble.textContent = message;
  
      messageDiv.appendChild(bubble);
      chatMessages.appendChild(messageDiv);
      chatMessages.scrollTop = chatMessages.scrollHeight;
    }
  
    function addUserMessage(text) {
      const messageDiv = document.createElement("div");
      messageDiv.classList.add("chat-message", "user");
  
      const bubble = document.createElement("div");
      bubble.classList.add("bubble");
      bubble.textContent = text;
  
      messageDiv.appendChild(bubble);
      chatMessages.appendChild(messageDiv);
      chatMessages.scrollTop = chatMessages.scrollHeight;
    }
  
    function nextStep(userInput = "") {
      switch (currentStep) {
        case "intro":
          createBotMessage("안녕하세요, K-MediGuide입니다!");
          setTimeout(() => {
            createBotChoiceMessage("아래에서 필요한 정보가 있으시다면 클릭해주세요!", [
              {
                label: "궁금한 약이 있으신가요?",
                onClick: () => {
                  currentStep = "ask-drug";
                  createBotMessage("어떤 약이 궁금하신가요? 약 이름을 입력해주세요.");
                },
              },
              {
                label: "증상에 맞는 약이 알고싶으신가요?",
                onClick: () => {
                  currentStep = "ask-symptom";
                  createBotMessage("어디가 아프신가요? 증상을 자세히 말씀해주세요!");
                },
              },
            ]);
          }, 600);
          break;
  
        case "ask-drug":
          addUserMessage(userInput);
          currentStep = "show-drugs";
          createBotChoiceMessage("다음 중 어떤 약이 궁금하신가요?", [
            { label: "타이레놀 : 진통해열제", onClick: () => nextStep("타이레놀") },
            { label: "판콜에스 : 감기약", onClick: () => nextStep("판콜에스") },
            { label: "겔포스 : 위장약", onClick: () => nextStep("겔포스") },
          ]);
          break;
  
        case "ask-symptom":
          addUserMessage(userInput);
          currentStep = "show-drugs";
          createBotChoiceMessage("말씀하신 증상에 효능이 있는 약들이에요. 어떤 약이 궁금하신가요?", [
            { label: "타이레놀 : 진통해열제", onClick: () => nextStep("타이레놀") },
            { label: "판콜에스 : 감기약", onClick: () => nextStep("판콜에스") },
            { label: "겔포스 : 위장약", onClick: () => nextStep("겔포스") },
          ]);
          break;
  
        case "show-drugs":
          selectedDrug = userInput;
          currentStep = "ask-usage";
          createBotChoiceMessage(`${selectedDrug}(은)는 이런 증상에 효과가 있어요! 복용법과 주의사항도 알려드릴까요?`, [
            { label: "네", onClick: () => nextStep("yes") },
            { label: "아니요", onClick: () => nextStep("no") },
          ]);
          break;
  
        case "ask-usage":
          if (userInput === "yes") {
            currentStep = "end";
            createBotMessage(`${selectedDrug}(은)는 이렇게 복용하면 돼요. 물과 함께 충분히 드시고, 졸음이 올 수 있으니 주의하세요.`);
            setTimeout(() => {
              createBotMessage("더 궁금한 게 있으신가요?");
            }, 600);
          } else {
            createBotMessage("네, 궁금하신 것이 생기면 다시 말씀해주세요!");
            currentStep = "end";
          }
          break;
  
        default:
          createBotMessage("챗봇을 다시 시작하려면 새로고침해주세요.");
          break;
      }
    }
  
    // 초기화
    nextStep();
  
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
  });
  