// =====================================================
// STORIES E-LIBRARY — HOME PAGE SCRIPT
// =====================================================

document.addEventListener("DOMContentLoaded", function () {

    // ---------- Lucide Icons ----------
    function renderIcons() {
        if (window.lucide && typeof window.lucide.createIcons === "function") {
            window.lucide.createIcons();
        }
    }

    renderIcons();

    // Safety net: if the CDN script was slow, retry once everything has loaded
    window.addEventListener("load", function () {
        if (document.querySelector("i[data-lucide]")) {
            renderIcons();
        }
    });


    // ---------- Navbar Scroll Effect (throttled with rAF) ----------
    const navbar = document.getElementById("navbar");
    const navToggle = document.getElementById("navToggle");
    const navLinks = document.getElementById("navLinks");

    function updateNavbar() {
        if (!navbar) return;
        navbar.classList.toggle("scrolled", window.scrollY > 40);
    }

    updateNavbar();

    let scrollTicking = false;

    window.addEventListener("scroll", function () {
        if (scrollTicking) return;

        scrollTicking = true;
        window.requestAnimationFrame(function () {
            updateNavbar();
            scrollTicking = false;
        });
    }, { passive: true });


    // ---------- Mobile Navigation ----------
    function setMenu(open) {
        if (!navLinks || !navbar || !navToggle) return;

        navLinks.classList.toggle("open", open);
        navbar.classList.toggle("menu-open", open);

        navToggle.setAttribute("aria-expanded", String(open));

        navToggle.innerHTML = open
            ? '<i data-lucide="x"></i>'
            : '<i data-lucide="menu"></i>';

        renderIcons();
    }

    if (navToggle && navLinks && navbar) {

        navToggle.addEventListener("click", function () {
            setMenu(!navLinks.classList.contains("open"));
        });

        navLinks.querySelectorAll("a").forEach(function (link) {
            link.addEventListener("click", function () {
                if (window.innerWidth <= 800) {
                    setMenu(false);
                }
            });
        });

        document.addEventListener("click", function (event) {
            if (
                window.innerWidth <= 800 &&
                navLinks.classList.contains("open") &&
                !navbar.contains(event.target)
            ) {
                setMenu(false);
            }
        });

        window.addEventListener("resize", function () {
            if (window.innerWidth > 800 && navLinks.classList.contains("open")) {
                setMenu(false);
            }
        });
    }


    // ---------- Category Carousel ----------
    const categoryTrack = document.getElementById("categoryTrack");
    const categoryPrev = document.querySelector(".category-prev");
    const categoryNext = document.querySelector(".category-next");
    const categoryWrap = document.querySelector(".category-carousel-wrap");

    function scrollCategories(direction) {
        if (!categoryTrack) return;

        const firstCard = categoryTrack.querySelector(".category-card");
        if (!firstCard) return;

        const gap = parseFloat(getComputedStyle(categoryTrack).columnGap) || 14;
        const distance = firstCard.getBoundingClientRect().width + gap;

        categoryTrack.scrollBy({
            left: direction * distance * 2,
            behavior: "smooth"
        });
    }

    function updateCategoryArrows() {
        if (!categoryTrack || !categoryWrap) return;

        const maxScroll = categoryTrack.scrollWidth - categoryTrack.clientWidth;

        // hide arrows completely when all cards already fit
        categoryWrap.classList.toggle("no-scroll", maxScroll <= 2);

        if (categoryPrev) {
            categoryPrev.disabled = categoryTrack.scrollLeft <= 2;
        }

        if (categoryNext) {
            categoryNext.disabled = categoryTrack.scrollLeft >= maxScroll - 2;
        }
    }

    if (categoryPrev) {
        categoryPrev.addEventListener("click", function () {
            scrollCategories(-1);
        });
    }

    if (categoryNext) {
        categoryNext.addEventListener("click", function () {
            scrollCategories(1);
        });
    }

    if (categoryTrack) {
        let arrowTicking = false;

        categoryTrack.addEventListener("scroll", function () {
            if (arrowTicking) return;

            arrowTicking = true;
            window.requestAnimationFrame(function () {
                updateCategoryArrows();
                arrowTicking = false;
            });
        }, { passive: true });

        window.addEventListener("resize", updateCategoryArrows);
        updateCategoryArrows();
    }


    // ---------- Scroll Reveal Animation ----------
    const revealElements = document.querySelectorAll(".reveal");

    if ("IntersectionObserver" in window) {

        const observer = new IntersectionObserver(function (entries) {

            // stagger only the elements that appear together in the same batch
            let order = 0;

            entries.forEach(function (entry) {
                if (!entry.isIntersecting) return;

                entry.target.style.setProperty(
                    "--reveal-delay",
                    Math.min(order, 5) * 80 + "ms"
                );

                entry.target.classList.add("visible");
                observer.unobserve(entry.target);
                order++;
            });

        }, {
            threshold: 0.12,
            rootMargin: "0px 0px -30px 0px"
        });

        revealElements.forEach(function (element) {
            observer.observe(element);
        });

    } else {
        revealElements.forEach(function (element) {
            element.classList.add("visible");
        });
    }


    // ---------- Newsletter UI Demo ----------
    const newsletterForm = document.getElementById("newsletterForm");
    const newsletterMessage = document.getElementById("newsletterMessage");

    if (newsletterForm && newsletterMessage) {

        newsletterForm.addEventListener("submit", function (event) {
            event.preventDefault();

            newsletterMessage.textContent =
                "Thanks for your interest! Newsletter signup is not connected yet.";

            newsletterForm.reset();
        });
    }

});