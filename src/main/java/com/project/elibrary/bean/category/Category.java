package com.project.elibrary.bean.category;

public class Category 
{
	private Long  categoryId;
	private String categoryName;
	
	public Category() {}
	
	Category(String categoryName){
		super();
		this.categoryName=categoryName;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	
}
