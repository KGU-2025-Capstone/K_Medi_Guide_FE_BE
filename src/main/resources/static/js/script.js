document.addEventListener("DOMContentLoaded", function() {
    const slides = document.querySelectorAll(".slide");
    const dots = document.querySelectorAll(".dot");
    const prevButton = document.querySelector(".prev-button");
    const nextButton = document.querySelector(".next-button");

    let currentIndex = 0;
    let autoSlideInterval = setInterval(nextSlide, 5000);

    function showSlide(index) {
        console.log(index);
        slides.forEach(function(slide, i) {
            slide.classList.remove("active");
            dots[i].classList.remove("active");
        });

        slides[index].classList.add("active");
        dots[index].classList.add("active");
    }

    function nextSlide() {
        currentIndex = (currentIndex + 1) % slides.length;
        showSlide(currentIndex);
    }

    function prevSlide() {
        currentIndex = (currentIndex - 1 + slides.length) % slides.length;
        showSlide(currentIndex);
    }

    nextButton.addEventListener("click", function() {
        nextSlide();
        resetAutoSlide();
    });

    prevButton.addEventListener("click", function() {
        prevSlide()
        resetAutoSlide();
    });

    function resetAutoSlide() {
        clearInterval(autoSlideInterval);
        autoSlideInterval = setInterval(nextSlide, 5000);
    }

    dots.forEach(function(dot, i) {
        dot.addEventListener("click", function() {
            currentIndex = i;
            showSlide(currentIndex);
            resetAutoSlide();
        });
    });
});
  