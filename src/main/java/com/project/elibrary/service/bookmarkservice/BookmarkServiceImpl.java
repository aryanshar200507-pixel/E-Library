package com.project.elibrary.service.bookmarkservice;

import java.util.List;

import com.project.elibrary.bean.book.Book;
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
    
    @Override
    public boolean addBookBookmark(Long userId, Long bookId) {

        if (userId == null || bookId == null ||
            userId <= 0 || bookId <= 0) {

            return false;
        }

        // Prevent duplicate book bookmark
        if (bookmarkDao.isBookBookmarked(userId, bookId)) {
            return false;
        }

        return bookmarkDao.saveBookBookmark(userId, bookId);
    }

    @Override
    public boolean removeBookBookmark(Long userId, Long bookId) {

        if (userId == null || bookId == null ||
            userId <= 0 || bookId <= 0) {

            return false;
        }

        return bookmarkDao.deleteBookBookmark(userId, bookId);
    }

    @Override
    public boolean isBookBookmarked(Long userId, Long bookId) {

        if (userId == null || bookId == null ||
            userId <= 0 || bookId <= 0) {

            return false;
        }

        return bookmarkDao.isBookBookmarked(userId, bookId);
    }
    
    @Override
    public List<Book> getBookmarkedBooks(Long userId, int page, int pageSize) {

        if (userId == null || userId <= 0) {
            return List.of();
        }

        if (page < 1) {
            page = 1;
        }

        if (pageSize <= 0) {
            pageSize = 10;
        }

        int offset = (page - 1) * pageSize;

        return bookmarkDao.findBookmarkedBooks(userId, offset, pageSize);
    }

    @Override
    public int getTotalBookmarkedBooks(Long userId) {

        if (userId == null || userId <= 0) {
            return 0;
        }

        return bookmarkDao.countBookmarkedBooks(userId);
    }
}