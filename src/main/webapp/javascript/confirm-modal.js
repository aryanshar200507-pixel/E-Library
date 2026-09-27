// =====================================================
// GENERIC CONFIRM MODAL
// Reusable on any page. Two ways to use it:
//
// 1) Automatic — just add these attributes to any <form>:
//      data-confirm="Message shown in the dialog"
//      data-confirm-title="Optional title"      (defaults to "Are you sure?")
//      data-confirm-label="Optional OK label"    (defaults to "Confirm")
//    The form's normal submit is intercepted, the dialog is
//    shown, and the form only actually submits if the user
//    clicks the confirm button. No extra JS needed per page.
//
// 2) Manual — call the API directly from your own code:
//      ConfirmModal.confirm({
//          title: "Delete this item?",
//          message: "This action cannot be undone.",
//          confirmLabel: "Delete"
//      }).then(function (confirmed) {
//          if (confirmed) { /* do the thing */ }
//      });
// =====================================================

(function () {
    "use strict";

    let overlay = null;
    let titleEl = null;
    let messageEl = null;
    let cancelBtn = null;
    let okBtn = null;

    let activeResolve = null;
    let previousFocusElement = null;

    // =================================================
    // BUILD (once — reused for every confirmation)
    // =================================================

    function buildModal() {

        overlay = document.createElement("div");
        overlay.className = "confirm-overlay";
        overlay.setAttribute("aria-hidden", "true");

        overlay.innerHTML =
            '<div class="confirm-dialog" role="dialog" aria-modal="true" ' +
            'aria-labelledby="confirmModalTitle" aria-describedby="confirmModalMessage">' +
                '<div class="confirm-icon" aria-hidden="true">' +
                    '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" ' +
                    'stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">' +
                        '<path d="M10.29 3.86 1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0Z"></path>' +
                        '<path d="M12 9v4"></path>' +
                        '<path d="M12 17h.01"></path>' +
                    '</svg>' +
                '</div>' +
                '<h2 class="confirm-title" id="confirmModalTitle">Are you sure?</h2>' +
                '<p class="confirm-message" id="confirmModalMessage"></p>' +
                '<div class="confirm-actions">' +
                    '<button type="button" class="confirm-btn confirm-cancel">Cancel</button>' +
                    '<button type="button" class="confirm-btn confirm-ok">Confirm</button>' +
                '</div>' +
            '</div>';

        document.body.appendChild(overlay);

        titleEl = overlay.querySelector(".confirm-title");
        messageEl = overlay.querySelector(".confirm-message");
        cancelBtn = overlay.querySelector(".confirm-cancel");
        okBtn = overlay.querySelector(".confirm-ok");

        cancelBtn.addEventListener("click", function () {
            settle(false);
        });

        okBtn.addEventListener("click", function () {
            settle(true);
        });

        // Click outside the dialog closes it (treated as cancel).
        overlay.addEventListener("click", function (event) {
            if (event.target === overlay) {
                settle(false);
            }
        });

        // Escape closes it (treated as cancel).
        document.addEventListener("keydown", function (event) {
            if (event.key === "Escape" && overlay.classList.contains("open")) {
                settle(false);
            }
        });
    }

    function openModal(options) {

        if (!overlay) {
            buildModal();
        }

        titleEl.textContent = options.title || "Are you sure?";
        messageEl.textContent = options.message || "This action cannot be undone.";
        okBtn.textContent = options.confirmLabel || "Confirm";
        okBtn.disabled = false;

        previousFocusElement = document.activeElement;

        overlay.classList.add("open");
        overlay.setAttribute("aria-hidden", "false");
        document.body.classList.add("confirm-lock");

        okBtn.focus();
    }

    function closeModal() {

        if (!overlay) return;

        overlay.classList.remove("open");
        overlay.setAttribute("aria-hidden", "true");
        document.body.classList.remove("confirm-lock");

        if (previousFocusElement) {
            previousFocusElement.focus();
        }
    }

    function settle(result) {

        closeModal();

        const resolve = activeResolve;
        activeResolve = null;

        if (resolve) {
            resolve(result);
        }
    }

    // =================================================
    // PUBLIC API
    // =================================================

    window.ConfirmModal = {
        confirm: function (options) {

            options = options || {};

            return new Promise(function (resolve) {
                activeResolve = resolve;
                openModal(options);
            });
        }
    };

    // =================================================
    // AUTO-WIRE: any form with [data-confirm], on any page.
    // Listening on document (not on individual forms found at
    // load time) means this also catches forms added to the
    // page later, e.g. after an AJAX refresh.
    // =================================================

    document.addEventListener("submit", function (event) {

        const form = event.target;

        if (!(form instanceof HTMLFormElement)) return;
        if (!form.hasAttribute("data-confirm")) return;

        event.preventDefault();

        window.ConfirmModal.confirm({
            title: form.dataset.confirmTitle,
            message: form.dataset.confirm,
            confirmLabel: form.dataset.confirmLabel
        }).then(function (confirmed) {

            if (!confirmed) return;

            // Native submit — bypasses the 'submit' event entirely,
            // so this won't re-trigger this same listener and won't
            // be blocked by anything else on the page.
            form.submit();
        });
    });

})();