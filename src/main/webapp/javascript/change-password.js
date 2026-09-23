document.addEventListener("DOMContentLoaded", function () {

    initializeLucide();

    initializePasswordToggles();

    initializePasswordConfirmation();

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
   PASSWORD TOGGLES
   ========================================= */

function initializePasswordToggles() {

    const buttons =
        document.querySelectorAll(".password-toggle");


    buttons.forEach(function (button) {

        button.addEventListener("click", function () {

            const targetId =
                button.getAttribute("data-target");

            const input =
                document.getElementById(targetId);


            if (!input) {
                return;
            }


            if (input.type === "password") {

                input.type = "text";

                button.innerHTML =
                    '<i data-lucide="eye-off"></i>';

                button.setAttribute(
                    "aria-label",
                    "Hide password"
                );

            } else {

                input.type = "password";

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


/* =========================================
   CONFIRM PASSWORD
   ========================================= */

function initializePasswordConfirmation() {

    const form =
        document.getElementById(
            "changePasswordForm"
        );

    const newPassword =
        document.getElementById(
            "newPassword"
        );

    const confirmPassword =
        document.getElementById(
            "confirmPassword"
        );

    const feedback =
        document.getElementById(
            "passwordFeedback"
        );


    if (
        !form ||
        !newPassword ||
        !confirmPassword
    ) {
        return;
    }


    function checkPasswordMatch() {

        if (confirmPassword.value === "") {

            confirmPassword.setCustomValidity("");

            confirmPassword.classList.remove(
                "password-mismatch",
                "password-match"
            );

            if (feedback) {

                feedback.textContent = "";

                feedback.className =
                    "password-feedback";

            }

            return;
        }


        if (
            newPassword.value !==
            confirmPassword.value
        ) {

            confirmPassword.setCustomValidity(
                "New passwords do not match."
            );

            confirmPassword.classList.add(
                "password-mismatch"
            );

            confirmPassword.classList.remove(
                "password-match"
            );


            if (feedback) {

                feedback.textContent =
                    "Passwords do not match.";

                feedback.className =
                    "password-feedback error";

            }

        } else {

            confirmPassword.setCustomValidity("");

            confirmPassword.classList.remove(
                "password-mismatch"
            );

            confirmPassword.classList.add(
                "password-match"
            );


            if (feedback) {

                feedback.textContent =
                    "Passwords match.";

                feedback.className =
                    "password-feedback success";

            }

        }

    }


    newPassword.addEventListener(
        "input",
        checkPasswordMatch
    );


    confirmPassword.addEventListener(
        "input",
        checkPasswordMatch
    );


    form.addEventListener(
        "submit",
        function (event) {

            checkPasswordMatch();


            if (!form.checkValidity()) {

                event.preventDefault();

                confirmPassword.reportValidity();

            }

        }
    );

}