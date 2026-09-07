package com.project.elibrary.service.commentservice;

import java.util.List;

import com.project.elibrary.bean.comment.Comment;
import com.project.elibrary.dao.comment.CommentDao;
import com.project.elibrary.dao.comment.CommentDaoImpl;

public class CommentServiceImpl implements CommentService {

	private CommentDao commentDao;

	/*
	 * Create the DAO object.
	 */
	public CommentServiceImpl() {

		commentDao = new CommentDaoImpl();
	}

	// =====================================================
	// ADD COMMENT
	// =====================================================

	@Override
	public boolean addComment(Comment comment) {

		validateComment(comment);

		return commentDao.save(comment);
	}

	// =====================================================
	// GET COMMENT BY ID
	// =====================================================

	@Override
	public Comment getCommentById(Long commentId) {

		validateId(commentId, "comment Id");

		return commentDao.findById(commentId);
	}

	// =====================================================
	// GET COMMENTS BY BOOK ID
	// =====================================================

	@Override
	public List<Comment> getCommentsByBookId(Long bookId) {

		validateId(bookId, "book Id");

		return commentDao.findByBookId(bookId);
	}

	// =====================================================
	// UPDATE COMMENT
	// =====================================================

	@Override
	public boolean updateComment(Comment comment) {

		validateComment(comment);

		/*
		 * An existing comment must have an ID before it can be updated.
		 */
		validateId(comment.getCommentId(), "comment Id");

		return commentDao.update(comment);
	}

	// =====================================================
	// DELETE COMMENT
	// =====================================================

	@Override
	public boolean deleteComment(Long commentId) {

		validateId(commentId, "comment Id");

		return commentDao.delete(commentId);
	}

	// =====================================================
	// VALIDATE COMMENT
	// =====================================================

	private void validateComment(Comment comment) {

		if (comment == null) {

			throw new IllegalArgumentException("Comment cannot be null.");
		}

		/*
		 * A comment must belong to a valid user.
		 */
		validateId(comment.getUserId(), "user Id");

		/*
		 * A comment must belong to a valid book.
		 */
		validateId(comment.getBookId(), "book Id");

		/*
		 * Comment text cannot be empty.
		 */
		if (comment.getComment() == null || comment.getComment().trim().isEmpty()) {

			throw new IllegalArgumentException("Comment cannot be empty.");
		}

		/*
		 * Remove unnecessary spaces from the beginning and end of the comment.
		 */
		comment.setComment(comment.getComment().trim());
	}

	// =====================================================
	// VALIDATE ID
	// =====================================================

	private void validateId(Long id, String fieldName) {

		if (id == null || id <= 0) {

			throw new IllegalArgumentException("Invalid " + fieldName + ".");
		}
	}
}