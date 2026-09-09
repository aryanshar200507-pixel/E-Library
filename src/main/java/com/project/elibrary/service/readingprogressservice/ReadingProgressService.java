package com.project.elibrary.service.readingprogressservice;

import java.util.List;

import com.project.elibrary.bean.book.Book;
import com.project.elibrary.bean.readingprogress.ReadingProgress;

public interface ReadingProgressService {

    boolean saveProgress(ReadingProgress progress);

    ReadingProgress getProgress(Long userId, Long bookId);

    boolean updateProgress(ReadingProgress progress);
    
    List<Book> getRecentlyRead(Long userId, int limit);
}