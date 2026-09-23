document.addEventListener("DOMContentLoaded", function () {

    initializeLucide();

    initializePasswordToggle();


});


/* =========================================
   LUCIDE
   ========================================= */

function initializeLucide() {

    if (typeof lucide !== "undefined") {

        lucide.createIcons();

    }

}


/* =========================================
   PASSWORD TOGGLE
   ========================================= */

function initializePasswordToggle() {

    const buttons =
        document.querySelectorAll(".password-toggle");

    buttons.forEach(function (button) {

        button.addEventListener("click", function () {

            const target =
                document.getElementById(
                    button.dataset.target
                );

            if (!target) {
                return;
            }


            if (target.type === "password") {

                target.type = "text";

                button.innerHTML =
                    '<i data-lucide="eye-off"></i>';

                button.setAttribute(
                    "aria-label",
                    "Hide password"
                );

            } else {

                target.type = "password";

                button.innerHTML =
                    '<i data-lucide="eye"></i>';

                button.setAttribute(
                    "aria-label",
                    "Show password"
                );

            }

            initializeLucide();

        });

    });

}
