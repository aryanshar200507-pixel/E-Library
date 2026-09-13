package com.project.elibrary.dao.bookmark;

import java.util.List;

import com.project.elibrary.bean.book.Book;
import com.project.elibrary.bean.bookmark.Bookmark;

public interface BookmarkDao {

	boolean saveBookmark(Bookmark bookmark);

	boolean deleteBookmark(Long userId, Long bookId, int pageNumber);
	
	boolean saveBookBookmark(Long userId, Long bookId);

	boolean deleteBookBookmark(Long userId, Long bookId);

	boolean isBookBookmarked(Long userId, Long bookId);

	Bookmark findBookmark(Long userId, Long bookId, int pageNumber);

	List<Bookmark> findBookmarks(Long userId, Long bookId);
	
	List<Book> findBookmarkedBooks(Long userId, int offset, int limit);

	int countBookmarkedBooks(Long userId);
}