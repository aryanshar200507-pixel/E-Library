package com.project.elibrary.dao.comment;

import java.util.List;

import com.project.elibrary.bean.comment.Comment;

public interface CommentDao {
	boolean save(Comment comment);

	Comment findById(Long commentId);

	List<Comment> findByBookId(Long bookId);

	boolean update(Comment comment);

	boolean delete(Long commentId);

}
