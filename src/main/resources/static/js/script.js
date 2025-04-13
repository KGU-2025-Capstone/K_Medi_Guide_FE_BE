document.addEventListener("DOMContentLoaded", function() {
    const slides = document.querySelectorAll(".slide");
    const dots = document.querySelectorAll(".dot");
    const prevButton = document.querySelector(".prev-button");
    const nextButton = document.querySelector(".next-button");
  
    let currentIndex = 0;
  
    function showSlide(index) {
      slides.forEach(function(slide, i) {
        slide.classList.remove("active");
        dots[i].classList.remove("active");
      });
  
      slides[index].classList.add("active");
      dots[index].classList.add("active");
    }
  
    nextButton.addEventListener("click", function() {
      currentIndex = (currentIndex + 1) % slides.length;
      showSlide(currentIndex);
    });
  
    prevButton.addEventListener("click", function() {
      currentIndex = (currentIndex - 1 + slides.length) % slides.length;
      showSlide(currentIndex);
    });
  
    dots.forEach(function(dot, i) {
      dot.addEventListener("click", function() {
        currentIndex = i;
        showSlide(currentIndex);
      });
    });
  });
  