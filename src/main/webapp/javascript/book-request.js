document.addEventListener("DOMContentLoaded", function () {

    /* =====================================================
       LUCIDE ICONS
       ===================================================== */

    if (typeof lucide !== "undefined") {
        lucide.createIcons();
    }


    /* =====================================================
       MOBILE MENU
       ===================================================== */

    const menuButton = document.getElementById("mobileMenuButton");
    const mobileNav = document.getElementById("mobileNav");

    if (menuButton && mobileNav) {

        menuButton.addEventListener("click", function (event) {

            event.preventDefault();

            const isOpen = mobileNav.classList.toggle("open");

            menuButton.setAttribute(
                "aria-expanded",
                isOpen ? "true" : "false"
            );

            menuButton.setAttribute(
                "aria-label",
                isOpen ? "Close navigation" : "Open navigation"
            );

        });


        /* Close menu when a mobile link is clicked */

        const mobileLinks = mobileNav.querySelectorAll("a");

        mobileLinks.forEach(function (link) {

            link.addEventListener("click", function () {

                mobileNav.classList.remove("open");

                menuButton.setAttribute(
                    "aria-expanded",
                    "false"
                );

                menuButton.setAttribute(
                    "aria-label",
                    "Open navigation"
                );

            });

        });

    }


    /* =====================================================
       ALERT CLOSE BUTTONS
       ===================================================== */

    const alertCloseButtons =
        document.querySelectorAll(".alert-close");

    alertCloseButtons.forEach(function (button) {

        button.addEventListener("click", function () {

            const alert = button.closest(".alert");

            if (alert) {
                alert.remove();
            }

        });

    });


    /* =====================================================
       AUTOMATIC ALERT DISMISS
       ===================================================== */

    setTimeout(function () {

        const alerts =
            document.querySelectorAll(".alert");

        alerts.forEach(function (alert) {

            alert.classList.add("fade-out");

            setTimeout(function () {

                if (alert && alert.parentNode) {
                    alert.remove();
                }

            }, 400);

        });

    }, 5000);


    /* =====================================================
       DELETE CONFIRMATION
       ===================================================== */

    const deleteButtons =
        document.querySelectorAll(".delete-button");

    deleteButtons.forEach(function (button) {

        button.addEventListener("click", function (event) {

            const confirmed = confirm(
                "Are you sure you want to delete this request?"
            );

            if (!confirmed) {
                event.preventDefault();
            }

        });

    });


    /* =====================================================
       REQUEST FORM
       ===================================================== */

    const requestForm =
        document.querySelector(".request-form");

    if (requestForm) {

        requestForm.addEventListener(
            "submit",
            function () {

                const submitButton =
                    requestForm.querySelector(
                        "button[type='submit']"
                    );

                if (submitButton) {

                    submitButton.disabled = true;

                    submitButton.querySelector("span")
                        ?.replaceChildren(
                            document.createTextNode("Submitting...")
                        );

                }

            }
        );

    }

});