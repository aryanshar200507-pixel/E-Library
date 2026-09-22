/* =====================================================
   CATEGORY BOOKS JAVASCRIPT
   ===================================================== */


/* =====================================================
   INITIALIZE PAGE
   ===================================================== */

document.addEventListener("DOMContentLoaded", function() {

    initializeBookmarkButtons();
    initializeStackedCards();
    initializeLucideIcons();
	initializeInfiniteScroll();				

});


/* =====================================================
   INITIALIZE BOOKMARK BUTTONS
   ===================================================== */

function initializeBookmarkButtons() {

    const buttons =
        document.querySelectorAll(".bookmark-button");


    buttons.forEach(function(button) {

        const bookId =
            button.dataset.bookId;


        /* Click event */

        button.addEventListener("click", function() {

            toggleBookBookmark(button);

        });


        /* Load current bookmark status */

        loadBookmarkStatus(button, bookId);

    });

}


/* =====================================================
   TOGGLE BOOKMARK
   ===================================================== */

function toggleBookBookmark(button) {

    const bookId =
        button.dataset.bookId;


    if (!bookId) {

        console.error("Book ID is missing.");

        return;

    }


    const formData =
        new URLSearchParams();


    formData.append(
        "bookId",
        bookId
    );


    /* Prevent multiple clicks */

    button.disabled = true;


    fetch(
        contextPath + "/books/bookmark-book",
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

        .then(function(response) {

            if (!response.ok) {

                throw new Error(
                    "Failed to update bookmark."
                );

            }

            return response.text();

        })


        .then(function(action) {

            if (action === "added") {

                setBookmarkedState(
                    button,
                    true
                );

            }

            else if (action === "removed") {

                setBookmarkedState(
                    button,
                    false
                );

            }

            else {

                console.warn(
                    "Unexpected bookmark response:",
                    action
                );

            }

        })


        .catch(function(error) {

            console.error(
                "Bookmark error:",
                error
            );

            alert(
                "Unable to update bookmark."
            );

        })


        .finally(function() {

            button.disabled = false;

        });

}


/* =====================================================
   LOAD BOOKMARK STATUS
   ===================================================== */

function loadBookmarkStatus(button, bookId) {

    fetch(
        contextPath +
        "/books/bookmark-book?bookId=" +
        encodeURIComponent(bookId)
    )

        .then(function(response) {

            if (!response.ok) {

                throw new Error(
                    "Failed to check bookmark."
                );

            }

            return response.text();

        })


        .then(function(result) {

            if (result === "true") {

                setBookmarkedState(
                    button,
                    true
                );

            }

            else {

                setBookmarkedState(
                    button,
                    false
                );

            }

        })


        .catch(function(error) {

            console.error(
                "Bookmark status error:",
                error
            );

        });

}


/* =====================================================
   UPDATE BOOKMARK BUTTON UI
   ===================================================== */

function setBookmarkedState(button, bookmarked) {

    const text =
        button.querySelector("span");


    const icon =
        button.querySelector("svg");


    if (bookmarked) {

        button.classList.add(
            "bookmarked"
        );


        if (text) {

            text.textContent =
                "Bookmarked";

        }


        button.setAttribute(
            "aria-label",
            "Remove bookmark"
        );


        if (icon) {

            icon.setAttribute(
                "fill",
                "currentColor"
            );

        }

    }

    else {

        button.classList.remove(
            "bookmarked"
        );


        if (text) {

            text.textContent =
                "Bookmark";

        }


        button.setAttribute(
            "aria-label",
            "Bookmark book"
        );


        if (icon) {

            icon.removeAttribute(
                "fill"
            );

        }

    }

}


/* =====================================================
   LUCIDE ICONS
   ===================================================== */

function initializeLucideIcons() {

    if (
        typeof lucide !== "undefined" &&
        lucide.createIcons
    ) {

        lucide.createIcons();

    }

}

/* =====================================================
   STACKED CARDS (scroll stacking effect)
   ===================================================== */

const STACK = {
	topGap: 20,       // px below header where first card sticks
	step: 14,         // px each next card sticks lower (peek strip of older cards)
	maxSteps: 4,      // stop offsetting after this many cards
	scaleStep: 0.03,  // shrink per covering card
	dimStep: 0.09     // darkening per covering card
};

function initializeStackedCards() {

	const cards = Array.from(document.querySelectorAll(".book-card"));

	if (cards.length < 2) return;

	const header = document.querySelector(".site-header");

	let stickTops = [];
	let enabled = false;
	let ticking = false;

	function clamp(value, min, max) {
		return Math.min(max, Math.max(min, value));
	}

	/* Set sticky offsets (also re-run on resize) */
	function layout() {

		enabled = getComputedStyle(cards[0]).position === "sticky";

		const headerHeight = header ? header.offsetHeight : 0;

		stickTops = cards.map(function (_, i) {
			return headerHeight + STACK.topGap +
				Math.min(i, STACK.maxSteps) * STACK.step;
		});

		cards.forEach(function (card, i) {
			card.style.top = enabled ? stickTops[i] + "px" : "";
		});

		update();
	}

	/* Runs on every scroll frame */
	function update() {

		ticking = false;

		if (!enabled) {
			cards.forEach(function (card) {
				card.style.transform = "";
				card.style.removeProperty("--dim");
			});
			return;
		}

		const viewportHeight = window.innerHeight;

		/* arrival[j]: 0 = card j not touching card j-1 yet,
		               1 = card j fully settled on the stack */
		const arrival = cards.map(function (card, j) {

			if (j === 0) return 0;

			const top = card.getBoundingClientRect().top;

			const prevBottom = Math.min(
				stickTops[j - 1] + cards[j - 1].offsetHeight,
				viewportHeight
			);

			const span = prevBottom - stickTops[j];

			if (span <= 0) return top <= stickTops[j] ? 1 : 0;

			return clamp((prevBottom - top) / span, 0, 1);
		});

		/* depth = how many cards are stacked on top of card i */
		let depth = 0;

		for (let i = cards.length - 1; i >= 0; i--) {

			const d = Math.min(depth, STACK.maxSteps);

			cards[i].style.transform = d > 0
				? "scale(" + (1 - d * STACK.scaleStep).toFixed(4) + ")"
				: "";

			cards[i].style.setProperty(
				"--dim",
				(d * STACK.dimStep).toFixed(3)
			);

			depth += arrival[i];
		}
	}

	window.addEventListener("scroll", function () {
		if (!ticking) {
			ticking = true;
			requestAnimationFrame(update);
		}
	}, { passive: true });

	window.addEventListener("resize", layout);
	window.addEventListener("load", layout);

	layout();
}

/* =====================================================
   INFINITE SCROLL PAGINATION
   ===================================================== */

function initializeInfiniteScroll() {

    const booksSection =
        document.getElementById("booksSection");

    const loader =
        document.getElementById("booksLoader");

    const endMessage =
        document.getElementById("booksEndMessage");


    if (!booksSection || !loader || !endMessage) {
        return;
    }


    let currentPage =
        parseInt(
            booksSection.dataset.currentPage,
            10
        ) || 1;


    const totalPages =
        parseInt(
            booksSection.dataset.totalPages,
            10
        ) || 1;


    const categoryId =
        booksSection.dataset.categoryId;


    const keyword =
        booksSection.dataset.keyword || "";


    let loading =
        false;


    /* =================================================
       SHOW END MESSAGE
       ================================================= */

    function showEndMessage() {

        loader.classList.remove("visible");

        endMessage.classList.add("visible");

        if (
            typeof lucide !== "undefined" &&
            lucide.createIcons
        ) {
            lucide.createIcons();
        }
    }


    /* =================================================
       LOAD NEXT PAGE
       ================================================= */

    function loadNextPage() {

        /* Already loading */

        if (loading) {
            return;
        }


        /* No more pages */

        if (currentPage >= totalPages) {

            showEndMessage();

            return;
        }


        loading = true;

        loader.classList.add("visible");


        const nextPage =
            currentPage + 1;


        const params =
            new URLSearchParams();


        params.append(
            "id",
            categoryId
        );


        if (keyword) {

            /*
             * Decode the keyword first because
             * JSP stored the URL encoded value.
             */

            let decodedKeyword = keyword;

            try {

                decodedKeyword =
                    decodeURIComponent(keyword);

            } catch (error) {

                console.warn(
                    "Unable to decode keyword:",
                    error
                );
            }


            params.append(
                "keyword",
                decodedKeyword
            );
        }


        params.append(
            "page",
            nextPage
        );


        fetch(
            contextPath +
            "/books/category?" +
            params.toString()
        )

        .then(function(response) {

            if (!response.ok) {

                throw new Error(
                    "Failed to load next page."
                );
            }

            return response.text();
        })


        .then(function(html) {

            /*
             * Convert returned JSP HTML
             * into a temporary document.
             */

            const parser =
                new DOMParser();


            const documentObject =
                parser.parseFromString(
                    html,
                    "text/html"
                );


            const newCards =
                documentObject.querySelectorAll(
                    ".book-card"
                );


            /*
             * Add each new card to
             * the current books section.
             */

            newCards.forEach(function(card) {

                booksSection.appendChild(
                    document.importNode(
                        card,
                        true
                    )
                );

            });


            /*
             * Update current page.
             */

            currentPage =
                nextPage;


            booksSection.dataset.currentPage =
                currentPage;


            /*
             * Reinitialize bookmark buttons
             * for newly loaded cards.
             */

            initializeBookmarkButtons();


            /*
             * Reinitialize Lucide icons.
             */

            initializeLucideIcons();


            /*
             * Reinitialize stacking effect.
             */

            initializeStackedCards();


            /*
             * Check whether this was
             * the final page.
             */

            if (
                currentPage >= totalPages
            ) {

                showEndMessage();

            }

        })


        .catch(function(error) {

            console.error(
                "Infinite scroll error:",
                error
            );

        })


        .finally(function() {

            loading = false;

            /*
             * Keep loader hidden unless
             * another request is running.
             */

            loader.classList.remove(
                "visible"
            );

        });

    }


    /* =================================================
       CREATE SCROLL SENTINEL
       ================================================= */

    const sentinel =
        document.createElement("div");


    sentinel.className =
        "infinite-scroll-sentinel";


    sentinel.setAttribute(
        "aria-hidden",
        "true"
    );


    /*
     * Put sentinel after the books.
     */

    booksSection.insertAdjacentElement(
        "afterend",
        sentinel
    );


    /* =================================================
       INTERSECTION OBSERVER
       ================================================= */

    const observer =
        new IntersectionObserver(
            function(entries) {

                if (
                    entries[0].isIntersecting
                ) {

                    loadNextPage();

                }

            },
            {
                root: null,

                /*
                 * Start loading before the
                 * user reaches the very bottom.
                 */

                rootMargin:
                    "900px 0px 900px 0px",

                threshold: 0
            }
        );


    observer.observe(
        sentinel
    );


    /* =================================================
       INITIAL END CHECK
       ================================================= */

    if (
        currentPage >= totalPages
    ) {

        showEndMessage();

    }

}

