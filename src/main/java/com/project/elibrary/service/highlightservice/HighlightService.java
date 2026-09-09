package com.project.elibrary.service.highlightservice;

import java.util.List;

import com.project.elibrary.bean.highlight.Highlight;

public interface HighlightService {

	// Add a new highlight.
	boolean addHighlight(Highlight highlight);

	// Delete one of the user's highlights.
	boolean removeHighlight(Long highlightId, Long userId);

	// Get all highlights for a book.
	List<Highlight> getHighlights(Long userId, Long bookId);

	boolean updateHighlightColor(Long highlightId, Long userId, String color);
}