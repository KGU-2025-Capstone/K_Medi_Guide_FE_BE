document.addEventListener("DOMContentLoaded", function () {
    const sendButton = document.querySelector(".send-button");
    const messageInput = document.querySelector(".chat-input");
    const chatMessages = document.getElementById("chatMessages");

    const botResponses = [
        "도와드릴게요!",
        "자세히 설명해 주시겠어요?",
        "좋은 질문이에요!",
        "곧 답변 드릴게요.",
        "그건 이런 방식으로 해결할 수 있어요."
    ];

    function getCurrentTime() {
        const now = new Date();
        return now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    }

    function addMessage(message, sender = "user") {
        const messageDiv = document.createElement("div");
        messageDiv.classList.add("chat-message", sender);

        const bubble = document.createElement("div");
        bubble.classList.add("bubble");
        bubble.textContent = message;

        const timestamp = document.createElement("div");
        timestamp.classList.add("timestamp");
        timestamp.textContent = getCurrentTime();

        messageDiv.appendChild(bubble);
        messageDiv.appendChild(timestamp);
        chatMessages.appendChild(messageDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    function sendMessage() {
        const message = messageInput.value.trim();
        if (!message) return;

        addMessage(message, "user");
        messageInput.value = "";
        messageInput.focus();

        // 챗봇 "타이핑 중..."
        const typingDiv = document.createElement("div");
        typingDiv.classList.add("chat-message", "bot");
        const bubble = document.createElement("div");
        bubble.classList.add("bubble");
        bubble.textContent = "입력 중...";
        typingDiv.appendChild(bubble);
        chatMessages.appendChild(typingDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;

        setTimeout(() => {
            chatMessages.removeChild(typingDiv);
            const randomReply = botResponses[Math.floor(Math.random() * botResponses.length)];
            addMessage(randomReply, "bot");
        }, 1000); // 1초 후 응답
    }

    sendButton.addEventListener("click", sendMessage);
    messageInput.addEventListener("keypress", function (e) {
        if (e.key === "Enter") sendMessage();
    });

    // 자동 포커스
    messageInput.focus();
});
