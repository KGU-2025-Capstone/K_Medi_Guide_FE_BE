document.addEventListener("DOMContentLoaded", async function () {
    let currentLocale; // 페이지 전역에서 사용할 로케일 변수
    window.showCardFromElement = function(cardId) {
        showCard(cardId);
    };


    async function fetchLocale() {
        try {
            const response = await fetch('/api/current-locale');
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return await response.text();
        } catch (error) {
            console.error('언어 정보 불러오기 실패:', error);
            return 'en'; // 기본값 설정 (선택 사항)
        }
    }



    function showCard(id) {
        fetch(`api/translateCard/${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(card => {
            let lang = card.e_symptoms; // 기본값
            if (currentLocale === "ja") {
                lang = card.c_symptoms;
            } else if (currentLocale === "zh") {
                lang = card.j_symptoms;
            }

            document.getElementById('modalImage').src = card.imageUrl;
            document.getElementById('modalForeignText').innerText = lang;
            document.getElementById('modalKoreanText').innerText = card.k_symptoms;
            document.getElementById('cardModal').style.display = 'block';
        })
        .catch(error => {
            console.error('카드 정보 불러오기 실패:', error);
            // 오류 처리 로직 추가
        });
    }


    function closeModal() {
        document.getElementById('cardModal').style.display = 'none';
    }

    window.onclick = function (event) {
        if (event.target == document.getElementById('cardModal')) {

            closeModal();
        }
    }
    currentLocale = await fetchLocale();
});