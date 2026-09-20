package com.project.elibrary.service.bookrequestservice;

import com.project.elibrary.dao.bookrequest.BookRequestVoteDao;

public class BookRequestVoteService {

    private final BookRequestVoteDao voteDao;

    public BookRequestVoteService() {
        this.voteDao = new BookRequestVoteDao();
    }

    // Add vote
    public boolean addVote(long requestId, long userId) {

        if (requestId <= 0 || userId <= 0) {
            return false;
        }

        // User can vote only once
        if (voteDao.hasVoted(requestId, userId)) {
            return false;
        }

        return voteDao.addVote(requestId, userId);
    }

    // Check whether user has voted
    public boolean hasVoted(long requestId, long userId) {

        if (requestId <= 0 || userId <= 0) {
            return false;
        }

        return voteDao.hasVoted(requestId, userId);
    }

    // Get vote count
    public int getVoteCount(long requestId) {

        if (requestId <= 0) {
            return 0;
        }

        return voteDao.getVoteCount(requestId);
    }
}
