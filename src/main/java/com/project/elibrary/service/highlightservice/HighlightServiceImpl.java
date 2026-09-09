package com.project.elibrary.service.highlightservice;

import java.util.List;

import com.project.elibrary.bean.highlight.Highlight;
import com.project.elibrary.dao.highlight.HighlightDao;
import com.project.elibrary.dao.highlight.HighlightDaoImpl;

public class HighlightServiceImpl implements HighlightService {

	private HighlightDao highlightDao;

	public HighlightServiceImpl() {

		highlightDao = new HighlightDaoImpl();
	}

	@Override
	public boolean addHighlight(Highlight highlight) {

		if (highlight == null) {
			return false;
		}

		if (highlight.getUserId() == null || highlight.getBookId() == null) {

			return false;
		}

		if (highlight.getPageNumber() < 1) {
			return false;
		}

		if (highlight.getSelectedText() == null || highlight.getSelectedText().isBlank()) {

			return false;
		}

		if (highlight.getStartOffset() < 0 || highlight.getEndOffset() <= highlight.getStartOffset()) {

			return false;
		}

		if (highlight.getRectanglesJson() == null || highlight.getRectanglesJson().isBlank()) {

			return false;
		}

		if (highlight.getColor() == null || highlight.getColor().isBlank()) {

			highlight.setColor("yellow");
		}

		return highlightDao.saveHighlight(highlight);
	}

	@Override
	public boolean removeHighlight(Long highlightId, Long userId) {

		if (highlightId == null || userId == null) {

			return false;
		}

		return highlightDao.deleteHighlight(highlightId, userId);
	}

	@Override
	public List<Highlight> getHighlights(Long userId, Long bookId) {

		if (userId == null || bookId == null) {

			return List.of();
		}

		return highlightDao.findHighlights(userId, bookId);
	}

	@Override
	public boolean updateHighlightColor(Long highlightId, Long userId, String color) {

		if (highlightId == null || userId == null) {

			return false;
		}

		if (color == null || color.isBlank()) {

			return false;
		}

		/*
		 * Only allow the colors that our reader supports.
		 *
		 * This prevents random/invalid values from being stored in the database.
		 */
		if (!color.equalsIgnoreCase("yellow") && !color.equalsIgnoreCase("green") && !color.equalsIgnoreCase("blue")
				&& !color.equalsIgnoreCase("pink")) {

			return false;
		}

		return highlightDao.updateHighlightColor(highlightId, userId, color.toLowerCase());
	}
}