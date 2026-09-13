// =====================================================
// E-LIBRARY — HOME PAGE SCRIPT
// =====================================================

document.addEventListener("DOMContentLoaded", function () {

    // ---------- Render Lucide Icons ----------
    if (typeof lucide !== "undefined") {
        lucide.createIcons();
    }

    // ---------- Mobile Nav Toggle ----------
    var navToggle = document.getElementById("navToggle");
    var navLinks = document.getElementById("navLinks");

    if (navToggle && navLinks) {

        navToggle.addEventListener("click", function () {

            var isOpen = navLinks.classList.toggle("open");

            navToggle.innerHTML = isOpen
                ? '<i data-lucide="x"></i>'
                : '<i data-lucide="menu"></i>';

            if (typeof lucide !== "undefined") {
                lucide.createIcons();
            }
        });

        // Close mobile menu after clicking a link
        navLinks.querySelectorAll("a").forEach(function (link) {

            link.addEventListener("click", function () {

                if (window.innerWidth <= 800) {

                    navLinks.classList.remove("open");

                    navToggle.innerHTML =
                        '<i data-lucide="menu"></i>';

                    if (typeof lucide !== "undefined") {
                        lucide.createIcons();
                    }
                }
            });
        });
    }

    // ---------- Scroll Reveal for Book Cards ----------
    var cards = document.querySelectorAll(".book-card");

    if (cards.length && "IntersectionObserver" in window) {

        cards.forEach(function (card) {

            card.style.opacity = "0";
            card.style.transform = "translateY(24px)";
            card.style.transition =
                "opacity 0.5s ease, transform 0.5s ease";
        });

        var observer = new IntersectionObserver(
            function (entries) {

                entries.forEach(function (entry) {

                    if (entry.isIntersecting) {

                        entry.target.style.opacity = "1";
                        entry.target.style.transform = "translateY(0)";

                        observer.unobserve(entry.target);
                    }
                });

            },
            {
                threshold: 0.12
            }
        );

        cards.forEach(function (card) {
            observer.observe(card);
        });
    }

    // ---------- Navbar Shadow on Scroll ----------
    var navbar = document.getElementById("navbar");

    if (navbar) {

        window.addEventListener("scroll", function () {

            if (window.scrollY > 20) {

                navbar.style.boxShadow =
                    "0 4px 24px rgba(0,0,0,0.25)";

            } else {

                navbar.style.boxShadow =
                    "0 2px 20px rgba(60,40,20,0.12)";
            }
        });
    }

});