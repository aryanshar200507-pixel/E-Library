package com.project.elibrary.service.categoryservice;

import java.util.List;

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

	@Override
	public List<Category> getAllCategories() {
		return categoryDao.findAllCategories();
	}

	@Override
	public List<Category> getCategories(int page, int pageSize) {

		if (page < 1) {
			throw new IllegalArgumentException("Page must be greater than 0.");
		}

		if (pageSize < 1) {
			throw new IllegalArgumentException("Page size must be greater than 0.");
		}

		int offset = (page - 1) * pageSize;

		return categoryDao.findCategories(offset, pageSize);
	}

	@Override
	public int getTotalCategories() {

		return categoryDao.countAllCategories();
	}

	@Override
	public List<Category> getTopRatedCategories(int limit) {

		if (limit <= 0) {
			throw new IllegalArgumentException("Limit must be greater than 0.");
		}

		return categoryDao.findTopRatedCategories(limit);
	}
}
