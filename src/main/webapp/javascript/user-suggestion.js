// =====================================================
// STORIES E-LIBRARY — USER SUGGESTION PAGE
// Responsive Header | Lucide Icons | Character Counter
// Form Validation | Keyboard Shortcuts
// =====================================================

document.addEventListener("DOMContentLoaded", function () {

    "use strict";

    // =================================================
    // 1. LUCIDE ICONS
    // =================================================

    function refreshIcons() {

        if (
            window.lucide &&
            typeof window.lucide.createIcons === "function"
        ) {
            window.lucide.createIcons();
        } else {
            console.error("Lucide library failed to load.");
        }

    }

    refreshIcons();


    // =================================================
    // 2. ELEMENTS
    // =================================================

    const form =
        document.getElementById("suggestionForm");

    const textarea =
        document.getElementById("description");

    const characterCount =
        document.getElementById("characterCount");

    const characterCounter =
        document.getElementById("characterCounter");

    const validationMessage =
        document.getElementById("validationMessage");

    const submitButton =
        document.getElementById("submitButton");


    // =================================================
    // HEADER ELEMENTS
    // IMPORTANT:
    // These IDs match the Book Request navbar exactly.
    // =================================================

    const menuButton =
        document.getElementById("mobileMenuButton");

    const mobileNav =
        document.getElementById("mobileNav");


    const MIN_LENGTH = 10;
    const MAX_LENGTH = 1000;


    // =================================================
    // 3. RESPONSIVE MOBILE NAVIGATION
    // =================================================

    function isMobileView() {

        return window.matchMedia(
            "(max-width: 800px)"
        ).matches;

    }


    function openMenu() {

        if (!menuButton || !mobileNav) {
            return;
        }

        mobileNav.classList.add("open");

        menuButton.setAttribute(
            "aria-expanded",
            "true"
        );

        menuButton.setAttribute(
            "aria-label",
            "Close navigation"
        );

        /*
         * Change menu icon to X.
         */

        menuButton.innerHTML =
            '<i data-lucide="x"></i>';

        refreshIcons();

    }


    function closeMenu() {

        if (!menuButton || !mobileNav) {
            return;
        }

        mobileNav.classList.remove("open");

        menuButton.setAttribute(
            "aria-expanded",
            "false"
        );

        menuButton.setAttribute(
            "aria-label",
            "Open navigation"
        );

        /*
         * Change X icon back to menu.
         */

        menuButton.innerHTML =
            '<i data-lucide="menu"></i>';

        refreshIcons();

    }


    function toggleMenu() {

        if (!menuButton || !mobileNav) {
            return;
        }

        const isOpen =
            mobileNav.classList.contains("open");

        if (isOpen) {
            closeMenu();
        } else {
            openMenu();
        }

    }


    // =================================================
    // MOBILE MENU EVENT HANDLERS
    // =================================================

    if (menuButton && mobileNav) {

        /*
         * Open / close menu.
         */

        menuButton.addEventListener(
            "click",
            function (event) {

                event.preventDefault();
                event.stopPropagation();

                toggleMenu();

            }
        );


        /*
         * Close menu after clicking a link.
         */

        const mobileLinks =
            mobileNav.querySelectorAll("a");

        mobileLinks.forEach(function (link) {

            link.addEventListener(
                "click",
                function () {

                    closeMenu();

                }
            );

        });


        /*
         * Close menu when clicking outside.
         */

        document.addEventListener(
            "click",
            function (event) {

                if (
                    isMobileView() &&
                    mobileNav.classList.contains("open") &&
                    !event.target.closest(".site-header")
                ) {
                    closeMenu();
                }

            }
        );


        /*
         * Close menu with Escape.
         */

        document.addEventListener(
            "keydown",
            function (event) {

                if (
                    event.key === "Escape" &&
                    mobileNav.classList.contains("open")
                ) {

                    closeMenu();

                    menuButton.focus();

                }

            }
        );


        /*
         * Close mobile menu when switching
         * back to desktop size.
         */

        window.addEventListener(
            "resize",
            function () {

                if (!isMobileView()) {
                    closeMenu();
                }

            }
        );


        /*
         * Initial state.
         */

        closeMenu();

    }


    // =================================================
    // 4. CHARACTER COUNTER
    // =================================================

    function updateCharacterCount() {

        if (
            !textarea ||
            !characterCount ||
            !characterCounter
        ) {
            return;
        }

        const length =
            textarea.value.length;

        characterCount.textContent =
            length;


        characterCounter.classList.remove(
            "near-limit",
            "limit-reached"
        );


        if (length >= MAX_LENGTH) {

            characterCounter.classList.add(
                "limit-reached"
            );

        } else if (
            length >= MAX_LENGTH * 0.9
        ) {

            characterCounter.classList.add(
                "near-limit"
            );

        }

    }


    if (textarea) {

        textarea.addEventListener(
            "input",
            function () {

                updateCharacterCount();


                if (validationMessage) {

                    validationMessage.hidden =
                        true;

                    validationMessage.textContent =
                        "";

                }

            }
        );

    }


    updateCharacterCount();


    // =================================================
    // 5. FORM VALIDATION
    // =================================================

    function showValidationMessage(message) {

        if (!validationMessage) {
            return;
        }

        validationMessage.textContent =
            message;

        validationMessage.hidden =
            false;

    }


    if (form && textarea) {

        form.addEventListener(
            "submit",
            function (event) {

                const description =
                    textarea.value.trim();


                // -----------------------------------------
                // Empty / too-short suggestion
                // -----------------------------------------

                if (
                    description.length <
                    MIN_LENGTH
                ) {

                    event.preventDefault();

                    showValidationMessage(
                        "Please enter at least 10 characters for your suggestion."
                    );

                    textarea.focus();

                    return;

                }


                // -----------------------------------------
                // Maximum character limit
                // -----------------------------------------

                if (
                    description.length >
                    MAX_LENGTH
                ) {

                    event.preventDefault();

                    showValidationMessage(
                        "Your suggestion cannot exceed 1000 characters."
                    );

                    textarea.focus();

                    return;

                }


                // -----------------------------------------
                // Prevent accidental double submission
                // -----------------------------------------

                if (submitButton) {

                    submitButton.disabled =
                        true;


                    const submitText =
                        submitButton.querySelector(
                            ".submit-text"
                        );


                    if (submitText) {

                        submitText.textContent =
                            "Submitting...";

                    }


                    const icon =
                        submitButton.querySelector(
                            "svg"
                        );


                    if (icon) {

                        icon.style.display =
                            "none";

                    }

                }

            }
        );

    }


    // =================================================
    // 6. TEXTAREA KEYBOARD SHORTCUT
    // =================================================

    if (textarea) {

        textarea.addEventListener(
            "keydown",
            function (event) {

                /*
                 * Ctrl + Enter
                 * OR
                 * Cmd + Enter
                 *
                 * submits the form.
                 */

                if (
                    event.key === "Enter" &&
                    (
                        event.ctrlKey ||
                        event.metaKey
                    )
                ) {

                    event.preventDefault();


                    if (
                        form &&
                        typeof form.requestSubmit ===
                            "function"
                    ) {

                        form.requestSubmit();

                    }

                }

            }
        );

    }


    // =================================================
    // 7. FINAL INITIALIZATION
    // =================================================

    refreshIcons();

});