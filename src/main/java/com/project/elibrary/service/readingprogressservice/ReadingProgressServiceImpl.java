package com.project.elibrary.service.readingprogressservice;

import java.util.List;

import com.project.elibrary.bean.book.Book;
import com.project.elibrary.bean.readingprogress.ReadingProgress;
import com.project.elibrary.dao.readingprogress.ReadingProgressDao;
import com.project.elibrary.dao.readingprogress.ReadingProgressDaoImpl;

public class ReadingProgressServiceImpl implements ReadingProgressService {

    private ReadingProgressDao readingProgressDao;

    public ReadingProgressServiceImpl() {
        readingProgressDao = new ReadingProgressDaoImpl();
    }

    @Override
    public boolean saveProgress(ReadingProgress progress) {
        if (progress == null) {
            return false;
        }

        return readingProgressDao.saveProgress(progress);
    }

    @Override
    public ReadingProgress getProgress(Long userId, Long bookId) {
        if (userId == null || bookId == null) {
            return null;
        }

        return readingProgressDao.findProgress(userId, bookId);
    }

    @Override
    public boolean updateProgress(ReadingProgress progress) {
        if (progress == null) {
            return false;
        }

        return readingProgressDao.updateProgress(progress);
    }

    @Override
    public List<Book> getRecentlyRead(Long userId, int limit) {

        if (userId == null || limit <= 0) {
            return List.of();
        }

        return readingProgressDao.findRecentlyRead(userId, limit);
    }
}