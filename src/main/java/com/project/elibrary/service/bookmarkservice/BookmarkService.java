package com.project.elibrary.service.bookmarkservice;

import java.util.List;

import com.project.elibrary.bean.bookmark.Bookmark;

public interface BookmarkService {

    boolean addBookmark(Bookmark bookmark);

    boolean removeBookmark(Long userId, Long bookId, int pageNumber);

    Bookmark getBookmark(Long userId, Long bookId, int pageNumber);

    List<Bookmark> getBookmarks(Long userId, Long bookId);
}