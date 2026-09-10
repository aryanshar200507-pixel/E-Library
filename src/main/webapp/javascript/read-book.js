/* ==================== SERVER DATA ==================== */

const pdfUrl = window.pdfUrl || "";
const bookId = window.bookId || "";
const contextPath = window.contextPath || "";
let currentPage = Number(window.currentPage) || 1;
const isNormalUser = Boolean(window.isNormalUser);

pdfjsLib.GlobalWorkerOptions.workerSrc =
    "https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.11.174/pdf.worker.min.js";


/* ==================== HELPERS ==================== */

function $(id) {
    return document.getElementById(id);
}

function postData(url, data) {
    return fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: data.toString()
    });
}

function pageSelector(page) {
    return '.page-wrapper[data-page-number="' + page + '"]';
}


/* ==================== PDF STATE ==================== */

let pdfDoc = null;
let scale = 1.2;
let rotation = 0;
let viewMode = "single";
let rendering = false;
let pendingPage = null;
let saveTimeout = null;


/* ==================== SEARCH STATE ==================== */

let searchResults = [];
let currentSearchIndex = -1;


/* ==================== BOOKMARK STATE ==================== */

let currentPageBookmarked = false;


/* ==================== HIGHLIGHT STATE ==================== */

let loadedHighlights = [];
let selectedRange = null;
let selectedText = "";
let selectedHighlightId = null;
let highlightMode = true;


/* ==================== LOAD PDF ==================== */

async function loadPdf() {
    try {
        if (!pdfUrl) {
            $("loading").textContent = "PDF file is not available.";
            return;
        }

        pdfDoc = await pdfjsLib.getDocument(pdfUrl).promise;
        $("totalPages").textContent = pdfDoc.numPages;
        $("loading").classList.add("hidden");

        currentPage = Math.max(
            1,
            Math.min(currentPage, pdfDoc.numPages)
        );

        if (isNormalUser)
            await loadHighlights();

        await renderPage(currentPage);
        generateThumbnails();

        if (isNormalUser) {
            await loadBookmarkState();
            await loadBookmarks();
            renderHighlightList();
        }
    } catch (error) {
        console.error("PDF loading error:", error);
        $("loading").classList.remove("hidden");
        $("loading").textContent = "Unable to load the PDF.";
    }
}


/* ==================== PAGE RENDERING ==================== */

async function renderPage(pageNumber) {
    if (!pdfDoc) return;

    pageNumber = Math.max(
        1,
        Math.min(pageNumber, pdfDoc.numPages)
    );

    if (rendering) {
        pendingPage = pageNumber;
        return;
    }

    rendering = true;

    try {
        const container = $("pagesContainer");
        container.innerHTML = "";
        container.classList.toggle(
            "spread-mode",
            viewMode === "spread"
        );

        await renderSinglePage(pageNumber);

        if (
            viewMode === "spread" &&
            pageNumber + 1 <= pdfDoc.numPages
        ) {
            await renderSinglePage(pageNumber + 1);
        }

        currentPage = pageNumber;
        $("pageNumber").value = currentPage;
        updateThumbnailSelection();
        saveReadingProgress();

        if (isNormalUser)
            await loadBookmarkState();

    } catch (error) {
        console.error("Page rendering error:", error);
    } finally {
        rendering = false;

        if (pendingPage !== null) {
            const next = pendingPage;
            pendingPage = null;
            renderPage(next);
        }
    }
}


async function renderSinglePage(pageNumber) {
    const page = await pdfDoc.getPage(pageNumber);

    const viewport = page.getViewport({
        scale: scale,
        rotation: rotation
    });

    const wrapper = document.createElement("div");
    wrapper.className = "page-wrapper";
    wrapper.dataset.pageNumber = pageNumber;

    const canvas = document.createElement("canvas");
    canvas.className = "pdf-canvas";

    const context = canvas.getContext("2d");
    const dpr = window.devicePixelRatio || 1;

    canvas.width = viewport.width * dpr;
    canvas.height = viewport.height * dpr;
    canvas.style.width = viewport.width + "px";
    canvas.style.height = viewport.height + "px";

    context.setTransform(dpr, 0, 0, dpr, 0, 0);
    wrapper.appendChild(canvas);


    /* Highlight layer */

    const highlightLayer = document.createElement("div");
    highlightLayer.className = "highlight-layer";
    highlightLayer.dataset.pageNumber = pageNumber;
    wrapper.appendChild(highlightLayer);


    /* Text layer */

    const textLayer = document.createElement("div");
    textLayer.className = "textLayer";
    textLayer.style.width = viewport.width + "px";
    textLayer.style.height = viewport.height + "px";

    /* Required by PDF.js text layer */
    textLayer.style.setProperty(
        "--scale-factor",
        viewport.scale
    );

    wrapper.appendChild(textLayer);
    $("pagesContainer").appendChild(wrapper);


    /* Render PDF */

    await page.render({
        canvasContext: context,
        viewport: viewport
    }).promise;


    /* Render text */

    const textContent = await page.getTextContent();

    await pdfjsLib.renderTextLayer({
        textContent: textContent,
        container: textLayer,
        viewport: viewport,
        textDivs: []
    }).promise;


    if (isNormalUser) {
        setupTextSelection(textLayer, wrapper);
        restoreHighlightsForPage(pageNumber, wrapper);
    }
}


