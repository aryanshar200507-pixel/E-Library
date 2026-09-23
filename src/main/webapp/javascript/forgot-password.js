document.addEventListener("DOMContentLoaded", function () {

    initializeLucide();

});


/* =========================================
   LUCIDE
   ========================================= */

function initializeLucide() {

    if (typeof lucide !== "undefined") {

        lucide.createIcons();

    }

}