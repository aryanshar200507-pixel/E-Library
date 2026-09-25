
/* =====================================================
   STORIES E-LIBRARY — ADD BOOK PAGE SCRIPT
===================================================== */

document.addEventListener("DOMContentLoaded", function () {

	const coverError = document.getElementById("coverError");
	const pdfError = document.getElementById("pdfError");
    // ---------- Lucide Icons ----------
    function refreshIcons() {
        if (window.lucide && typeof window.lucide.createIcons === "function") {
            window.lucide.createIcons();
        } else {
            console.error("Lucide library failed to load.");
        }
    }

    refreshIcons();

    // ---------- Elements ----------
    const form = document.getElementById("addBookForm");
    const coverInput = document.getElementById("cover");
    const pdfInput = document.getElementById("pdf");

    const coverPreview = document.getElementById("coverPreview");
    const coverPlaceholder = document.getElementById("coverPlaceholder");

    const coverDropzone = document.getElementById("coverDropzone");
    const pdfDropzone = document.getElementById("pdfDropzone");

    const coverFileStatus = document.getElementById("coverFileStatus");
    const pdfFileStatus = document.getElementById("pdfFileStatus");

    const description = document.getElementById("description");
    const charCount = document.getElementById("charCount");
    const submitBtn = document.getElementById("submitBtn");

    let currentCoverUrl = null;

    // ---------- File Size ----------
    function formatFileSize(bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }

        if (bytes < 1024 * 1024) {
            return (bytes / 1024).toFixed(1) + " KB";
        }

        return (bytes / (1024 * 1024)).toFixed(2) + " MB";
    }

    // ---------- File Status ----------
    function updateFileStatus(element, file, iconName) {
        element.classList.add("has-file");
        element.replaceChildren();

        const icon = document.createElement("i");
        icon.setAttribute("data-lucide", iconName);

        const text = document.createElement("span");
        text.textContent = file.name + " (" + formatFileSize(file.size) + ")";

        element.append(icon, text);

        refreshIcons();
    }

    function resetFileStatus(element, iconName, message) {
        element.classList.remove("has-file");
        element.replaceChildren();

        const icon = document.createElement("i");
        icon.setAttribute("data-lucide", iconName);

        const text = document.createElement("span");
        text.textContent = message;

        element.append(icon, text);

        refreshIcons();
    }

    // ---------- Cover Preview ----------
    function resetCover() {
        coverPreview.removeAttribute("src");
        coverPreview.hidden = true;
        coverPlaceholder.hidden = false;

        resetFileStatus(
            coverFileStatus,
            "file-image",
            "No cover selected"
        );

        if (currentCoverUrl) {
            URL.revokeObjectURL(currentCoverUrl);
            currentCoverUrl = null;
        }
    }

    function previewCover(file) {
        if (!file) return;

        const validTypes = [
            "image/jpeg",
            "image/png",
            "image/webp"
        ];

        if (!validTypes.includes(file.type)) {
            alert("Please select a JPG, PNG, or WebP image.");
            coverInput.value = "";
            resetCover();
            return;
        }

        if (currentCoverUrl) {
            URL.revokeObjectURL(currentCoverUrl);
        }

        currentCoverUrl = URL.createObjectURL(file);

        coverPreview.src = currentCoverUrl;
        coverPreview.hidden = false;
        coverPlaceholder.hidden = true;

        updateFileStatus(
            coverFileStatus,
            file,
            "image"
        );
    }

    coverInput.addEventListener("change", function () {
        const file = this.files[0];

        if (file) {
            previewCover(file);
        } else {
            resetCover();
        }
    });

    // ---------- PDF Validation ----------
    function validatePdf(file) {
        if (!file) return false;

        const isPdf =
            file.type === "application/pdf" ||
            file.name.toLowerCase().endsWith(".pdf");

        if (!isPdf) {
            alert("Please select a valid PDF file.");
            return false;
        }

        return true;
    }

    function resetPdf() {
        resetFileStatus(
            pdfFileStatus,
            "file-text",
            "No PDF selected"
        );
    }

    function handlePdf(file) {
        if (!file) return;

        if (!validatePdf(file)) {
            pdfInput.value = "";
            resetPdf();
            return;
        }

        updateFileStatus(
            pdfFileStatus,
            file,
            "file-text"
        );
    }

    pdfInput.addEventListener("change", function () {
        handlePdf(this.files[0]);
    });

    // ---------- Drag & Drop ----------
    function setupDragDrop(dropzone, input, onFileSelected) {
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
            const files = event.dataTransfer.files;

            if (!files || !files.length) return;

            const file = files[0];

            try {
                const transfer = new DataTransfer();
                transfer.items.add(file);
                input.files = transfer.files;
            } catch (error) {
                console.error("Unable to assign dropped file:", error);
                alert("Please use the file picker to select your file.");
                return;
            }

            onFileSelected(file);
        });
    }

    setupDragDrop(
        coverDropzone,
        coverInput,
        function (file) {
            previewCover(file);
        }
    );

    setupDragDrop(
        pdfDropzone,
        pdfInput,
        function (file) {
            handlePdf(file);
        }
    );

    // ---------- Description Character Counter ----------
    function updateCharCount() {
        const length = description.value.length;
        const maxLength = description.maxLength;

        charCount.textContent = length + " / " + maxLength;
    }

    description.addEventListener("input", updateCharCount);
    updateCharCount();

    // ---------- Form Submission ----------
    form.addEventListener("submit", function (event) {
        if (!form.checkValidity()) {
            event.preventDefault();
            form.reportValidity();
            return;
        }

        const coverFile = coverInput.files[0];
        const pdfFile = pdfInput.files[0];

        if (!coverFile || !pdfFile) {
            event.preventDefault();
            alert("Please upload both a book cover and a PDF.");
            return;
        }

        const validCoverTypes = [
            "image/jpeg",
            "image/png",
            "image/webp"
        ];

        if (!validCoverTypes.includes(coverFile.type)) {
            event.preventDefault();
            alert("Please upload a valid JPG, PNG, or WebP cover.");
            return;
        }

        if (!validatePdf(pdfFile)) {
            event.preventDefault();
            return;
        }

        // Prevent duplicate submissions.
        submitBtn.disabled = true;

        const buttonText = submitBtn.querySelector("span");

        if (buttonText) {
            buttonText.textContent = "Adding Book...";
        }

        // Replace the existing icon with a new Lucide icon.
        const buttonIcon = submitBtn.querySelector("[data-lucide], svg");

        if (buttonIcon) {
            const spinner = document.createElement("i");
            spinner.setAttribute("data-lucide", "loader-circle");
            spinner.classList.add("spin");

            buttonIcon.replaceWith(spinner);
            refreshIcons();
        }

        // Allow the browser to submit the multipart form normally.
    });
	
	function showFileError(element, message) {
	    element.textContent = message;
	    element.hidden = false;
	}

	function clearFileError(element) {
	    element.textContent = "";
	    element.hidden = true;
	}

    // ---------- Cleanup ----------
    window.addEventListener("beforeunload", function () {
        if (currentCoverUrl) {
            URL.revokeObjectURL(currentCoverUrl);
        }
    });

});