/* ==================== PAGE NAVIGATION ==================== */

function previousPage() {
    if (currentPage > 1)
        renderPage(currentPage - 1);
}


function nextPage() {
    if (!pdfDoc) return;

    if (viewMode === "spread") {
        if (currentPage + 2 <= pdfDoc.numPages)
            renderPage(currentPage + 2);
        else if (currentPage < pdfDoc.numPages)
            renderPage(currentPage + 1);
    } else if (currentPage < pdfDoc.numPages) {
        renderPage(currentPage + 1);
    }
}


function goToPageFromInput() {
    let page = parseInt($("pageNumber").value);

    if (isNaN(page)) return;

    page = Math.max(1, page);

    if (pdfDoc)
        page = Math.min(page, pdfDoc.numPages);

    renderPage(page);
}


/* ==================== ZOOM ==================== */

function zoomIn() {
    scale += 0.2;
    renderPage(currentPage);
}


function zoomOut() {
    if (scale <= 0.5) return;

    scale -= 0.2;
    renderPage(currentPage);
}


async function fitWidth() {
    if (!pdfDoc) return;

    const page = await pdfDoc.getPage(currentPage);
    const viewer = $("viewer");

    const viewport = page.getViewport({
        scale: 1,
        rotation: rotation
    });

    scale = (viewer.clientWidth - 50) / viewport.width;
    renderPage(currentPage);
}


/* ==================== ROTATION / VIEW ==================== */

function rotatePage() {
    rotation = (rotation + 90) % 360;
    renderPage(currentPage);
}


function changeViewMode() {
    viewMode = $("viewMode").value;
    renderPage(currentPage);
}


/* ==================== SIDEBAR ==================== */

function toggleSidebar() {
    $("sidebar").classList.toggle("active");
}


function showSidebarTab(tab) {
    const pages = $("pagesContainerSidebar");
    const bookmarks = $("bookmarkContainer");
    const highlights = $("highlightContainer");

    const pagesTab = $("pagesTab");
    const bookmarksTab = $("bookmarksTab");
    const highlightsTab = $("highlightsTab");

    pages.style.display = "none";
    bookmarks.style.display = "none";
    highlights.style.display = "none";

    pagesTab.classList.remove("active");

    if (bookmarksTab)
        bookmarksTab.classList.remove("active");

    if (highlightsTab)
        highlightsTab.classList.remove("active");


    if (tab === "pages") {
        pages.style.display = "block";
        pagesTab.classList.add("active");
    }

    if (tab === "bookmarks") {
        bookmarks.style.display = "block";

        if (bookmarksTab)
            bookmarksTab.classList.add("active");

        if (isNormalUser)
            loadBookmarks();
    }

    if (tab === "highlights") {
        highlights.style.display = "block";

        if (highlightsTab)
            highlightsTab.classList.add("active");

        if (isNormalUser)
            renderHighlightList();
    }
}


/* ==================== MORE OPTIONS MENU (mobile) ====================
   On desktop #toolbarOverflow renders inline via CSS
   (display:contents) so this menu never shows there — the
   button itself is hidden above 768px too. On mobile, this
   is the only way to reach bookmark, highlight, zoom, rotate,
   view mode, and fullscreen, so keep this working.
   ==================================================================== */

function toggleMoreMenu() {
    const menu = $("toolbarOverflow");

    if (!menu) return;

    menu.classList.toggle("open");
}


function closeMoreMenu() {
    const menu = $("toolbarOverflow");

    if (menu)
        menu.classList.remove("open");
}


(function setupMoreMenu() {
    const menu = $("toolbarOverflow");
    const trigger = $("moreOptionsButton");

    if (!menu) return;

    /* Close the menu right after any button inside it is used,
       so the dropdown doesn't stay open after e.g. Zoom In. */
    menu.addEventListener("click", function(event) {
        if (event.target.closest("button"))
            closeMoreMenu();
    });

    /* Close on outside tap/click */
    document.addEventListener("click", function(event) {
        if (!menu.classList.contains("open"))
            return;

        if (
            menu.contains(event.target) ||
            (trigger && trigger.contains(event.target))
        ) {
            return;
        }

        closeMoreMenu();
    });

    /* Close if the viewport is resized past the mobile
       breakpoint (e.g. device rotation) */
    window.addEventListener("resize", function() {
        if (window.innerWidth > 768)
            closeMoreMenu();
    });
})();


