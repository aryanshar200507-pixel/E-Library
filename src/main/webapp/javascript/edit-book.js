// =====================================================
// STORIES E-LIBRARY — EDIT BOOK SCRIPT
// Reuses Add Book preview and upload behavior
// =====================================================

document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("editBookForm");

    const coverInput = document.getElementById("cover");
    const pdfInput = document.getElementById("pdf");

    const coverPreview = document.getElementById("coverPreview");
    const coverPlaceholder = document.getElementById("coverPlaceholder");
    const previewCaptionText = document.getElementById("previewCaptionText");

    const coverDropzone = document.getElementById("coverDropzone");
    const pdfDropzone = document.getElementById("pdfDropzone");

    const coverFileStatus = document.getElementById("coverFileStatus");
    const pdfFileStatus = document.getElementById("pdfFileStatus");

    const coverError = document.getElementById("coverError");
    const pdfError = document.getElementById("pdfError");

    const description = document.getElementById("description");
    const charCount = document.getElementById("charCount");

    const submitBtn = document.getElementById("submitBtn");

    let currentPreviewUrl = null;

    // ---------- LUCIDE ICONS ----------

    function refreshIcons() {
        if (window.lucide) {
            window.lucide.createIcons();
        }
    }

    // ---------- FILE STATUS ----------

    function updateFileStatus(element, iconName, message, hasFile) {
        element.replaceChildren();

        const icon = document.createElement("i");
        icon.setAttribute("data-lucide", iconName);

        const text = document.createElement("span");
        text.textContent = message;

        element.append(icon, text);
        element.classList.toggle("has-file", Boolean(hasFile));

        refreshIcons();
    }

    // ---------- INLINE ERRORS ----------

    function showError(element, message) {
        element.textContent = message;
        element.classList.add("visible");
    }

    function clearError(element) {
        element.textContent = "";
        element.classList.remove("visible");
    }

    // ---------- COVER PREVIEW ----------

    function previewCover(file) {
        if (!file) return;

        const allowedTypes = [
            "image/jpeg",
            "image/png",
            "image/webp"
        ];

        const allowedExtensions = ["jpg", "jpeg", "png", "webp"];
        const extension = file.name.split(".").pop().toLowerCase();

        if (
            !allowedExtensions.includes(extension) ||
            (file.type && !allowedTypes.includes(file.type))
        ) {
            coverInput.value = "";

            showError(
                coverError,
                "Invalid cover format. Select a JPG, PNG, or WebP image."
            );

            updateFileStatus(
                coverFileStatus,
                "file-image",
                "No replacement selected",
                false
            );

            return;
        }

        clearError(coverError);

        if (currentPreviewUrl) {
            URL.revokeObjectURL(currentPreviewUrl);
        }

        currentPreviewUrl = URL.createObjectURL(file);

        coverPreview.src = currentPreviewUrl;
        coverPreview.hidden = false;
        coverPlaceholder.hidden = true;

        previewCaptionText.textContent = "New cover preview";

        updateFileStatus(
            coverFileStatus,
            "file-image",
            file.name,
            true
        );
    }

    coverInput.addEventListener("change", function () {
        const file = this.files[0];

        if (file) {
            previewCover(file);
        } else {
            clearError(coverError);
            updateFileStatus(
                coverFileStatus,
                "file-image",
                "No replacement selected",
                false
            );
        }
    });

    // ---------- PDF VALIDATION ----------

    function validatePdf(file) {
        if (!file) return;

        const extension = file.name.split(".").pop().toLowerCase();

        if (extension !== "pdf") {
            pdfInput.value = "";

            showError(
                pdfError,
                "Invalid file format. Please select a PDF document."
            );

            updateFileStatus(
                pdfFileStatus,
                "file-text",
                "No replacement selected",
                false
            );

            return;
        }

        clearError(pdfError);

        updateFileStatus(
            pdfFileStatus,
            "file-text",
            file.name,
            true
        );
    }

    pdfInput.addEventListener("change", function () {
        const file = this.files[0];

        if (file) {
            validatePdf(file);
        } else {
            clearError(pdfError);
            updateFileStatus(
                pdfFileStatus,
                "file-text",
                "No replacement selected",
                false
            );
        }
    });

    // ---------- DRAG AND DROP ----------

    function setupDropzone(dropzone, input, callback) {

        ["dragenter", "dragover"].forEach(function (eventName) {
            dropzone.addEventListener(eventName, function (event) {
                event.preventDefault();
                event.stopPropagation();
                dropzone.classList.add("drag-over");
            });
        });

        ["dragleave", "dragend", "drop"].forEach(function (eventName) {
            dropzone.addEventListener(eventName, function (event) {
                event.preventDefault();
                event.stopPropagation();
                dropzone.classList.remove("drag-over");
            });
        });

        dropzone.addEventListener("drop", function (event) {
            const file = event.dataTransfer.files[0];

            if (!file) return;

            const dataTransfer = new DataTransfer();
            dataTransfer.items.add(file);
            input.files = dataTransfer.files;

            callback(file);
        });
    }

    setupDropzone(coverDropzone, coverInput, previewCover);
    setupDropzone(pdfDropzone, pdfInput, validatePdf);

    // ---------- DESCRIPTION CHARACTER COUNT ----------

    function updateCharacterCount() {
        charCount.textContent = description.value.length + " / 5000";
    }

    description.addEventListener("input", updateCharacterCount);
    updateCharacterCount();

    // ---------- FORM SUBMISSION ----------

    form.addEventListener("submit", function (event) {

        if (
            coverError.classList.contains("visible") ||
            pdfError.classList.contains("visible")
        ) {
            event.preventDefault();
            return;
        }

        submitBtn.disabled = true;

        const buttonText = submitBtn.querySelector("span");

        if (buttonText) {
            buttonText.textContent = "Updating Book...";
        }

        const icon = submitBtn.querySelector("svg");

        if (icon) {
            icon.classList.add("spin");
        }
    });

    // ---------- INITIALIZE ----------

    refreshIcons();
});