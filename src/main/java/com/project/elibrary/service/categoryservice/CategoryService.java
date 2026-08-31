package com.project.elibrary.service.categoryservice;

import com.project.elibrary.bean.category.Category;

public interface CategoryService {
	
	int countAllCategories();
	
	boolean addCategories(Category categories);
	
}
