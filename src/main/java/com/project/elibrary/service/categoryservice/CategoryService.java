package com.project.elibrary.service.categoryservice;

import java.util.List;

import com.project.elibrary.bean.category.Category;

public interface CategoryService {
	
	int countAllCategories();
	
	boolean addCategories(Category categories);
	
	List<Category> getAllCategories();
	
	List<Category> getCategories(int page, int pageSize);

	int getTotalCategories();
	
	List<Category> getTopRatedCategories(int limit);
}
