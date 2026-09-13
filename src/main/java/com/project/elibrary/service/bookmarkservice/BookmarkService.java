package com.project.elibrary.service.bookmarkservice;

import java.util.List;

import com.project.elibrary.bean.book.Book;
import com.project.elibrary.bean.bookmark.Bookmark;

public interface BookmarkService {

    // PDF page bookmarks
    boolean addBookmark(Bookmark bookmark);

    boolean removeBookmark(Long userId, Long bookId, int pageNumber);

    Bookmark getBookmark(Long userId, Long bookId, int pageNumber);

    List<Bookmark> getBookmarks(Long userId, Long bookId);

    // Whole-book bookmarks
    boolean addBookBookmark(Long userId, Long bookId);

    boolean removeBookBookmark(Long userId, Long bookId);

    boolean isBookBookmarked(Long userId, Long bookId);

    // Bookmarked books page with pagination
    List<Book> getBookmarkedBooks(Long userId, int page, int pageSize);

    int getTotalBookmarkedBooks(Long userId);
}