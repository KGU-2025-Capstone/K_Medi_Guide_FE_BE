function showCard(id) {
    fetch('api/translateCard/' + id)
        .then(response => response.json())
        .then(card => {
            document.getElementById('modalImage').src = card.imageUrl;
            document.getElementById('modalForeignText').innerText = card.e_symptoms;
            document.getElementById('modalKoreanText').innerText = card.k_symptoms;
            document.getElementById('cardModal').style.display = 'block';
        });
}

function showCardFromElement(cardId) {
    showCard(cardId);
}

function closeModal() {
    document.getElementById('cardModal').style.display = 'none';
}

window.onclick = function(event) {
    if (event.target == document.getElementById('cardModal')) {
        closeModal();
    }
}