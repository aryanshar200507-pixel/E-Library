package com.project.elibrary.dao.categorydao;

import java.util.List;

import com.project.elibrary.bean.category.Category;

public interface CategoryDao {

	int countAllCategories();
	
	boolean addCategories(Category categories);
	
	List<Category> findAllCategories();

	List<Category> findCategories(int offset, int limit);
	
	List<Category> findTopRatedCategories(int limit);
}