/* ==================== THUMBNAILS ==================== */

async function generateThumbnails() {
    if (!pdfDoc) return;

    const container = $("pagesContainerSidebar");
    container.innerHTML = "";

    for (
        let pageNumber = 1;
        pageNumber <= pdfDoc.numPages;
        pageNumber++
    ) {
        const page = await pdfDoc.getPage(pageNumber);

        const viewport = page.getViewport({
            scale: 0.18
        });

        const thumbnail = document.createElement("div");
        thumbnail.className = "thumbnail";
        thumbnail.dataset.page = pageNumber;

        const canvas = document.createElement("canvas");
        const context = canvas.getContext("2d");

        canvas.width = viewport.width;
        canvas.height = viewport.height;

        thumbnail.appendChild(canvas);

        const pageNumberText = document.createElement("div");
        pageNumberText.className = "thumbnail-page-number";
        pageNumberText.textContent = "Page " + pageNumber;

        thumbnail.appendChild(pageNumberText);

        thumbnail.addEventListener("click", function() {
            renderPage(pageNumber);
        });

        container.appendChild(thumbnail);

        await page.render({
            canvasContext: context,
            viewport: viewport
        }).promise;
    }

    updateThumbnailSelection();
}


function updateThumbnailSelection() {
    document.querySelectorAll(".thumbnail").forEach(function(item) {
        item.classList.toggle(
            "selected",
            Number(item.dataset.page) === Number(currentPage)
        );
    });
}


/* ==================== SEARCH ==================== */

function toggleSearch() {
    const panel = $("searchPanel");

    panel.classList.toggle("active");

    if (panel.classList.contains("active"))
        $("searchInput").focus();
}


function closeSearch() {
    $("searchPanel").classList.remove("active");
}


function handleSearchKey(event) {
    if (event.key === "Enter")
        performSearch();
}


async function performSearch() {
    if (!pdfDoc) return;

    const input = $("searchInput");
    const keyword = input.value.trim().toLowerCase();
    const info = $("searchInfo");

    if (!keyword) {
        info.textContent = "Enter a word to search.";
        return;
    }

    searchResults = [];
    currentSearchIndex = -1;
    info.textContent = "Searching...";

    for (
        let pageNumber = 1;
        pageNumber <= pdfDoc.numPages;
        pageNumber++
    ) {
        try {
            const page = await pdfDoc.getPage(pageNumber);
            const textContent = await page.getTextContent();

            let pageText = "";

            textContent.items.forEach(function(item) {
                pageText += item.str + " ";
            });

            if (pageText.toLowerCase().includes(keyword))
                searchResults.push(pageNumber);

        } catch (error) {
            console.error(
                "Search error on page:",
                pageNumber,
                error
            );
        }
    }

    if (searchResults.length === 0) {
        info.textContent = "No results found.";
        return;
    }

    currentSearchIndex = 0;

    info.textContent =
        searchResults.length + " result(s) found.";

    renderPage(searchResults[currentSearchIndex]);
}


function nextSearchResult() {
    if (searchResults.length === 0) return;

    currentSearchIndex++;

    if (currentSearchIndex >= searchResults.length)
        currentSearchIndex = 0;

    renderPage(searchResults[currentSearchIndex]);
}


function previousSearchResult() {
    if (searchResults.length === 0) return;

    currentSearchIndex--;

    if (currentSearchIndex < 0)
        currentSearchIndex = searchResults.length - 1;

    renderPage(searchResults[currentSearchIndex]);
}


/* ==================== FULLSCREEN ==================== */

function toggleFullscreen() {
    if (!document.fullscreenElement) {
        document.documentElement
            .requestFullscreen()
            .catch(function(error) {
                console.error("Fullscreen error:", error);
            });
    } else {
        document.exitFullscreen();
    }
}


/* ==================== READING PROGRESS ==================== */

function saveReadingProgress() {
    clearTimeout(saveTimeout);

    saveTimeout = setTimeout(function() {
        const data = new URLSearchParams();

        data.append("bookId", bookId);
        data.append("currentPage", currentPage);

        postData(
            contextPath + "/books/save-progress",
            data
        ).catch(function(error) {
            console.error("Progress save error:", error);
        });
    }, 500);
}


