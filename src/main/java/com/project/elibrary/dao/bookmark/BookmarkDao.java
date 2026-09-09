package com.project.elibrary.dao.bookmark;

import java.util.List;

import com.project.elibrary.bean.bookmark.Bookmark;

public interface BookmarkDao {

    boolean saveBookmark(Bookmark bookmark);

    boolean deleteBookmark(Long userId, Long bookId, int pageNumber);

    Bookmark findBookmark(Long userId, Long bookId, int pageNumber);

    List<Bookmark> findBookmarks(Long userId, Long bookId);
}