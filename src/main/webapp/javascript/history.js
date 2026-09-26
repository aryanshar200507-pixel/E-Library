document.addEventListener("DOMContentLoaded", function () {

    /* =====================================================
       LUCIDE ICONS
       ===================================================== */

    function refreshIcons() {
        if (typeof lucide !== "undefined") {
            lucide.createIcons();
        }
    }

    refreshIcons();


    /* =====================================================
       MOBILE NAVIGATION
       ===================================================== */

    const menuButton = document.getElementById("mobileMenuButton");
    const mobileNav = document.getElementById("mobileNav");

    if (!menuButton || !mobileNav) {
        return;
    }


    function openMobileMenu() {

        mobileNav.classList.add("open");

        menuButton.setAttribute(
            "aria-expanded",
            "true"
        );

        menuButton.setAttribute(
            "aria-label",
            "Close navigation"
        );

        menuButton.innerHTML =
            '<i data-lucide="x"></i>';

        refreshIcons();
    }


    function closeMobileMenu() {

        mobileNav.classList.remove("open");

        menuButton.setAttribute(
            "aria-expanded",
            "false"
        );

        menuButton.setAttribute(
            "aria-label",
            "Open navigation"
        );

        menuButton.innerHTML =
            '<i data-lucide="menu"></i>';

        refreshIcons();
    }


    function toggleMobileMenu() {

        const isOpen =
            mobileNav.classList.contains("open");

        if (isOpen) {
            closeMobileMenu();
        } else {
            openMobileMenu();
        }
    }


    /* =====================================================
       MENU BUTTON
       ===================================================== */

    menuButton.addEventListener("click", function (event) {

        event.preventDefault();
        event.stopPropagation();

        toggleMobileMenu();

    });


    /* =====================================================
       CLOSE WHEN MOBILE LINK IS CLICKED
       ===================================================== */

    const mobileLinks =
        mobileNav.querySelectorAll("a");

    mobileLinks.forEach(function (link) {

        link.addEventListener("click", function () {

            closeMobileMenu();

        });

    });


    /* =====================================================
       CLOSE WHEN CLICKING OUTSIDE
       ===================================================== */

    document.addEventListener("click", function (event) {

        if (!mobileNav.classList.contains("open")) {
            return;
        }

        const clickedInsideNav =
            mobileNav.contains(event.target);

        const clickedMenuButton =
            menuButton.contains(event.target);

        if (!clickedInsideNav && !clickedMenuButton) {
            closeMobileMenu();
        }

    });


    /* =====================================================
       ESCAPE KEY
       ===================================================== */

    document.addEventListener("keydown", function (event) {

        if (event.key === "Escape") {

            if (mobileNav.classList.contains("open")) {
                closeMobileMenu();
            }

        }

    });


    /* =====================================================
       RESET MOBILE MENU ON DESKTOP RESIZE
       ===================================================== */

    let resizeTimer;

    window.addEventListener("resize", function () {

        clearTimeout(resizeTimer);

        resizeTimer = setTimeout(function () {

            if (window.innerWidth > 800) {
                closeMobileMenu();
            }

        }, 100);

    });


    /* =====================================================
       INITIAL ARIA STATE
       ===================================================== */

    menuButton.setAttribute(
        "aria-expanded",
        "false"
    );

    menuButton.setAttribute(
        "aria-label",
        "Open navigation"
    );

});