function saveProgressBeforeLeaving() {
    const data = new URLSearchParams();

    data.append("bookId", bookId);
    data.append("currentPage", currentPage);

    try {
        navigator.sendBeacon(
            contextPath + "/books/save-progress",
            new Blob(
                [data.toString()],
                {
                    type:
                        "application/x-www-form-urlencoded"
                }
            )
        );
    } catch (error) {
        console.error(
            "Unable to save progress:",
            error
        );
    }
}


/* ==================== BOOKMARKS ==================== */

async function toggleBookmark() {
    if (!isNormalUser) return;

    try {
        const data = new URLSearchParams();

        data.append("bookId", bookId);
        data.append("pageNumber", currentPage);

        const response = await postData(
            contextPath + "/books/bookmark",
            data
        );

        if (!response.ok)
            throw new Error("Bookmark request failed.");

        const result = await response.text();

        if (result === "added")
            currentPageBookmarked = true;
        else if (result === "removed")
            currentPageBookmarked = false;

        updateBookmarkButton();
        loadBookmarks();

    } catch (error) {
        console.error("Bookmark error:", error);
    }
}


async function loadBookmarkState() {
    if (!isNormalUser) return;

    const button = $("bookmarkButton");

    if (!button) return;

    try {
        const response = await fetch(
            contextPath +
            "/books/bookmark?bookId=" +
            encodeURIComponent(bookId) +
            "&pageNumber=" +
            encodeURIComponent(currentPage)
        );

        if (!response.ok) return;

        currentPageBookmarked = (await response.text()) === "true";

        updateBookmarkButton();

    } catch (error) {
        console.error(
            "Bookmark state error:",
            error
        );
    }
}


function updateBookmarkButton() {
    const button = $("bookmarkButton");

    if (!button) return;

    button.classList.toggle(
        "bookmarked",
        currentPageBookmarked
    );

    button.title = currentPageBookmarked
        ? "Remove bookmark"
        : "Bookmark this page";
}


async function loadBookmarks() {
    if (!isNormalUser) return;

    const container = $("bookmarkContainer");

    if (!container) return;

    try {
        const response = await fetch(
            contextPath +
            "/books/bookmark?action=list&bookId=" +
            encodeURIComponent(bookId)
        );

        if (!response.ok) {
            container.innerHTML =
                '<div class="empty-sidebar-message">' +
                "Unable to load bookmarks." +
                "</div>";
            return;
        }

        const bookmarks = await response.json();

        container.innerHTML = "";

        if (!bookmarks || bookmarks.length === 0) {
            container.innerHTML =
                '<div class="empty-sidebar-message">' +
                "No bookmarks yet." +
                "</div>";
            return;
        }

        bookmarks.forEach(function(bookmark) {
            const item = document.createElement("div");
            item.className = "bookmark-list-item";

            const text = document.createElement("div");
            text.className = "bookmark-page-text";
            text.textContent =
                "Page " + bookmark.pageNumber;

            text.addEventListener("click", function() {
                renderPage(bookmark.pageNumber);
            });

            const remove = document.createElement("button");
            remove.className = "bookmark-remove";
            remove.textContent = "×";
            remove.title = "Remove bookmark";

            remove.addEventListener("click", function(event) {
                event.stopPropagation();
                removeBookmarkFromList(
                    bookmark.pageNumber
                );
            });

            item.appendChild(text);
            item.appendChild(remove);
            container.appendChild(item);
        });

    } catch (error) {
        console.error(
            "Error loading bookmarks:",
            error
        );
    }
}


async function removeBookmarkFromList(pageNumber) {
    if (currentPage !== pageNumber)
        await renderPage(pageNumber);

    await toggleBookmark();
    loadBookmarks();
}


/* ==================== HIGHLIGHT MODE ==================== */

function activateHighlightMode() {
    if (!isNormalUser) return;

    highlightMode = true;

    const button = $("highlightHeaderButton");

    if (button) {
        button.classList.add("active");
        button.title = "Select text to highlight";
    }
}


/* ==================== TEXT SELECTION ==================== */

function setupTextSelection(textLayer, pageWrapper) {
    textLayer.addEventListener("mouseup", function() {
        setTimeout(function() {
            handleTextSelection(pageWrapper);
        }, 20);
    });

    textLayer.addEventListener("touchend", function() {
        setTimeout(function() {
            handleTextSelection(pageWrapper);
        }, 100);
    });
}


function handleTextSelection(pageWrapper) {
    if (!highlightMode) return;

    const selection = window.getSelection();

    if (!selection || selection.rangeCount === 0)
        return;

    const text = selection.toString().trim();

    if (!text) return;

    const range = selection.getRangeAt(0);

    let node = range.commonAncestorContainer;

    if (node.nodeType === 3)
        node = node.parentElement;

    if (!node) return;

    const selectedPage =
        node.closest(".page-wrapper");

    if (selectedPage !== pageWrapper)
        return;

    selectedRange = range.cloneRange();
    selectedText = text;

    showHighlightToolbar(range);
}


