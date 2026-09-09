package com.project.elibrary.dao.highlight;

import java.util.List;

import com.project.elibrary.bean.highlight.Highlight;

public interface HighlightDao {

	// Save a new highlight.
	boolean saveHighlight(Highlight highlight);

	// Delete a highlight belonging to a user.
	boolean deleteHighlight(Long highlightId, Long userId);

	// Get all highlights of a particular book
	// belonging to a particular user.
	List<Highlight> findHighlights(Long userId, Long bookId);

	boolean updateHighlightColor(Long highlightId, Long userId, String color);
}