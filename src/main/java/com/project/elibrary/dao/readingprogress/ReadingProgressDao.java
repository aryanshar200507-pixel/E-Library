package com.project.elibrary.dao.readingprogress;

import java.util.List;

import com.project.elibrary.bean.book.Book;
import com.project.elibrary.bean.readingprogress.ReadingProgress;

public interface ReadingProgressDao {

    boolean saveProgress(ReadingProgress progress);

    ReadingProgress findProgress(Long userId, Long bookId);

    boolean updateProgress(ReadingProgress progress);
    
    List<Book> findRecentlyRead(Long userId , int limit);
    
    List<Book> findReadingHistory(Long userId, int offset, int limit);

    int countReadingHistory(Long userId);
}