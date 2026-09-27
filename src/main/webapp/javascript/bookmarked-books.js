
document.addEventListener("DOMContentLoaded", function () {

    function refreshIcons() {
        if (typeof lucide !== "undefined") {
            lucide.createIcons();
        }
    }

    /*
     * Initial Lucide icons
     */
    refreshIcons();


    /*
     * Mobile navigation
     */
    const menuButton =
        document.getElementById("mobileMenuButton");

    const mobileNav =
        document.getElementById("mobileNav");

    if (menuButton && mobileNav) {

        function openMobileMenu() {
            mobileNav.classList.add("open");

            menuButton.setAttribute(
                "aria-expanded",
                "true"
            );

            menuButton.setAttribute(
                "aria-label",
                "Close navigation"
            );

            menuButton.innerHTML =
                '<i data-lucide="x"></i>';

            refreshIcons();
        }

        function closeMobileMenu() {
            mobileNav.classList.remove("open");

            menuButton.setAttribute(
                "aria-expanded",
                "false"
            );

            menuButton.setAttribute(
                "aria-label",
                "Open navigation"
            );

            menuButton.innerHTML =
                '<i data-lucide="menu"></i>';

            refreshIcons();
        }

        function toggleMobileMenu() {
            const isOpen =
                mobileNav.classList.contains("open");

            if (isOpen) {
                closeMobileMenu();
            } else {
                openMobileMenu();
            }
        }

        menuButton.addEventListener(
            "click",
            function (event) {
                event.preventDefault();
                event.stopPropagation();
                toggleMobileMenu();
            }
        );

        const mobileLinks =
            mobileNav.querySelectorAll("a");

        mobileLinks.forEach(function (link) {
            link.addEventListener(
                "click",
                function () {
                    closeMobileMenu();
                }
            );
        });

        document.addEventListener(
            "click",
            function (event) {

                if (
                    !mobileNav.classList.contains("open")
                ) {
                    return;
                }

                const clickedInsideNav =
                    mobileNav.contains(event.target);

                const clickedMenuButton =
                    menuButton.contains(event.target);

                if (
                    !clickedInsideNav &&
                    !clickedMenuButton
                ) {
                    closeMobileMenu();
                }
            }
        );

        document.addEventListener(
            "keydown",
            function (event) {

                if (
                    event.key === "Escape" &&
                    mobileNav.classList.contains("open")
                ) {
                    closeMobileMenu();
                }
            }
        );

        let resizeTimer;

        window.addEventListener(
            "resize",
            function () {
                clearTimeout(resizeTimer);

                resizeTimer = setTimeout(
                    function () {
                        if (window.innerWidth > 800) {
                            closeMobileMenu();
                        }
                    },
                    100
                );
            }
        );

        menuButton.setAttribute(
            "aria-expanded",
            "false"
        );

        menuButton.setAttribute(
            "aria-label",
            "Open navigation"
        );
    }
});


/*
 * =========================================================
 * REMOVE BOOKMARK
 * =========================================================
 *
 * Uses the existing ConfirmModal API.
 *
 * Backend endpoint:
 * POST /books/bookmark-book?bookId=ID
 *
 */

function removeBookBookmark(button, bookId) {

    if (!button || !bookId) {
        return;
    }

    /*
     * Prevent duplicate clicks while confirmation is open.
     */
    if (button.disabled) {
        return;
    }

    if (
        typeof window.ConfirmModal === "undefined" ||
        typeof window.ConfirmModal.confirm !== "function"
    ) {
        console.error(
            "ConfirmModal is not loaded. " +
            "Please include confirm-modal.js before bookmarked-books.js."
        );
        return;
    }

    /*
     * Show the existing confirmation modal.
     */
    window.ConfirmModal.confirm({

        title: "Remove Bookmark?",

        message:
            "Are you sure you want to remove this book from your bookmarks?",

        confirmLabel: "Remove"

    }).then(function (confirmed) {

        /*
         * Cancel: do nothing.
         */
        if (!confirmed) {
            return;
        }

        /*
         * Prevent multiple clicks after confirmation.
         */
        button.disabled = true;

        const originalContent =
            button.innerHTML;

        /*
         * Show loading state.
         */
        button.innerHTML =
            '<i data-lucide="loader-circle"></i>' +
            '<span>Removing...</span>';

        if (typeof lucide !== "undefined") {
            lucide.createIcons();
        }

        const contextPath =
            getContextPath();

        /*
         * Send removal request to the backend.
         */
        fetch(
            contextPath +
            "/books/bookmark-book?bookId=" +
            encodeURIComponent(bookId),
            {
                method: "POST"
            }
        )
        .then(function (response) {

            if (!response.ok) {
                throw new Error(
                    "Failed to remove bookmark."
                );
            }

            return response.text();
        })
        .then(function (result) {

            result = result.trim();

            if (result === "removed") {

                const card =
                    button.closest(".book-card");

                if (card) {

                    card.style.opacity = "0";

                    card.style.transform =
                        "translateY(8px)";

                    setTimeout(function () {

                        card.remove();

                        checkEmptyBookmarks();

                    }, 220);
                }

            } else {

                alert(
                    "The bookmark could not be removed."
                );

                button.disabled = false;

                button.innerHTML =
                    originalContent;

                if (typeof lucide !== "undefined") {
                    lucide.createIcons();
                }
            }
        })
        .catch(function (error) {

            console.error(error);

            alert(
                "Something went wrong while removing the bookmark."
            );

            button.disabled = false;

            button.innerHTML =
                originalContent;

            if (typeof lucide !== "undefined") {
                lucide.createIcons();
            }
        });

    }).catch(function (error) {

        console.error(
            "Confirmation modal error:",
            error
        );

    });
}


/*
 * =========================================================
 * CHECK EMPTY BOOKMARKS
 * =========================================================
 *
 * If the last card on the current page is removed,
 * reload so the servlet can recalculate pagination.
 *
 */

function checkEmptyBookmarks() {

    const cards =
        document.querySelectorAll(".book-card");

    if (cards.length === 0) {
        window.location.reload();
    }
}


/*
 * =========================================================
 * GET CONTEXT PATH
 * =========================================================
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