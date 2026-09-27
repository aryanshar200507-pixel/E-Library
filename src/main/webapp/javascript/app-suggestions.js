
/* =====================================================
   STORIES E-LIBRARY — APP SUGGESTIONS ADMIN
   Search, status filters, reusable confirmation modal
   ===================================================== */

document.addEventListener("DOMContentLoaded", function () {
    "use strict";

    /* =================================================
       1. LUCIDE ICONS
       ================================================= */

    function refreshIcons() {
        if (
            window.lucide &&
            typeof window.lucide.createIcons === "function"
        ) {
            window.lucide.createIcons();
        }
    }

    refreshIcons();

    /* =================================================
       2. ELEMENTS
       ================================================= */

    const searchInput = document.getElementById("suggestionSearch");
    const filterButtons = document.querySelectorAll(".filter-btn");
    const suggestionCards = document.querySelectorAll(".suggestion-card");
    const visibleCount = document.getElementById("visibleCount");
    const noResults = document.getElementById("noResults");
    const suggestionsContainer = document.getElementById("suggestionsContainer");
    const clearFiltersBtn = document.getElementById("clearFilters");

    /* =================================================
       3. SEARCH AND FILTER
       ================================================= */

    let activeFilter = "ALL";

    function filterSuggestions() {
        const searchTerm = searchInput
            ? searchInput.value.trim().toLowerCase()
            : "";

        let visible = 0;

        suggestionCards.forEach(function (card) {
            const status = (card.dataset.status || "").toUpperCase();
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

        if (visibleCount) {
            visibleCount.textContent = visible;
        }

        if (noResults) {
            noResults.hidden = visible !== 0;
        }

        if (suggestionsContainer) {
            suggestionsContainer.hidden = visible === 0;
        }
    }

    if (searchInput) {
        searchInput.addEventListener("input", filterSuggestions);
    }

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

    /* =================================================
       4. CLEAR FILTERS
       ================================================= */

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

    /* =================================================
       5. FORM SAFETY CHECKS
       Confirmation itself is handled by confirm-modal.js.
       ================================================= */

    document.addEventListener("submit", function (event) {
        const form = event.target;

        if (!(form instanceof HTMLFormElement)) {
            return;
        }

        if (!form.classList.contains("action-form")) {
            return;
        }

        const actionInput = form.querySelector('input[name="action"]');
        const suggestionIdInput =
            form.querySelector('input[name="suggestionId"]');

        if (!actionInput || !suggestionIdInput) {
            event.preventDefault();
            console.error("Suggestion form is missing required fields.");
            return;
        }

        if (!suggestionIdInput.value.trim()) {
            event.preventDefault();
            console.error("Suggestion ID is missing.");
            return;
        }

        const validActions = ["accept", "delete"];

        if (!validActions.includes(actionInput.value)) {
            event.preventDefault();
            console.error("Invalid suggestion action:", actionInput.value);
        }
    }, true);

    /* =================================================
       6. INITIAL FILTER STATE
       ================================================= */

    filterSuggestions();
});