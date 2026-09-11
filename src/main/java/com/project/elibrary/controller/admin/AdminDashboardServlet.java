package com.project.elibrary.controller.admin;

import java.io.IOException;
//import java.util.ArrayList;
import java.util.List;

import com.project.elibrary.bean.category.Category;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.bookservice.BookService;
import com.project.elibrary.service.bookservice.BookServiceImpl;
import com.project.elibrary.service.categoryservice.CategoryService;
import com.project.elibrary.service.categoryservice.CategoryServiceImpl;
import com.project.elibrary.service.userservice.UserService;
import com.project.elibrary.service.userservice.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AdminDashboardServlet
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final UserService userService = new UserServiceImpl();
	private final Category category = new Category();
	private final CategoryService categoryService = new CategoryServiceImpl();
	private final BookService bookService = new BookServiceImpl();
	
	public AdminDashboardServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Load dashboard statistics
		loadTotalUsers(request);
		loadTotalCategories(request);
		loadTotalBooks(request);

		String keyword = request.getParameter("keyword");

		// Load users according to search
		if (keyword != null && !keyword.trim().isEmpty()) {
			loadSearchUsers(request);
		} else {
			loadUsers(request);
		}
		// Load Categories 
		loadCategories(request);
		
		request.getRequestDispatcher("/WEB-INF/admin/dashboard.jsp").forward(request, response);
	}

	// Method to search users
	private void loadSearchUsers(HttpServletRequest request) {

		String keyword = request.getParameter("keyword");
		List<User> users = userService.searchUsers(keyword);

		request.setAttribute("users", users);

		System.out.println("Users found: " + users.size());

	}

	// Method to count users
	private void loadTotalUsers(HttpServletRequest request) {

		int totalUsers = userService.countAllUsers();

		request.setAttribute("totalUsers", totalUsers);
	}

	// Method to get all users
	private void loadUsers(HttpServletRequest request) {

		List<User> users = userService.findAllUsers();

		request.setAttribute("users", users);

		System.out.println("Users found: " + users.size());
	}

	// Method to get the userId to block
	private void loadIdToBlock(HttpServletRequest request) {
		String userIdParam = request.getParameter("userId");
		long userId = Long.parseLong(userIdParam);
		userService.deactivateUser(userId);
	}

	// Method to get the userId to Recover
	private void loadIdToRecover(HttpServletRequest request) {
		String userIdParam = request.getParameter("userId");
		long userId = Long.parseLong(userIdParam);
		userService.recoverUser(userId);

	}

	// Method to count Categories
	private void loadTotalCategories(HttpServletRequest request) {

		int totalCategories = categoryService.countAllCategories();

		request.setAttribute("totalCategories", totalCategories);
	}
	//Method to get all Categories
	private void loadCategories(HttpServletRequest request) {
		List<Category> categories = categoryService.getAllCategories();

		request.setAttribute("categories", categories);

		System.out.println("Categories found: " + categories.size());
	}
	// Method to add category
	private void addCategory(HttpServletRequest request) {

		String categoryName = request.getParameter("category");

		Category category = new Category();
		category.setCategoryName(categoryName);

		boolean added = categoryService.addCategories(category);

		if (added) {
			System.out.println("Category added successfully");
		} else {
			System.out.println("Failed to add category");
		}
	}
	//Method to update/Edit Category
	private void updateCategory(HttpServletRequest request) {

	    String categoryIdParam = request.getParameter("categoryId");
	    String categoryName = request.getParameter("categoryName");

	    Category category = new Category();

	    if (categoryIdParam != null && !categoryIdParam.trim().isEmpty()) {
	        int categoryId = Integer.parseInt(categoryIdParam);
	        category.setCategoryId(categoryId);
	    }

	    category.setCategoryName(categoryName);

	    boolean updated = categoryService.update(category);

	    if (updated) {
	        System.out.println("Category updated successfully");
	    } else {
	        System.out.println("Failed to update category");
	    }
	}
	
	//Method to delete Category
	private void deleteCategory(HttpServletRequest request) {
		String categoryIdParam = request.getParameter("categoryId");
		
		Category category = new Category();
		if (categoryIdParam != null && !categoryIdParam.trim().isEmpty()) {
	        int categoryId = Integer.parseInt(categoryIdParam);
	        category.setCategoryId(categoryId);
	    }
		 boolean deleted = categoryService.delete(category);

		    if (deleted) {
		        System.out.println("Category deleted successfully");
		    } else {
		        System.out.println("Failed to deleted category");
		    }
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		if ("block".equals(action)) {

			loadIdToBlock(request);

		} else if ("recover".equals(action)) {

			loadIdToRecover(request);
		} else if ("addCategory".equals(action)) {
			addCategory(request);
		}else if("updateCategory".equals(action)) {
			updateCategory(request);
		}else if("deleteCategory".equals(action)) {
			deleteCategory(request);
		}

		response.sendRedirect(request.getContextPath() + "/admin/dashboard");
		// doGet(request, response);

	}

	private void loadTotalBooks(HttpServletRequest request) {

		int totalBooks = bookService.getTotalBooks();

		request.setAttribute("totalBooks", totalBooks);
	}

}