/* ==================== HIGHLIGHT TOOLBAR ==================== */

function showHighlightToolbar(range) {
    const toolbar = $("highlightToolbar");

    if (!toolbar) return;

    const rect = range.getBoundingClientRect();

    let left =
        rect.left +
        rect.width / 2 -
        85;

    let top = rect.top - 48;

    if (left < 5)
        left = 5;

    if (left + 190 > window.innerWidth)
        left = window.innerWidth - 195;

    if (top < 5)
        top = rect.bottom + 8;

    toolbar.style.left = left + "px";
    toolbar.style.top = top + "px";
    toolbar.style.display = "flex";
}


function hideHighlightToolbar() {
    const toolbar = $("highlightToolbar");

    if (toolbar)
        toolbar.style.display = "none";
}


/* ==================== HIGHLIGHT RECTANGLES ==================== */

function getSelectionRectangles(range, pageWrapper) {
    const pageRect =
        pageWrapper.getBoundingClientRect();

    return Array.from(range.getClientRects())
        .filter(function(rect) {
            return rect.width > 1 && rect.height > 1;
        })
        .map(function(rect) {
            return {
                left:
                    (rect.left - pageRect.left) /
                    pageWrapper.clientWidth,

                top:
                    (rect.top - pageRect.top) /
                    pageWrapper.clientHeight,

                width:
                    rect.width /
                    pageWrapper.clientWidth,

                height:
                    rect.height /
                    pageWrapper.clientHeight
            };
        });
}


/* ==================== APPLY HIGHLIGHT ==================== */

async function applyHighlightColor(color) {
    if (selectedRange && selectedText) {
        await createHighlight(color);
        return;
    }

    if (selectedHighlightId !== null)
        await changeHighlightColor(
            selectedHighlightId,
            color
        );
}


async function createHighlight(color) {
    if (!selectedRange || !selectedText)
        return;

    let node =
        selectedRange.commonAncestorContainer;

    if (node.nodeType === 3)
        node = node.parentElement;

    if (!node) return;

    const pageWrapper =
        node.closest(".page-wrapper");

    if (!pageWrapper) return;

    const pageNumber =
        Number(pageWrapper.dataset.pageNumber);

    const rectangles =
        getSelectionRectangles(
            selectedRange,
            pageWrapper
        );

    if (rectangles.length === 0)
        return;

    const highlight = {
        bookId: Number(bookId),
        pageNumber: pageNumber,
        selectedText: selectedText,
        startOffset: selectedRange.startOffset,
        endOffset: selectedRange.endOffset,
        rectangles: rectangles,
        color: color
    };

    const saved = await saveHighlight(highlight);

    if (!saved) return;

    const selection = window.getSelection();

    if (selection)
        selection.removeAllRanges();

    selectedRange = null;
    selectedText = "";

    hideHighlightToolbar();



    await loadHighlights();

    const newPageWrapper =
        document.querySelector(
            pageSelector(pageNumber)
        );

    if (newPageWrapper) {
        restoreHighlightsForPage(
            pageNumber,
            newPageWrapper
        );
    }

    renderHighlightList();
}


/* ==================== SAVE HIGHLIGHT ==================== */

async function saveHighlight(highlight) {
    try {
        const data = new URLSearchParams();

        data.append("bookId", highlight.bookId);
        data.append("pageNumber", highlight.pageNumber);
        data.append("selectedText", highlight.selectedText);
        data.append("startOffset", highlight.startOffset);
        data.append("endOffset", highlight.endOffset);
        data.append(
            "rectanglesJson",
            JSON.stringify(highlight.rectangles)
        );
        data.append("color", highlight.color);

        const response = await postData(
            contextPath + "/books/highlight",
            data
        );

        if (!response.ok) {
            console.error("Failed to save highlight.");
            return false;
        }

        return (await response.text()) === "saved";

    } catch (error) {
        console.error(
            "Error saving highlight:",
            error
        );
        return false;
    }
}


/* ==================== LOAD HIGHLIGHTS ==================== */

async function loadHighlights() {
    if (!isNormalUser) return;

    try {
        const response = await fetch(
            contextPath +
            "/books/highlight?action=list&bookId=" +
            encodeURIComponent(bookId)
        );

        if (!response.ok) {
            console.error(
                "Failed to load highlights."
            );
            return;
        }

        loadedHighlights = await response.json();
        renderHighlightList();

    } catch (error) {
        console.error(
            "Error loading highlights:",
            error
        );
    }
}


/* ==================== RESTORE HIGHLIGHTS ==================== */

