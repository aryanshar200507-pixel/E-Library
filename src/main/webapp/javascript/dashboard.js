document.addEventListener("DOMContentLoaded", function () {

	/* ============================================
	   RENDER LUCIDE ICONS
	============================================ */
	if (window.lucide) {
		lucide.createIcons();
	}

	/* ============================================
	   MOBILE NAV TOGGLE
	============================================ */
	var mobileMenuButton = document.getElementById("mobileMenuButton");
	var mobileNav = document.getElementById("mobileNav");

	if (mobileMenuButton && mobileNav) {

		mobileMenuButton.addEventListener("click", function () {
			var isOpen = mobileNav.classList.toggle("open");
			mobileMenuButton.setAttribute("aria-expanded", isOpen ? "true" : "false");

			mobileMenuButton.innerHTML = isOpen
				? '<i data-lucide="x"></i>'
				: '<i data-lucide="menu"></i>';

			if (window.lucide) {
				lucide.createIcons();
			}
		});

		/* Close the menu when a nav link is tapped (mobile) */
		mobileNav.querySelectorAll("a").forEach(function (link) {
			link.addEventListener("click", function () {
				mobileNav.classList.remove("open");
				mobileMenuButton.setAttribute("aria-expanded", "false");
				mobileMenuButton.innerHTML = '<i data-lucide="menu"></i>';
				if (window.lucide) {
					lucide.createIcons();
				}
			});
		});

		/* Close the menu on resize back to desktop */
		window.addEventListener("resize", function () {
			if (window.innerWidth > 800) {
				mobileNav.classList.remove("open");
				mobileMenuButton.setAttribute("aria-expanded", "false");
				mobileMenuButton.innerHTML = '<i data-lucide="menu"></i>';
			}
		});
	}

	/* ============================================
	   RECOMMENDED BOOKS SLIDER
	============================================ */
	var slider = document.getElementById("recommendedSlider");

	if (!slider) {
		return;
	}

	var track = slider.querySelector(".slider-track");
	var slides = Array.prototype.slice.call(slider.querySelectorAll(".slide"));
	var dotsContainer = document.getElementById("sliderDots");
	var prevBtn = document.getElementById("sliderPrev");
	var nextBtn = document.getElementById("sliderNext");

	var currentIndex = 0;
	var slideCount = slides.length;
	var autoSlideDelay = 4000; // 4 seconds
	var autoSlideTimer = null;

	if (slideCount === 0) {
		return;
	}

	/* Hide arrows/dots when there's only one slide */
	if (slideCount === 1) {
		if (prevBtn) prevBtn.style.display = "none";
		if (nextBtn) nextBtn.style.display = "none";
	}

	/* Build dots */
	slides.forEach(function (_, index) {
		var dot = document.createElement("span");
		dot.className = "dot" + (index === 0 ? " active" : "");
		dot.setAttribute("data-index", index);
		dot.addEventListener("click", function () {
			goToSlide(index);
			resetAutoSlide();
		});
		dotsContainer.appendChild(dot);
	});

	var dots = Array.prototype.slice.call(dotsContainer.querySelectorAll(".dot"));

	function updateDots() {
		dots.forEach(function (dot, index) {
			dot.classList.toggle("active", index === currentIndex);
		});
	}

	function goToSlide(index) {
		currentIndex = (index + slideCount) % slideCount;
		track.style.transform = "translateX(-" + (currentIndex * 100) + "%)";
		updateDots();
	}

	function nextSlide() {
		goToSlide(currentIndex + 1);
	}

	function prevSlide() {
		goToSlide(currentIndex - 1);
	}

	function startAutoSlide() {
		autoSlideTimer = setInterval(nextSlide, autoSlideDelay);
	}

	function stopAutoSlide() {
		if (autoSlideTimer) {
			clearInterval(autoSlideTimer);
			autoSlideTimer = null;
		}
	}

	function resetAutoSlide() {
		stopAutoSlide();
		startAutoSlide();
	}

	if (nextBtn) {
		nextBtn.addEventListener("click", function () {
			nextSlide();
			resetAutoSlide();
		});
	}

	if (prevBtn) {
		prevBtn.addEventListener("click", function () {
			prevSlide();
			resetAutoSlide();
		});
	}

	/* Pause auto-slide while hovering the slider */
	slider.addEventListener("mouseenter", stopAutoSlide);
	slider.addEventListener("mouseleave", startAutoSlide);

	/* Init */
	goToSlide(0);
	startAutoSlide();
});