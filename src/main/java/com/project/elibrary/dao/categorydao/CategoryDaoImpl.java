package com.project.elibrary.dao.categorydao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.project.elibrary.bean.category.Category;
import com.project.elibrary.config.DatabaseConnection;
//import java.util.List;

//import com.project.elibrary.bean.category.Category;
//import com.project.elibrary.config.DatabaseConnection;

public class CategoryDaoImpl implements CategoryDao {

	  @Override
	    public int countAllCategories() {

	        String sql = """
	                SELECT COUNT(*) AS total
	                FROM category
	                """;

	        try (Connection connection = DatabaseConnection.getConnection();
	             PreparedStatement statement = connection.prepareStatement(sql);
	             ResultSet resultSet = statement.executeQuery()) {

	            if (resultSet.next()) {
	                return resultSet.getInt("total");
	            }

	            return 0;

	        } catch (SQLException e) {
	            throw new RuntimeException("Failed to count categories.", e);
	        }
	    }
	  
	  @Override
	  public boolean addCategories(Category categories) {
		  String sql = """
	               INSERT INTO category (category_name) VALUES (?);
	            
	                """;

	        try (Connection connection = DatabaseConnection.getConnection();
	             PreparedStatement statement = connection.prepareStatement(sql);
	             ){
	        	statement.setString(1,categories.getCategoryName());
	        	int rowsAffected = statement.executeUpdate();

	            return rowsAffected>0;

	        } catch (SQLException e) {
	            throw new RuntimeException("Failed to count categories.", e);
	        }
		  
	  }
	
}
