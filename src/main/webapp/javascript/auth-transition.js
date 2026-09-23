/* =========================================================
   STORIES - AUTH PAGE TURN (login <-> register)
   ========================================================= */

(function () {
    "use strict";

    var KEY = "stories-auth-transition";
    var LEAVE_MS = 700;      // must match the .7s in auth-transition.css
    var CLEANUP_MS = 1000;   // after reveal finishes

    var root = document.documentElement;

    var reduceMotion =
        window.matchMedia &&
        window.matchMedia("(prefers-reduced-motion: reduce)").matches;


    /* ---------- ARRIVING ---------- */

    function playArrival() {

        if (!root.classList.contains("auth-arriving")) return;

        // wait for two frames so the page is painted under the sheet
        requestAnimationFrame(function () {
            requestAnimationFrame(function () {
                root.classList.add("auth-reveal");
            });
        });

        setTimeout(function () {
            root.classList.remove("auth-arriving", "auth-reveal");
            root.removeAttribute("data-auth-dir");
        }, CLEANUP_MS);
    }


    /* ---------- LEAVING ---------- */

    function bindLinks() {

        var overlay = document.getElementById("pageTurnOverlay");
        var links = document.querySelectorAll(".page-turn-link");

        links.forEach(function (link) {

            link.addEventListener("click", function (event) {

                // ctrl / cmd / middle click: normal browser behaviour
                if (event.metaKey || event.ctrlKey ||
                    event.shiftKey || event.button !== 0) return;

                event.preventDefault();

                if (root.classList.contains("auth-leaving")) return;

                var href = link.href;

                if (reduceMotion || !overlay) {
                    window.location.href = href;
                    return;
                }

                // login -> register = forward, register -> login = back
                var dir = link.dataset.direction === "login"
                    ? "back"
                    : "forward";

                try { sessionStorage.setItem(KEY, dir); } catch (e) {}

                root.setAttribute("data-auth-dir", dir);
                root.classList.add("auth-leaving");

                var navigated = false;

                function go() {
                    if (navigated) return;
                    navigated = true;
                    window.location.href = href;
                }

                overlay.addEventListener("animationend", function (e) {
                    if (e.target === overlay) go();
                });

                setTimeout(go, LEAVE_MS + 200);   // safety net
            });
        });
    }


    /* ---------- Browser Back (page restored from cache) ---------- */

    window.addEventListener("pageshow", function (event) {

        if (!event.persisted) return;

        try { sessionStorage.removeItem(KEY); } catch (e) {}

        if (root.classList.contains("auth-leaving")) {

            var dir = root.getAttribute("data-auth-dir");

            root.classList.remove("auth-leaving");
            root.setAttribute(
                "data-auth-dir",
                dir === "forward" ? "back" : "forward"
            );
            root.classList.add("auth-arriving");

            playArrival();
        }
    });


    /* ---------- INIT ---------- */

    function init() {
        bindLinks();
        playArrival();
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", init);
    } else {
        init();
    }

})();