function restoreHighlightsForPage(
    pageNumber,
    pageWrapper
) {
    const highlightLayer =
        pageWrapper.querySelector(
            ".highlight-layer"
        );

    if (!highlightLayer) return;

    highlightLayer.innerHTML = "";

    const pageHighlights =
        loadedHighlights.filter(function(highlight) {
            return Number(highlight.pageNumber) ===
                Number(pageNumber);
        });

    pageHighlights.forEach(function(highlight) {
        let rectangles =
            highlight.rectanglesJson;

        if (typeof rectangles === "string") {
            try {
                rectangles = JSON.parse(rectangles);
            } catch (error) {
                console.error(
                    "Invalid highlight rectangle JSON:",
                    error
                );
                return;
            }
        }

        if (!Array.isArray(rectangles))
            return;

        rectangles.forEach(function(rectangle) {
            drawSavedHighlight(
                pageWrapper,
                highlight,
                rectangle
            );
        });
    });
}


function drawSavedHighlight(
    pageWrapper,
    highlight,
    rectangle
) {
    const highlightLayer =
        pageWrapper.querySelector(
            ".highlight-layer"
        );

    if (!highlightLayer) return;

    const element =
        document.createElement("div");

    element.className =
        "pdf-highlight highlight-" +
        highlight.color;

    element.dataset.highlightId =
        highlight.highlightId;

    element.style.left =
        rectangle.left *
        pageWrapper.clientWidth +
        "px";

    element.style.top =
        rectangle.top *
        pageWrapper.clientHeight +
        "px";

    element.style.width =
        rectangle.width *
        pageWrapper.clientWidth +
        "px";

    element.style.height =
        rectangle.height *
        pageWrapper.clientHeight +
        "px";

    highlightLayer.appendChild(element);
}


/* ==================== SELECT SAVED HIGHLIGHT ==================== */

document.addEventListener("click", function(event) {
    if (!isNormalUser) return;

    if (
        event.target.closest(".highlight-toolbar") ||
        event.target.closest(".sidebar")
    ) {
        return;
    }

    const pageWrapper =
        event.target.closest(".page-wrapper");

    if (!pageWrapper) return;

    const pageRect =
        pageWrapper.getBoundingClientRect();

    const normalizedX =
        (event.clientX - pageRect.left) /
        pageWrapper.clientWidth;

    const normalizedY =
        (event.clientY - pageRect.top) /
        pageWrapper.clientHeight;

    const pageNumber =
        Number(pageWrapper.dataset.pageNumber);

    const pageHighlights =
        loadedHighlights.filter(function(highlight) {
            return Number(highlight.pageNumber) ===
                pageNumber;
        });

    for (let i = 0;i < pageHighlights.length;i++) {
        const highlight = pageHighlights[i];

        let rectangles =
            highlight.rectanglesJson;

        if (typeof rectangles === "string") {
            try {
                rectangles = JSON.parse(rectangles);
            } catch (error) {
                continue;
            }
        }

        if (!Array.isArray(rectangles))
            continue;

        for (let j = 0;j < rectangles.length;j++) {
            const rectangle = rectangles[j];

            const inside =
                normalizedX >= rectangle.left &&
                normalizedX <=
                rectangle.left +
                rectangle.width &&
                normalizedY >= rectangle.top &&
                normalizedY <=
                rectangle.top +
                rectangle.height;

            if (inside) {
                selectExistingHighlight(
                    highlight.highlightId
                );
                return;
            }
        }
    }
});


function selectExistingHighlight(highlightId) {
    selectedHighlightId = highlightId;

    document
        .querySelectorAll(".selected-highlight")
        .forEach(function(element) {
            element.classList.remove(
                "selected-highlight"
            );
        });

    document
        .querySelectorAll(
            '[data-highlight-id="' +
            highlightId +
            '"]'
        )
        .forEach(function(element) {
            element.classList.add(
                "selected-highlight"
            );
        });

    const element =
        document.querySelector(
            '[data-highlight-id="' +
            highlightId +
            '"]'
        );

    if (!element) return;

    const rect = element.getBoundingClientRect();
    const toolbar = $("highlightToolbar");

    if (!toolbar) return;

    let left = rect.left;
    let top = rect.bottom + 8;

    if (left + 190 > window.innerWidth)
        left = window.innerWidth - 195;

    if (top + 50 > window.innerHeight)
        top = rect.top - 50;

    left = Math.max(5, left);
    top = Math.max(5, top);

    toolbar.style.left = left + "px";
    toolbar.style.top = top + "px";
    toolbar.style.display = "flex";
}


/* ==================== CHANGE HIGHLIGHT COLOR ==================== */

