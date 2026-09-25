// =====================================================
// STORIES E-LIBRARY — USER SUGGESTION PAGE
// Lucide Icons | Character Counter | Form Validation
// =====================================================

document.addEventListener("DOMContentLoaded", function () {

    // =================================================
    // LUCIDE ICONS
    // =================================================

    function refreshIcons() {
        if (window.lucide) {
            window.lucide.createIcons();
        } else {
            console.error("Lucide library failed to load.");
        }
    }

    refreshIcons();

    // =================================================
    // ELEMENTS
    // =================================================

    const form = document.getElementById("suggestionForm");
    const textarea = document.getElementById("description");
    const characterCount = document.getElementById("characterCount");
    const characterCounter = document.getElementById("characterCounter");
    const validationMessage = document.getElementById("validationMessage");
    const submitButton = document.getElementById("submitButton");

    const MIN_LENGTH = 10;
    const MAX_LENGTH = 1000;

    // =================================================
    // CHARACTER COUNTER
    // =================================================

    function updateCharacterCount() {

        if (!textarea || !characterCount || !characterCounter) {
            return;
        }

        const length = textarea.value.length;

        characterCount.textContent = length;

        characterCounter.classList.remove(
            "near-limit",
            "limit-reached"
        );

        if (length >= MAX_LENGTH) {
            characterCounter.classList.add("limit-reached");
        } else if (length >= MAX_LENGTH * 0.9) {
            characterCounter.classList.add("near-limit");
        }
    }

    if (textarea) {
        textarea.addEventListener("input", function () {

            updateCharacterCount();

            if (validationMessage) {
                validationMessage.hidden = true;
                validationMessage.textContent = "";
            }
        });
    }

    updateCharacterCount();

    // =================================================
    // FORM VALIDATION
    // =================================================

    function showValidationMessage(message) {

        if (!validationMessage) {
            return;
        }

        validationMessage.textContent = message;
        validationMessage.hidden = false;
    }

    if (form && textarea) {

        form.addEventListener("submit", function (event) {

            const description = textarea.value.trim();

            // Empty suggestion.
            if (description.length < MIN_LENGTH) {

                event.preventDefault();

                showValidationMessage(
                    "Please enter at least 10 characters for your suggestion."
                );

                textarea.focus();
                return;
            }

            // Maximum character limit.
            if (description.length > MAX_LENGTH) {

                event.preventDefault();

                showValidationMessage(
                    "Your suggestion cannot exceed 1000 characters."
                );

                textarea.focus();
                return;
            }

            // Prevent accidental double submission.
            if (submitButton) {

                submitButton.disabled = true;

                const submitText = submitButton.querySelector(".submit-text");

                if (submitText) {
                    submitText.textContent = "Submitting...";
                }

                const icon = submitButton.querySelector("svg");

                if (icon) {
                    icon.style.display = "none";
                }
            }
        });
    }

    // =================================================
    // TEXTAREA KEYBOARD SHORTCUT
    // =================================================

    if (textarea) {

        textarea.addEventListener("keydown", function (event) {

            // Ctrl + Enter or Cmd + Enter submits the form.
            if (
                event.key === "Enter" &&
                (event.ctrlKey || event.metaKey)
            ) {

                event.preventDefault();

                if (form && typeof form.requestSubmit === "function") {
                    form.requestSubmit();
                }
            }
        });
    }

    // =================================================
    // INITIALIZATION
    // =================================================

    refreshIcons();

});