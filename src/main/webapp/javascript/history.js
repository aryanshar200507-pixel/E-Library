
document.addEventListener("DOMContentLoaded", function () {

    /* =========================================
       LUCIDE ICONS
       ========================================= */

    function renderIcons() {
        if (window.lucide) {
            window.lucide.createIcons();
        }
    }

    renderIcons();

    /* =========================================
       MOBILE NAVIGATION
       ========================================= */

    var menuButton = document.getElementById("mobileMenuButton");
    var mobileNav = document.getElementById("mobileNav");

    if (!menuButton || !mobileNav) {
        return;
    }

    var menuOpen = false;

    function updateMenu(open) {
        menuOpen = open;

        mobileNav.classList.toggle("open", menuOpen);

        menuButton.setAttribute(
            "aria-expanded",
            String(menuOpen)
        );

        menuButton.setAttribute(
            "aria-label",
            menuOpen ? "Close navigation" : "Open navigation"
        );

        var icon = menuButton.querySelector("i");

        if (icon) {
            icon.setAttribute(
                "data-lucide",
                menuOpen ? "x" : "menu"
            );

            renderIcons();
        }
    }

    /* Toggle mobile navigation */

    menuButton.addEventListener("click", function () {
        updateMenu(!menuOpen);
    });

    /* Close after selecting a navigation link */

    mobileNav.querySelectorAll("a").forEach(function (link) {
        link.addEventListener("click", function () {
            updateMenu(false);
        });
    });

    /* Close when clicking outside */

    document.addEventListener("click", function (event) {
        if (
            menuOpen &&
            !mobileNav.contains(event.target) &&
            !menuButton.contains(event.target)
        ) {
            updateMenu(false);
        }
    });

    /* Close with Escape key */

    document.addEventListener("keydown", function (event) {
        if (event.key === "Escape" && menuOpen) {
            updateMenu(false);
            menuButton.focus();
        }
    });

    /* Close mobile menu when switching to desktop */

    window.addEventListener("resize", function () {
        if (window.innerWidth > 1050 && menuOpen) {
            updateMenu(false);
        }
    });

});