async function changeHighlightColor(
    highlightId,
    color
) {
    try {
        const data = new URLSearchParams();

        data.append("action", "updatecolor");
        data.append("highlightId", highlightId);
        data.append("color", color);

        const response = await postData(
            contextPath + "/books/highlight",
            data
        );

        if (!response.ok) {
            console.error(
                "Failed to change highlight color."
            );
            return;
        }

        const result = await response.text();

        if (result !== "updated") {
            console.error(
                "Highlight color was not updated."
            );
            return;
        }

        await loadHighlights();

        const pageWrapper =
            document.querySelector(
                pageSelector(currentPage)
            );

        if (pageWrapper) {
            restoreHighlightsForPage(
                currentPage,
                pageWrapper
            );
        }

        selectedHighlightId = null;
        hideHighlightToolbar();
        renderHighlightList();

    } catch (error) {
        console.error(
            "Error changing highlight color:",
            error
        );
    }
}


/* ==================== DELETE HIGHLIGHT ==================== */

async function removeSelectedHighlight() {
    if (selectedHighlightId === null)
        return;

    try {
        const url =
            contextPath +
            "/books/highlight?highlightId=" +
            encodeURIComponent(
                selectedHighlightId
            );

        const response = await fetch(url, {
            method: "DELETE"
        });

        if (!response.ok) {
            console.error(
                "Failed to remove highlight."
            );
            return;
        }

        const result = await response.text();

        if (result !== "deleted") {
            console.error(
                "Highlight was not deleted."
            );
            return;
        }

        selectedHighlightId = null;
        hideHighlightToolbar();

        await loadHighlights();

        const pageWrapper =
            document.querySelector(
                pageSelector(currentPage)
            );

        if (pageWrapper) {
            restoreHighlightsForPage(
                currentPage,
                pageWrapper
            );
        }

        renderHighlightList();

    } catch (error) {
        console.error(
            "Remove highlight failed:",
            error
        );
    }
}


/* ==================== HIGHLIGHT SIDEBAR ==================== */

function renderHighlightList() {
    const container = $("highlightContainer");

    if (!container) return;

    container.innerHTML = "";

    if (
        !loadedHighlights ||
        loadedHighlights.length === 0
    ) {
        container.innerHTML =
            '<div class="empty-sidebar-message">' +
            "No highlights yet." +
            "</div>";
        return;
    }

    loadedHighlights.forEach(function(highlight) {
        const item =
            document.createElement("div");

        item.className =
            "highlight-list-item";

        item.style.borderLeftColor =
            getHighlightColor(highlight.color);


        const page =
            document.createElement("div");

        page.className = "highlight-page";
        page.textContent =
            "Page " + highlight.pageNumber;


        const text =
            document.createElement("div");

        text.className = "highlight-text";
        text.textContent =
            highlight.selectedText;

        item.appendChild(page);
        item.appendChild(text);


        const actions =
            document.createElement("div");

        actions.className =
            "highlight-list-actions";


        ["yellow", "green", "blue", "pink"]
            .forEach(function(color) {
                actions.appendChild(
                    createHighlightListColorButton(
                        color,
                        highlight.highlightId
                    )
                );
            });


        const remove =
            document.createElement("button");

        remove.className =
            "highlight-list-delete";

        remove.textContent = "🗑";
        remove.title = "Delete highlight";

        remove.addEventListener(
            "click",
            function(event) {
                event.stopPropagation();

                selectedHighlightId =
                    highlight.highlightId;

                removeSelectedHighlight();
            }
        );

        actions.appendChild(remove);
        item.appendChild(actions);


        item.addEventListener(
            "click",
            function() {
                renderPage(
                    highlight.pageNumber
                );
            }
        );

        container.appendChild(item);
    });
}


function createHighlightListColorButton(
    color,
    highlightId
) {
    const button =
        document.createElement("button");

    button.className =
        "highlight-list-color";

    button.style.background =
        getHighlightColor(color);

    button.title = "Change to " + color;

    button.addEventListener(
        "click",
        function(event) {
            event.stopPropagation();

            changeHighlightColor(
                highlightId,
                color
            );
        }
    );

    return button;
}


function getHighlightColor(color) {
    if (color === "green")
        return "#70e000";

    if (color === "blue")
        return "#74c0fc";

    if (color === "pink")
        return "#ff8fab";

    return "#ffe066";
}


/* ==================== BACK ==================== */

function goBack() {
    saveProgressBeforeLeaving();
    window.history.back();
}


/* ==================== KEYBOARD CONTROLS ==================== */

