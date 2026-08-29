package com.project.elibrary.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
//import java.util.ArrayList;
import java.util.List;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.userservice.UserService;
import com.project.elibrary.service.userservice.UserServiceImpl;

/**
 * Servlet implementation class AdminDashboardServlet
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final UserService userService = new UserServiceImpl();   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminDashboardServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		loadTotalUsers(request);
        //loadUsers(request);
            
        String keyword = request.getParameter("keyword");

        if (keyword != null && !keyword.trim().isEmpty()) {
            loadSearchUsers(request);
        } else {
            loadUsers(request);
        }
       
        request.getRequestDispatcher("/WEB-INF/admin/dashboard.jsp")
               .forward(request, response);
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
    
    //Method to get the userId to block
   private void loadIdToBlock(HttpServletRequest request) {
	   
//	   System.out.println("BLOCK CALLED");
	   
	   String userIdParam = request.getParameter("userId");
	   long userId = Long.parseLong(userIdParam);
	    userService.deactivateUser(userId);
	   
	  
   }
   private void loadIdToRecover(HttpServletRequest request) {
	   String userIdParam = request.getParameter("userId");
	   long userId = Long.parseLong(userIdParam);
	   userService.recoverUser(userId);
	  
   }
   
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
		throws ServletException, IOException {
    	
//    	System.out.println("DOPOST CALLED");
//    	System.out.println("Action = " + request.getParameter("action"));
//    	System.out.println("User ID = " + request.getParameter("userId"));
    	
    	
    	String action = request.getParameter("action");

    	if ("block".equals(action)) {

    	    loadIdToBlock(request);

    	} else if ("recover".equals(action)) {

    	    loadIdToRecover(request);
    	}

    	response.sendRedirect(
    	    request.getContextPath() + "/admin/dashboard"
    	);
		//doGet(request, response);
		
		}

}
