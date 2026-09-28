/* ============================================================
   STORIES E-LIBRARY — PROFILE JAVASCRIPT
   ============================================================ */


document.addEventListener("DOMContentLoaded", function () {


    /* ========================================================
       LUCIDE ICONS
       ======================================================== */

    if (window.lucide) {
        lucide.createIcons();
    }



    /* ========================================================
       MOBILE NAVIGATION
       Same behavior as dashboard.js
       ======================================================== */

    var mobileMenuButton =
        document.getElementById("mobileMenuButton");

    var mobileNav =
        document.getElementById("mobileNav");


    if (mobileMenuButton && mobileNav) {


        mobileMenuButton.addEventListener("click", function () {

            var isOpen =
                mobileNav.classList.toggle("open");


            mobileMenuButton.setAttribute(
                "aria-expanded",
                isOpen ? "true" : "false"
            );


            mobileMenuButton.setAttribute(
                "aria-label",
                isOpen ? "Close menu" : "Open menu"
            );


            mobileMenuButton.innerHTML =
                isOpen
                    ? '<i data-lucide="x"></i>'
                    : '<i data-lucide="menu"></i>';


            if (window.lucide) {
                lucide.createIcons();
            }

        });



        /* Close mobile menu after clicking a link */

        mobileNav
            .querySelectorAll("a")
            .forEach(function (link) {

                link.addEventListener("click", function () {

                    mobileNav.classList.remove("open");

                    mobileMenuButton.setAttribute(
                        "aria-expanded",
                        "false"
                    );

                    mobileMenuButton.setAttribute(
                        "aria-label",
                        "Open menu"
                    );

                    mobileMenuButton.innerHTML =
                        '<i data-lucide="menu"></i>';


                    if (window.lucide) {
                        lucide.createIcons();
                    }

                });

            });



        /* Close mobile navigation on desktop resize */

        window.addEventListener("resize", function () {

            if (window.innerWidth > 800) {

                mobileNav.classList.remove("open");

                mobileMenuButton.setAttribute(
                    "aria-expanded",
                    "false"
                );

                mobileMenuButton.setAttribute(
                    "aria-label",
                    "Open menu"
                );

                mobileMenuButton.innerHTML =
                    '<i data-lucide="menu"></i>';


                if (window.lucide) {
                    lucide.createIcons();
                }

            }

        });

    }



    /* ========================================================
       PROFILE EDITING
       ======================================================== */

    var nameInput =
        document.getElementById("name");

    var emailInput =
        document.getElementById("email");

    var editButton =
        document.getElementById("editButton");

    var saveButton =
        document.getElementById("saveButton");

    var cancelButton =
        document.getElementById("cancelButton");


    if (
        nameInput &&
        emailInput &&
        editButton &&
        saveButton &&
        cancelButton
    ) {


        var originalName =
            nameInput.value;

        var originalEmail =
            emailInput.value;



        /* ====================================================
           ENABLE EDITING
           ==================================================== */

        editButton.addEventListener(
            "click",
            function () {

                nameInput.removeAttribute("readonly");

                emailInput.removeAttribute("readonly");


                nameInput.classList.add("editing");

                emailInput.classList.add("editing");


                editButton.style.display =
                    "none";


                saveButton.style.display =
                    "inline-flex";


                cancelButton.style.display =
                    "inline-flex";


                nameInput.focus();

            }
        );



        /* ====================================================
           CANCEL EDITING
           ==================================================== */

        cancelButton.addEventListener(
            "click",
            function () {

                nameInput.value =
                    originalName;

                emailInput.value =
                    originalEmail;


                nameInput.setAttribute(
                    "readonly",
                    "readonly"
                );

                emailInput.setAttribute(
                    "readonly",
                    "readonly"
                );


                nameInput.classList.remove(
                    "editing"
                );

                emailInput.classList.remove(
                    "editing"
                );


                editButton.style.display =
                    "inline-flex";


                saveButton.style.display =
                    "none";


                cancelButton.style.display =
                    "none";

            }
        );

    }



    /* ========================================================
       CLOSE SUCCESS / ERROR MESSAGE
       ======================================================== */

    var messageCloseButtons =
        document.querySelectorAll(
            ".profile-message .message-close"
        );


    messageCloseButtons.forEach(
        function (button) {

            button.addEventListener(
                "click",
                function () {

                    var message =
                        button.closest(
                            ".profile-message"
                        );


                    if (message) {

                        message.style.opacity = "0";

                        message.style.transform =
                            "translateY(-5px)";


                        setTimeout(
                            function () {

                                message.remove();

                            },
                            200
                        );

                    }

                }
            );

        }
    );



    /* ========================================================
       FORM SUBMISSION STATE
       ======================================================== */

    var profileForm =
        document.getElementById("profileForm");


    if (profileForm && saveButton) {

        profileForm.addEventListener(
            "submit",
            function () {

                saveButton.disabled = true;

                saveButton.innerHTML =
                    '<i data-lucide="loader-circle"></i>' +
                    '<span>Saving...</span>';


                if (window.lucide) {
                    lucide.createIcons();
                }

            }
        );

    }


});