package com.project.elibrary.service.categoryservice;

import com.project.elibrary.bean.category.Category;
import com.project.elibrary.dao.categorydao.CategoryDao;
import com.project.elibrary.dao.categorydao.CategoryDaoImpl;

public class CategoryServiceImpl implements CategoryService {
	
	private CategoryDao categoryDao = new CategoryDaoImpl();
	
	@Override
	public int countAllCategories() {
		
		return categoryDao.countAllCategories();
	}
	
	@Override
	public boolean addCategories(Category category) {
		
		return categoryDao.addCategories(category);
	}
}
