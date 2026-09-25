// =====================================================
// STORIES E-LIBRARY — APP SUGGESTIONS ADMIN
// Search, status filters, confirmations, Lucide icons
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

    const searchInput = document.getElementById("suggestionSearch");
    const filterButtons = document.querySelectorAll(".filter-btn");
    const suggestionCards = document.querySelectorAll(".suggestion-card");

    const visibleCount = document.getElementById("visibleCount");
    const noResults = document.getElementById("noResults");
    const suggestionsContainer = document.getElementById("suggestionsContainer");
    const clearFiltersBtn = document.getElementById("clearFilters");

    // =================================================
    // SEARCH AND FILTER
    // =================================================

    let activeFilter = "ALL";

    function filterSuggestions() {

        const searchTerm = searchInput
            ? searchInput.value.trim().toLowerCase()
            : "";

        let visible = 0;

        suggestionCards.forEach(function (card) {

            const status = card.dataset.status || "";

            // Search the visible card text.
            const searchableText = card.textContent.toLowerCase();

            const matchesSearch = searchableText.includes(searchTerm);

            const matchesFilter =
                activeFilter === "ALL" || status === activeFilter;

            const shouldShow = matchesSearch && matchesFilter;

            card.hidden = !shouldShow;

            if (shouldShow) {
                visible++;
            }
        });

        // Update visible count.
        if (visibleCount) {
            visibleCount.textContent = visible;
        }

        // Show the empty search state.
        if (noResults) {
            noResults.hidden = visible !== 0;
        }

        // Hide the card container when nothing matches.
        if (suggestionsContainer) {
            suggestionsContainer.hidden = visible === 0;
        }
    }

    // Search input.
    if (searchInput) {
        searchInput.addEventListener("input", filterSuggestions);
    }

    // Filter buttons.
    filterButtons.forEach(function (button) {

        button.addEventListener("click", function () {

            activeFilter = button.dataset.filter || "ALL";

            filterButtons.forEach(function (item) {
                item.classList.remove("active");
                item.setAttribute("aria-pressed", "false");
            });

            button.classList.add("active");
            button.setAttribute("aria-pressed", "true");

            filterSuggestions();
        });

        button.setAttribute(
            "aria-pressed",
            button.classList.contains("active") ? "true" : "false"
        );
    });

    // =================================================
    // CLEAR FILTERS
    // =================================================

    if (clearFiltersBtn) {

        clearFiltersBtn.addEventListener("click", function () {

            if (searchInput) {
                searchInput.value = "";
            }

            activeFilter = "ALL";

            filterButtons.forEach(function (button) {

                const isAll = button.dataset.filter === "ALL";

                button.classList.toggle("active", isAll);
                button.setAttribute(
                    "aria-pressed",
                    isAll ? "true" : "false"
                );
            });

            filterSuggestions();

            if (searchInput) {
                searchInput.focus();
            }
        });
    }

   
	// =================================================
	// CUSTOM ACCEPT CONFIRMATION MODAL
	// =================================================

	const acceptModal = document.getElementById("acceptSuggestionModal");
	const cancelAcceptBtn = document.getElementById("cancelAcceptSuggestion");
	const confirmAcceptBtn = document.getElementById("confirmAcceptSuggestion");

	let pendingAcceptForm = null;
	let previousFocusElement = null;

	function openAcceptModal(form) {
	    if (!acceptModal) {
	        console.error("Accept confirmation modal not found.");
	        return;
	    }

	    pendingAcceptForm = form;
	    previousFocusElement = document.activeElement;

	    acceptModal.classList.add("active");
	    acceptModal.setAttribute("aria-hidden", "false");

	    document.body.style.overflow = "hidden";

	    if (window.lucide) {
	        window.lucide.createIcons();
	    }

	    cancelAcceptBtn?.focus();
	}

	function closeAcceptModal() {
	    if (!acceptModal) return;

	    acceptModal.classList.remove("active");
	    acceptModal.setAttribute("aria-hidden", "true");

	    document.body.style.overflow = "";

	    pendingAcceptForm = null;

	    if (previousFocusElement) {
	        previousFocusElement.focus();
	    }
	}

	// Intercept Accept form submissions.
	document.querySelectorAll(
	    '.action-form input[name="action"][value="accept"]'
	).forEach(function (input) {
	    const form = input.closest("form");

	    if (!form) return;

	    form.addEventListener("submit", function (event) {

	        // Allow the form through after confirmation.
	        if (form.dataset.acceptConfirmed === "true") {
	            delete form.dataset.acceptConfirmed;
	            return;
	        }

	        event.preventDefault();
	        openAcceptModal(form);
	    });
	});

	// Cancel button.
	if (cancelAcceptBtn) {
	    cancelAcceptBtn.addEventListener("click", closeAcceptModal);
	}

	// Confirm button.
	if (confirmAcceptBtn) {
	    confirmAcceptBtn.addEventListener("click", function () {
	        if (!pendingAcceptForm) return;

	        const formToSubmit = pendingAcceptForm;

	        formToSubmit.dataset.acceptConfirmed = "true";

	        closeAcceptModal();

	        formToSubmit.requestSubmit();
	    });
	}

	// Close when clicking outside the modal.
	if (acceptModal) {
	    acceptModal.addEventListener("click", function (event) {
	        if (event.target === acceptModal) {
	            closeAcceptModal();
	        }
	    });
	}

	// Close with Escape.
	document.addEventListener("keydown", function (event) {
	    if (
	        event.key === "Escape" &&
	        acceptModal &&
	        acceptModal.classList.contains("active")
	    ) {
	        closeAcceptModal();
	    }
	});
    // =================================================
    // INITIAL FILTER STATE
    // =================================================

    filterSuggestions();

});