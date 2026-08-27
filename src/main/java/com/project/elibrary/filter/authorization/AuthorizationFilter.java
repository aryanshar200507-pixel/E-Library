package com.project.elibrary.filter.authorization;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.project.elibrary.bean.enums.Role;
import com.project.elibrary.bean.user.User;

/**
 * Filter can intercept an Http request before it reaches the Servlet/jsp , that's perfect for authorization , without a filter , we'd have to write this in every admin servlet
 */

//Whenever someone requests something whose URL starts with /admin/, run this filter first.
@WebFilter("/admin/*")
public class AuthorizationFilter extends HttpFilter implements Filter {
       
  
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
/**
 * A Filter receives a generic ServletRequest.

We convert it to HttpServletRequest because we're working with an HTTP web application.		
 */
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		/**
		 * Don't create a new session if one doesn't already exist.
		 * why ? 
		 * Suppose someone who isn't logged in directly enters: the admin url and access admin features to stop that we don't want filter to create new session 
		 */
		HttpSession session = httpServletRequest.getSession(false);
		
		if(session == null) {
			httpResponse.sendRedirect(httpServletRequest.getContextPath()+"/login.jsp");
			return;
		}
		
		User user = (User) session.getAttribute("loggedInUser");
		
		if(user == null) {
			httpResponse.sendRedirect(httpServletRequest.getContextPath()+"/login.jsp");
			return;
		}
		
		if(user.getRole() != Role.ADMIN) {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN , "You do not have permission to access this resources.");
			return;
		}
		
//	very important it means : 	The user passed the authorization check, so continue processing the request.
		chain.doFilter(request, response);
	}

}

