function showCard(id) {
    fetch('/card/' + id)
        .then(response => response.json())
        .then(card => {
            document.getElementById('modalImage').src = card.imageUrl;
            document.getElementById('modalForeignText').textContent = card.foreignText;
            document.getElementById('modalKoreanText').textContent = card.koreanText;
            document.getElementById('cardModal').style.display = 'block';
        });
}

function closeModal() {
    document.getElementById('cardModal').style.display = 'none';
}

document.addEventListener('mousedown', function(event) {
    const modal = document.getElementById('cardModal');
    const modalContent = document.querySelector('.modal-content');

    if (modal && modal.style.display === 'block') {
        // 클릭된 요소가 모달 컨텐츠 영역 밖이면 모달 닫기
        if (!modalContent.contains(event.target) && event.target !== modalContent) {
            closeModal();
        }
    }
});