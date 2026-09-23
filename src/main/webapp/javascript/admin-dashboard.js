/* =========================================================
   STORIES — ADMIN DASHBOARD
   ========================================================= */

document.addEventListener("DOMContentLoaded", function () {

    initializeLucide();

    initializeCategoryEditing();

    initializeDeleteModal();

});


/* =========================================================
   LUCIDE
   ========================================================= */

function initializeLucide() {

    if (typeof lucide !== "undefined") {
        lucide.createIcons();
    }

}


/* =========================================================
   CATEGORY EDITING
   ========================================================= */

function initializeCategoryEditing() {

    const editButtons =
        document.querySelectorAll(".edit-icon");

    const cancelButtons =
        document.querySelectorAll(".cancel-icon");


    /*
     * EDIT
     */

    editButtons.forEach(function (button) {

        button.addEventListener("click", function () {

            const categoryId =
                button.dataset.categoryId;

            const editForm =
                document.getElementById(
                    "edit-form-" + categoryId
                );

            if (!editForm) {
                return;
            }


            /*
             * Hide edit button
             */

            button.style.display = "none";


            /*
             * Show edit form
             */

            editForm.classList.add("active");


            /*
             * Focus input
             */

            const input =
                editForm.querySelector(
                    'input[name="categoryName"]'
                );

            if (input) {

                input.focus();

                input.select();

            }

        });

    });


    /*
     * CANCEL
     */

    cancelButtons.forEach(function (button) {

        button.addEventListener("click", function () {

            const categoryId =
                button.dataset.categoryId;

            const editForm =
                document.getElementById(
                    "edit-form-" + categoryId
                );

            const editButton =
                document.querySelector(
                    '.edit-icon[data-category-id="' +
                    categoryId +
                    '"]'
                );


            if (editForm) {
                editForm.classList.remove("active");
            }


            if (editButton) {
                editButton.style.display = "inline-grid";
            }

        });

    });

}


/* =========================================================
   DELETE MODAL
   ========================================================= */

function initializeDeleteModal() {

    const modal =
        document.getElementById("deleteModal");

    if (!modal) {
        return;
    }


    const cancelButton =
        document.getElementById(
            "deleteCancelButton"
        );

    const confirmButton =
        document.getElementById(
            "deleteConfirmButton"
        );

    const backdrop =
        modal.querySelector(
            ".delete-modal-backdrop"
        );


    let selectedForm = null;


    /*
     * OPEN MODAL
     */

    function openDeleteModal(form) {

        selectedForm = form;

        modal.classList.add("active");

        modal.setAttribute(
            "aria-hidden",
            "false"
        );

        document.body.style.overflow = "hidden";


        setTimeout(function () {

            if (cancelButton) {
                cancelButton.focus();
            }

        }, 100);

    }


    /*
     * CLOSE MODAL
     */

    function closeDeleteModal() {

        modal.classList.remove("active");

        modal.setAttribute(
            "aria-hidden",
            "true"
        );

        document.body.style.overflow = "";

        selectedForm = null;

    }


    /*
     * DELETE FORM SUBMISSION
     */

    const deleteForms =
        document.querySelectorAll(
            ".delete-category-form"
        );


    deleteForms.forEach(function (form) {

        form.addEventListener(
            "submit",
            function (event) {

                event.preventDefault();

                openDeleteModal(form);

            }
        );

    });


    /*
     * CANCEL
     */

    if (cancelButton) {

        cancelButton.addEventListener(
            "click",
            closeDeleteModal
        );

    }


    /*
     * BACKDROP
     */

    if (backdrop) {

        backdrop.addEventListener(
            "click",
            closeDeleteModal
        );

    }


    /*
     * CONFIRM DELETE
     */

    if (confirmButton) {

        confirmButton.addEventListener(
            "click",
            function () {

                if (!selectedForm) {
                    return;
                }


                confirmButton.disabled = true;

                confirmButton.innerHTML = `
                    <i data-lucide="loader-circle"
                       class="delete-loading-icon">
                    </i>
                    <span>Deleting...</span>
                `;


                initializeLucide();


                /*
                 * Submit the original POST form.
                 */

                HTMLFormElement.prototype.submit.call(
                    selectedForm
                );

            }
        );

    }


    /*
     * ESCAPE KEY
     */

    document.addEventListener(
        "keydown",
        function (event) {

            if (
                event.key === "Escape" &&
                modal.classList.contains("active")
            ) {

                closeDeleteModal();

            }

        }
    );

}