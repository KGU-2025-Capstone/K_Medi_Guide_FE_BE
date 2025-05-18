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

window.onclick = function(event) {
    if (event.target == document.getElementById('cardModal')) {
        closeModal();
    }
}