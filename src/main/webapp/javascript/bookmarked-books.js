document.addEventListener("DOMContentLoaded", function() {

    // Create all Lucide icons
    if (typeof lucide !== "undefined") {
        lucide.createIcons();
    }

});


/*
 * Remove a whole-book bookmark.
 *
 * The request goes to the same servlet
 * already used for book-level bookmarks:
 *
 * POST /books/bookmark
 */
function removeBookBookmark(button, bookId) {

    if (!button || !bookId) {
        return;
    }

    const confirmed = confirm(
        "Remove this book from your bookmarks?"
    );

    if (!confirmed) {
        return;
    }


    // Prevent multiple clicks
    button.disabled = true;


    const contextPath = getContextPath();

    fetch(
        contextPath + "/books/bookmark-book?bookId=" + bookId,
        {
            method: "POST"
        }
    )
        .then(function(response) {

            if (!response.ok) {
                throw new Error(
                    "Failed to remove bookmark."
                );
            }

            return response.text();
        })
        .then(function(result) {

            result = result.trim();


            if (result === "removed") {

                const card =
                    button.closest(".book-card");

                if (card) {

                    card.remove();

                    checkEmptyBookmarks();
                }

            } else {

                alert(
                    "The bookmark could not be removed."
                );

                button.disabled = false;
            }

        })
        .catch(function(error) {

            console.error(error);

            alert(
                "Something went wrong while removing the bookmark."
            );

            button.disabled = false;
        });
}


/*
 * If the last book on the page was removed,
 * reload the page so pagination is recalculated.
 */
function checkEmptyBookmarks() {

    const cards =
        document.querySelectorAll(".book-card");

    if (cards.length === 0) {

        window.location.reload();
    }
}


/*
 * Get the application context path.
 *
 * Example:
 *
 * http://localhost:8080/Elibrary
 *
 * returns:
 *
 * /Elibrary
 */
function getContextPath() {

    const path =
        window.location.pathname;

    const firstSlash =
        path.indexOf("/", 1);

    if (firstSlash === -1) {

        return path;
    }

    return path.substring(
        0,
        firstSlash
    );
}