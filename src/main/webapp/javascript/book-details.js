/* =========================================================
   STORIES - BOOK DETAILS JAVASCRIPT
   ========================================================= */


/* =========================================================
   INITIALIZE PAGE
   ========================================================= */

document.addEventListener("DOMContentLoaded", function () {

    initializeLucideIcons();

    initializeBookmarkStatus();

    initializeRating();

});


/* =========================================================
   LUCIDE ICONS
   ========================================================= */

function initializeLucideIcons() {

    if (typeof lucide !== "undefined") {

        lucide.createIcons();

    }

}


/* =========================================================
   BOOKMARK
   ========================================================= */

function toggleBookBookmark(button) {

    const bookId =
        button.dataset.bookId;


    const formData =
        new URLSearchParams();


    formData.append(
        "bookId",
        bookId
    );


    fetch(
        getContextPath() +
        "/books/bookmark-book",
        {
            method: "POST",

            headers: {
                "Content-Type":
                    "application/x-www-form-urlencoded"
            },

            body:
                formData.toString()
        }
    )

    .then(function (response) {

        if (!response.ok) {

            throw new Error(
                "Failed to update bookmark."
            );

        }

        return response.text();

    })

    .then(function (action) {

        if (action === "added") {

            button.querySelector("span").textContent =
                "Bookmarked";

            button.classList.add(
                "bookmarked"
            );

        }

        else if (action === "removed") {

            button.querySelector("span").textContent =
                "Bookmark";

            button.classList.remove(
                "bookmarked"
            );

        }

    })

    .catch(function (error) {

        console.error(error);

        alert(
            "Unable to update bookmark."
        );

    });

}


/* =========================================================
   LOAD BOOKMARK STATUS
   ========================================================= */

function initializeBookmarkStatus() {

    const button =
        document.getElementById(
            "bookBookmarkButton"
        );


    if (!button) {

        return;

    }


    const bookId =
        button.dataset.bookId;


    fetch(
        getContextPath() +
        "/books/bookmark-book?bookId=" +
        encodeURIComponent(bookId)
    )

    .then(function (response) {

        if (!response.ok) {

            throw new Error(
                "Failed to check bookmark."
            );

        }

        return response.text();

    })

    .then(function (result) {

        if (result === "true") {

            const text =
                button.querySelector("span");

            if (text) {

                text.textContent =
                    "Bookmarked";

            }

            button.classList.add(
                "bookmarked"
            );

        }

    })

    .catch(function (error) {

        console.error(error);

    });

}


/* =========================================================
   STAR RATING
   ========================================================= */

function initializeRating() {

    const stars =
        document.querySelectorAll(
            ".touch-star"
        );


    const ratingValue =
        document.getElementById(
            "ratingValue"
        );


    const ratingSubmit =
        document.getElementById(
            "ratingSubmit"
        );


    if (
        !stars.length ||
        !ratingValue ||
        !ratingSubmit
    ) {

        return;

    }


    stars.forEach(function (star) {

        star.addEventListener(
            "click",
            function () {

                const selectedRating =
                    parseInt(
                        this.dataset.rating
                    );


                ratingValue.value =
                    selectedRating;


                ratingSubmit.disabled =
                    false;


                stars.forEach(
                    function (currentStar) {

                        const starRating =
                            parseInt(
                                currentStar.dataset.rating
                            );


                        if (
                            starRating <=
                            selectedRating
                        ) {

                            currentStar.textContent =
                                "★";

                        }

                        else {

                            currentStar.textContent =
                                "☆";

                        }

                    }
                );

            }
        );

    });

}


/* =========================================================
   EDIT COMMENT
   ========================================================= */

function showEditForm(commentId) {

    const editForm =
        document.getElementById(
            "edit-form-" + commentId
        );


    if (editForm) {

        editForm.style.display =
            "block";

    }

}


/* =========================================================
   HIDE EDIT COMMENT
   ========================================================= */

function hideEditForm(commentId) {

    const editForm =
        document.getElementById(
            "edit-form-" + commentId
        );


    if (editForm) {

        editForm.style.display =
            "none";

    }

}


/* =========================================================
   ANIMATE NEWLY ADDED COMMENT

   Call this right after a new comment element has been
   appended to .comments-list (e.g. if you switch the
   add-comment flow to AJAX instead of a full page reload).
   Restarts the commentReveal keyframe animation on that
   element specifically, so it animates in on its own
   instead of appearing statically.
   ========================================================= */

function animateNewComment(commentEl) {

    if (!commentEl) {

        return;

    }

    commentEl.style.animation =
        "none";

    /* force reflow so the animation restarts cleanly */

    void commentEl.offsetWidth;

    commentEl.style.animation =
        "commentReveal 0.5s cubic-bezier(0.16, 1, 0.3, 1) both";

}


/* =========================================================
   CONTEXT PATH
   ========================================================= */

function getContextPath() {

    const path =
        window.location.pathname;


    const firstSlash =
        path.indexOf("/", 1);


    if (firstSlash === -1) {

        return "";

    }


    return path.substring(
        0,
        firstSlash
    );

}