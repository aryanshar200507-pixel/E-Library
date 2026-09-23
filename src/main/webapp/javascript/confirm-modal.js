/* =========================================================
   CUSTOM CONFIRM MODAL
   Usage: add data-confirm="message" to any link, button or form
   ========================================================= */

(function () {
    "use strict";

    var overlay, titleEl, messageEl, cancelBtn, confirmBtn;
    var pendingAction = null;
    var lastFocused = null;


    /* ---------- build modal once ---------- */

    function build() {

        overlay = document.createElement("div");
        overlay.className = "confirm-overlay";
        overlay.setAttribute("aria-hidden", "true");

        overlay.innerHTML =
            '<div class="confirm-dialog" role="alertdialog" aria-modal="true"' +
            ' aria-labelledby="confirmTitle" aria-describedby="confirmMessage">' +
                '<div class="confirm-icon">' +
                    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor"' +
                    ' stroke-width="2" stroke-linecap="round" stroke-linejoin="round">' +
                    '<path d="M3 6h18"/><path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"/>' +
                    '<path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"/>' +
                    '<line x1="10" x2="10" y1="11" y2="17"/>' +
                    '<line x1="14" x2="14" y1="11" y2="17"/></svg>' +
                '</div>' +
                '<h3 class="confirm-title" id="confirmTitle"></h3>' +
                '<p class="confirm-message" id="confirmMessage"></p>' +
                '<div class="confirm-actions">' +
                    '<button type="button" class="confirm-btn confirm-cancel">Cancel</button>' +
                    '<button type="button" class="confirm-btn confirm-ok">Delete</button>' +
                '</div>' +
            '</div>';

        document.body.appendChild(overlay);

        titleEl    = overlay.querySelector(".confirm-title");
        messageEl  = overlay.querySelector(".confirm-message");
        cancelBtn  = overlay.querySelector(".confirm-cancel");
        confirmBtn = overlay.querySelector(".confirm-ok");

        cancelBtn.addEventListener("click", function () { close(true); });

        confirmBtn.addEventListener("click", function () {
            var action = pendingAction;
            confirmBtn.disabled = true;
            confirmBtn.textContent = "Deleting…";
            if (action) action();
        });

        // click on dark backdrop closes
        overlay.addEventListener("click", function (e) {
            if (e.target === overlay) close(true);
        });
    }


    /* ---------- open / close ---------- */

    function open(source, action) {

        if (!overlay) build();

        var data = source.dataset;

        titleEl.textContent   = data.confirmTitle || "Delete this item?";
        messageEl.textContent = data.confirm || "This action cannot be undone.";

        confirmBtn.textContent = data.confirmLabel || "Delete";
        confirmBtn.disabled = false;

        pendingAction = action;
        lastFocused = document.activeElement;

        overlay.classList.add("open");
        overlay.setAttribute("aria-hidden", "false");
        document.body.classList.add("confirm-lock");

        cancelBtn.focus();   // safe default: Cancel
    }

    function close(restoreFocus) {

        if (!overlay) return;

        overlay.classList.remove("open");
        overlay.setAttribute("aria-hidden", "true");
        document.body.classList.remove("confirm-lock");

        pendingAction = null;

        if (restoreFocus && lastFocused && lastFocused.focus) {
            lastFocused.focus();
        }
    }

    function isOpen() {
        return overlay && overlay.classList.contains("open");
    }


    /* ---------- links and buttons with data-confirm ---------- */

    document.addEventListener("click", function (e) {

        var el = e.target.closest("[data-confirm]");

        if (!el || el.tagName === "FORM") return;

        e.preventDefault();

        open(el, function () {

            if (el.tagName === "A") {
                window.location.href = el.href;

            } else if (el.form) {
                if (el.form.requestSubmit) el.form.requestSubmit(el);
                else el.form.submit();
            }
        });
    });


    /* ---------- forms with data-confirm ---------- */

    document.addEventListener("submit", function (e) {

        var form = e.target;

        if (!form.hasAttribute("data-confirm")) return;
        if (form.dataset.confirmed === "true") return;

        e.preventDefault();

        open(form, function () {
            form.dataset.confirmed = "true";
            form.submit();
        });
    });


    /* ---------- keyboard: Esc closes, Tab stays inside ---------- */

    document.addEventListener("keydown", function (e) {

        if (!isOpen()) return;

        if (e.key === "Escape") {
            close(true);
            return;
        }

        if (e.key === "Tab") {
            var first = cancelBtn;
            var last  = confirmBtn.disabled ? cancelBtn : confirmBtn;

            if (e.shiftKey && document.activeElement === first) {
                e.preventDefault();
                last.focus();
            } else if (!e.shiftKey && document.activeElement === last) {
                e.preventDefault();
                first.focus();
            }
        }
    });


    /* ---------- reset when user comes back with browser Back ---------- */

    window.addEventListener("pageshow", function () {
        close(false);
    });


    /* ---------- auto-convert old inline confirm() calls ----------
       onclick="return confirm('...')"  ->  data-confirm="..."
       so existing pages work without editing each one */

    function convertInlineConfirms() {

        var selector = "[onclick*='confirm('], [onsubmit*='confirm(']";

        document.querySelectorAll(selector).forEach(function (el) {

            ["onclick", "onsubmit"].forEach(function (attr) {

                var code = el.getAttribute(attr);
                if (!code) return;

                var match = code.match(/confirm\(\s*(['"])([\s\S]*?)\1\s*\)/);
                if (!match) return;

                el.setAttribute("data-confirm", match[2]);
                el.removeAttribute(attr);
            });
        });
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", convertInlineConfirms);
    } else {
        convertInlineConfirms();
    }

})();