document.addEventListener(
    "keydown",
    function(event) {
        if (
            event.target.tagName === "INPUT" ||
            event.target.tagName === "TEXTAREA" ||
            event.target.tagName === "SELECT"
        ) {
            return;
        }

        if (event.key === "ArrowLeft")
            previousPage();

        if (event.key === "ArrowRight")
            nextPage();

        if (
            event.key === "+" ||
            event.key === "="
        ) {
            zoomIn();
        }

        if (event.key === "-")
            zoomOut();

        if (event.key === "Escape") {
            hideHighlightToolbar();
            closeMoreMenu();
            selectedRange = null;
            selectedText = "";
            selectedHighlightId = null;
        }
    }
);


/* ==================== TOUCH / SWIPE (drag gestures) ==================== */

let touchStartX = 0;
let touchStartY = 0;
let touchEndX = 0;
let touchEndY = 0;
let wasSwipe = false;

const swipeThreshold = 50;

const readerContent = $("readerContent");

if (readerContent) {
    readerContent.addEventListener(
        "touchstart",
        function(event) {
            touchStartX =
                event.changedTouches[0].screenX;

            touchStartY =
                event.changedTouches[0].screenY;

            wasSwipe = false;
        },
        { passive: true }
    );

    readerContent.addEventListener(
        "touchend",
        function(event) {
            touchEndX =
                event.changedTouches[0].screenX;

            touchEndY =
                event.changedTouches[0].screenY;

            handleSwipe();
        },
        { passive: true }
    );
}


function handleSwipe() {
    const horizontalDifference =
        touchEndX - touchStartX;

    const verticalDifference =
        touchEndY - touchStartY;

    if (
        Math.abs(verticalDifference) >
        Math.abs(horizontalDifference)
    ) {
        return;
    }

    if (
        Math.abs(horizontalDifference) <
        swipeThreshold
    ) {
        return;
    }

    wasSwipe = true;

    if (horizontalDifference < 0)
        nextPage();
    else
        previousPage();
}


/* ==================== TAP / CLICK EDGE NAVIGATION ====================
   Works for both mouse clicks (desktop browser) and touch taps
   (mobile) via a single handler on the viewer. A tap/click in the
   left ~18% of the viewer goes to the previous page, right ~18%
   goes to the next page. Middle area, buttons, sidebar, search
   panel, and highlight toolbar are all excluded, and an active
   text selection (from highlighting) is never treated as a nav tap.
   ==================================================================== */

let pointerDownX = 0;
let pointerDownY = 0;

const viewerElement = $("viewer");

function isNavExcludedTarget(target) {
    return Boolean(
        target.closest(".highlight-toolbar") ||
        target.closest(".sidebar") ||
        target.closest(".search-panel") ||
        target.closest(".toolbar") ||
        target.closest("button") ||
        target.closest("input") ||
        target.closest("select") ||
        target.closest(".thumbnail")
    );
}

if (viewerElement) {

    viewerElement.addEventListener(
        "mousedown",
        function(event) {
            pointerDownX = event.clientX;
            pointerDownY = event.clientY;
        }
    );

    viewerElement.addEventListener(
        "click",
        function(event) {

            if (wasSwipe) {
                wasSwipe = false;
                return;
            }

            if (isNavExcludedTarget(event.target))
                return;

            const selection = window.getSelection();

            if (
                selection &&
                selection.toString().trim().length > 0
            ) {
                return;
            }

            const movedX =
                Math.abs(event.clientX - pointerDownX);

            const movedY =
                Math.abs(event.clientY - pointerDownY);

            /* Ignore drags (text selection, scroll-drag) */
            if (movedX > 10 || movedY > 10)
                return;

            const viewerRect =
                viewerElement.getBoundingClientRect();

            const clickX =
                event.clientX - viewerRect.left;

            const zoneWidth =
                Math.min(
                    110,
                    viewerRect.width * 0.18
                );

            if (clickX <= zoneWidth) {
                previousPage();
            } else if (
                clickX >= viewerRect.width - zoneWidth
            ) {
                nextPage();
            }
        }
    );
}


/* ==================== DOUBLE TAP ==================== */

let lastTap = 0;

const viewer = $("viewer");

if (viewer) {
    viewer.addEventListener(
        "touchend",
        function(event) {
            const currentTime =
                new Date().getTime();

            const tapLength =
                currentTime - lastTap;

            if (
                tapLength < 300 &&
                tapLength > 0
            ) {
                event.preventDefault();
            }

            lastTap = currentTime;
        }
    );
}


/* ==================== RESIZE ==================== */

window.addEventListener(
    "resize",
    function() {
        if (pdfDoc)
            renderPage(currentPage);
    }
);


/* ==================== BEFORE LEAVING ==================== */

window.addEventListener(
    "beforeunload",
    function() {
        saveProgressBeforeLeaving();
    }
);


/* ==================== START READER ==================== */

if (isNormalUser) {
    activateHighlightMode();
}

loadPdf();