/* =========================================================
   STORIES E-LIBRARY
   BOOKS / CATEGORY PAGE
   ========================================================= */


/* =========================================================
   INITIALIZATION
   ========================================================= */

document.addEventListener(
    "DOMContentLoaded",
    function () {

        initializeLucide();

        initializeBookAnimations();

        initializeSearch();

    }
);


/* =========================================================
   LUCIDE
   ========================================================= */

function initializeLucide() {

    if (
        typeof lucide !== "undefined"
    ) {

        lucide.createIcons();

    }

}


/* =========================================================
   BOOK CARD ANIMATIONS
   ========================================================= */

function initializeBookAnimations() {

    const cards =
        document.querySelectorAll(
            ".book-card, .category-card"
        );


    cards.forEach(
        function (card, index) {

            card.style.animationDelay =
                Math.min(
                    index * 0.06,
                    0.45
                ) + "s";

        }
    );

}


/* =========================================================
   SEARCH
   ========================================================= */

function initializeSearch() {

    const searchInputs =
        document.querySelectorAll(
            ".search-input-wrapper input"
        );


    searchInputs.forEach(
        function (input) {

            input.addEventListener(
                "keydown",
                function (event) {

                    if (
                        event.key === "Escape"
                    ) {

                        input.value = "";

                        input.focus();

                    }

                }
            );

        }
    );

}


/* =========================================================
   IMAGE ERROR HANDLING
   ========================================================= */

document.addEventListener(
    "error",
    function (event) {

        const image =
            event.target;


        if (
            image &&
            image.tagName === "IMG"
        ) {

            image.classList.add(
                "image-error"
            );

        }

    },
    true
);

