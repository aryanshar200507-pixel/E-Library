
/* ============================================================
   STORIES E-LIBRARY — BOOKS / CATEGORY PAGE
   Corrected JavaScript with responsive mobile navigation
   ============================================================ */

document.addEventListener("DOMContentLoaded", function () {

    "use strict";

    /* ============================================================
       1. INITIALIZE LUCIDE ICONS
       ============================================================ */

    function initializeLucide() {
        if (
            window.lucide &&
            typeof window.lucide.createIcons === "function"
        ) {
            window.lucide.createIcons();
        }
    }

    initializeLucide();

    /* ============================================================
       2. GET MOBILE MENU ELEMENTS
       ============================================================ */

    var menuButton = document.getElementById("mobileMenuButton");
    var mobileNav = document.getElementById("mobileNav");

    if (menuButton && mobileNav) {

        var isMenuOpen = false;

        // Initial menu state
        mobileNav.classList.remove("open");
        mobileNav.setAttribute("aria-hidden", "true");

        menuButton.setAttribute("aria-expanded", "false");
        menuButton.setAttribute("aria-label", "Open menu");

        /* ========================================================
           3. UPDATE MENU ICON
           ======================================================== */

        function updateMenuIcon(open) {

            var iconName = open ? "x" : "menu";

            menuButton.setAttribute(
                "aria-label",
                open ? "Close menu" : "Open menu"
            );

            menuButton.innerHTML =
                '<i data-lucide="' + iconName + '"></i>';

            initializeLucide();
        }

        /* ========================================================
           4. OPEN MOBILE MENU
           ======================================================== */

        function openMobileMenu() {

            if (isMenuOpen) return;

            isMenuOpen = true;

            mobileNav.classList.add("open");

            menuButton.setAttribute("aria-expanded", "true");
            mobileNav.setAttribute("aria-hidden", "false");

            document.body.classList.add("mobile-menu-open");

            updateMenuIcon(true);
        }

        /* ========================================================
           5. CLOSE MOBILE MENU
           ======================================================== */

        function closeMobileMenu(returnFocus) {

            if (!isMenuOpen) return;

            isMenuOpen = false;

            mobileNav.classList.remove("open");

            menuButton.setAttribute("aria-expanded", "false");
            mobileNav.setAttribute("aria-hidden", "true");

            document.body.classList.remove("mobile-menu-open");

            updateMenuIcon(false);

            if (returnFocus) {
                menuButton.focus();
            }
        }

        /* ========================================================
           6. HAMBURGER BUTTON CLICK
           ======================================================== */

        menuButton.addEventListener("click", function (event) {

            event.preventDefault();
            event.stopPropagation();

            if (isMenuOpen) {
                closeMobileMenu(false);
            } else {
                openMobileMenu();
            }
        });

        /* ========================================================
           7. CLOSE MENU WHEN CLICKING OUTSIDE
           ======================================================== */

        document.addEventListener("click", function (event) {

            if (
                isMenuOpen &&
                !mobileNav.contains(event.target) &&
                !menuButton.contains(event.target)
            ) {
                closeMobileMenu(false);
            }
        });

        /* ========================================================
           8. CLOSE MENU WHEN CLICKING A NAVIGATION LINK
           ======================================================== */

        var navLinks = mobileNav.querySelectorAll("a");

        navLinks.forEach(function (link) {

            link.addEventListener("click", function () {
                closeMobileMenu(false);
            });
        });

        /* ========================================================
           9. CLOSE MENU WITH ESCAPE KEY
           ======================================================== */

        document.addEventListener("keydown", function (event) {

            if (event.key === "Escape" && isMenuOpen) {
                closeMobileMenu(true);
            }
        });

        /* ========================================================
           10. CLOSE MENU WHEN RESIZING TO DESKTOP
           ======================================================== */

        function handleResize() {

            if (window.innerWidth > 800 && isMenuOpen) {
                closeMobileMenu(false);
            }
        }

        window.addEventListener("resize", handleResize);
    }

    /* ============================================================
       11. SEARCH INPUT ENHANCEMENT
       ============================================================ */

    var searchInputs = document.querySelectorAll(
        '.search-form input[name="keyword"]'
    );

    searchInputs.forEach(function (input) {

        input.addEventListener("keydown", function (event) {

            if (event.key === "Escape") {
                input.blur();
            }
        });
    });

    /* ============================================================
       12. BOOK CARD ANIMATIONS
       ============================================================ */

    var animatedCards = document.querySelectorAll(
        ".category-card, .book-card"
    );

    animatedCards.forEach(function (card, index) {
        card.style.setProperty("--card-index", index);
    });

    /* ============================================================
       13. FINAL ICON INITIALIZATION
       ============================================================ */

    initializeLucide();

});