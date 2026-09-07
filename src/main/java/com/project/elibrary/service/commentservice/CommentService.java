package com.project.elibrary.service.commentservice;

import java.util.List;

import com.project.elibrary.bean.comment.Comment;

public interface CommentService {

    /*
     * Add a new comment.
     */
    boolean addComment(Comment comment);


    /*
     * Get one comment by its ID.
     */
    Comment getCommentById(Long commentId);


    /*
     * Get all comments for a particular book.
     */
    List<Comment> getCommentsByBookId(Long bookId);


    /*
     * Update an existing comment.
     */
    boolean updateComment(Comment comment);


    /*
     * Delete a comment.
     */
    boolean deleteComment(Long commentId);
}