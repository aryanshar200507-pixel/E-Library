package com.project.elibrary.service.recommendationservice;
import java.util.List;

import com.project.elibrary.bean.book.Book;
public interface RecommendationService {
	

	    List<Book> getRecommendations(Long userId, int limit);

	}
