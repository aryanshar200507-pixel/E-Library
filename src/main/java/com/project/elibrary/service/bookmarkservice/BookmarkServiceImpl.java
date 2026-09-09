package com.project.elibrary.service.bookmarkservice;

import java.util.List;

import com.project.elibrary.bean.bookmark.Bookmark;
import com.project.elibrary.dao.bookmark.BookmarkDao;
import com.project.elibrary.dao.bookmark.BookmarkDaoImpl;

public class BookmarkServiceImpl implements BookmarkService {

    private BookmarkDao bookmarkDao;

    public BookmarkServiceImpl() {
        bookmarkDao = new BookmarkDaoImpl();
    }

    @Override
    public boolean addBookmark(Bookmark bookmark) {

        if (bookmark == null) {
            return false;
        }

        if (bookmark.getUserId() == null ||
            bookmark.getBookId() == null ||
            bookmark.getPageNumber() < 1) {

            return false;
        }

        // Prevent duplicate bookmark
        Bookmark existingBookmark = bookmarkDao.findBookmark(
                bookmark.getUserId(),
                bookmark.getBookId(),
                bookmark.getPageNumber()
        );

        if (existingBookmark != null) {
            return false;
        }

        return bookmarkDao.saveBookmark(bookmark);
    }

    @Override
    public boolean removeBookmark(
            Long userId,
            Long bookId,
            int pageNumber) {

        if (userId == null ||
            bookId == null ||
            pageNumber < 1) {

            return false;
        }

        return bookmarkDao.deleteBookmark(
                userId,
                bookId,
                pageNumber
        );
    }

    @Override
    public Bookmark getBookmark(
            Long userId,
            Long bookId,
            int pageNumber) {

        if (userId == null ||
            bookId == null ||
            pageNumber < 1) {

            return null;
        }

        return bookmarkDao.findBookmark(
                userId,
                bookId,
                pageNumber
        );
    }

    @Override
    public List<Bookmark> getBookmarks(
            Long userId,
            Long bookId) {

        if (userId == null || bookId == null) {
            return List.of();
        }

        return bookmarkDao.findBookmarks(userId, bookId);
    }
}