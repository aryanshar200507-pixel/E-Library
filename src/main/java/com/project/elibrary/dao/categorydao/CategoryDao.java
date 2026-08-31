package com.project.elibrary.dao.categorydao;

import com.project.elibrary.bean.category.Category;

public interface CategoryDao {

	int countAllCategories();
	
	boolean addCategories(Category categories);
	
	


}
