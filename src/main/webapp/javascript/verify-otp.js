document.addEventListener("DOMContentLoaded", function () {

    initializeLucide();

    initializeOtpInputs();

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
   OTP INPUT
   ========================================= */

function initializeOtpInputs() {

    const inputs =
        document.querySelectorAll(".otp-box");

    const hiddenInput =
        document.getElementById("otpValue");

    const form =
        document.getElementById("otpForm");


    if (
        inputs.length !== 6 ||
        !hiddenInput ||
        !form
    ) {
        return;
    }


    /* ================================
       TYPE DIGIT
       ================================ */

    inputs.forEach(function (input, index) {

        input.addEventListener(
            "input",
            function () {

                // Allow numbers only
                input.value =
                    input.value.replace(/\D/g, "");


                if (input.value.length > 0) {

                    if (
                        index < inputs.length - 1
                    ) {

                        inputs[index + 1].focus();

                    }

                }

                updateOtpValue();

            }
        );


        /* ================================
           BACKSPACE
        ================================= */

        input.addEventListener(
            "keydown",
            function (event) {

                if (
                    event.key === "Backspace" &&
                    input.value === "" &&
                    index > 0
                ) {

                    inputs[index - 1].focus();

                }

            }
        );


        /* ================================
           LEFT / RIGHT ARROW
        ================================= */

        input.addEventListener(
            "keydown",
            function (event) {

                if (
                    event.key === "ArrowLeft" &&
                    index > 0
                ) {

                    inputs[index - 1].focus();

                }


                if (
                    event.key === "ArrowRight" &&
                    index < inputs.length - 1
                ) {

                    inputs[index + 1].focus();

                }

            }
        );


        /* ================================
           PASTE OTP
        ================================= */

        input.addEventListener(
            "paste",
            function (event) {

                event.preventDefault();

                const pasted =
                    event.clipboardData
                        .getData("text")
                        .replace(/\D/g, "")
                        .slice(0, 6);


                pasted
                    .split("")
                    .forEach(function (digit, i) {

                        if (inputs[i]) {

                            inputs[i].value =
                                digit;

                        }

                    });


                updateOtpValue();


                if (pasted.length === 6) {

                    inputs[5].focus();

                } else {

                    inputs[pasted.length].focus();

                }

            }
        );

    });


    /* ================================
       UPDATE HIDDEN OTP
    ================================= */

    function updateOtpValue() {

        let otp = "";

        inputs.forEach(function (input) {

            otp += input.value;

        });

        hiddenInput.value = otp;

    }


    /* ================================
       FORM SUBMIT
    ================================= */

    form.addEventListener(
        "submit",
        function (event) {

            updateOtpValue();


            if (hiddenInput.value.length !== 6) {

                event.preventDefault();

                alert(
                    "Please enter the complete 6-digit OTP."
                );

                const firstEmpty =
                    Array.from(inputs)
                        .find(function (input) {
                            return input.value === "";
                        });

                if (firstEmpty) {

                    firstEmpty.focus();

                }

                return;

            }

        }
    );

}