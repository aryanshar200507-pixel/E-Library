package com.project.elibrary.controller.rating;

import java.io.IOException;

import com.project.elibrary.bean.enums.Role;
import com.project.elibrary.bean.ratings.Rating;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.ratingservice.RatingService;
import com.project.elibrary.service.ratingservice.RatingServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/rating")
public class RatingServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private RatingService ratingService;

	/*
	 * Create the RatingService when the servlet starts.
	 */
	@Override
	public void init() throws ServletException {

		ratingService = new RatingServiceImpl();
	}

	/*
	 * This method handles the rating submitted when the user clicks a star.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// ==========================================
		// 1. Get existing session
		// ==========================================

		HttpSession session = request.getSession(false);

		/*
		 * If there is no session, the user is not logged in.
		 */
		if (session == null) {

			response.sendRedirect(request.getContextPath() + "/login");

			return;
		}

		// ==========================================
		// 2. Get logged-in user
		// ==========================================

		User loggedInUser = (User) session.getAttribute("loggedInUser");

		/*
		 * If the user object is missing, send them back to login.
		 */
		if (loggedInUser == null) {

			response.sendRedirect(request.getContextPath() + "/login");

			return;
		}

		// ==========================================
		// 3. Only USER can rate
		// ==========================================

		/*
		 * Admins are not allowed to rate books.
		 */
		if (loggedInUser.getRole() != Role.USER) {

			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only users can rate books.");

			return;
		}

		// ==========================================
		// 4. Get data sent from JSP
		// ==========================================

		String bookIdParam = request.getParameter("bookId");

		String ratingParam = request.getParameter("rating");

		/*
		 * Both values are required.
		 */
		if (bookIdParam == null || ratingParam == null) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Book ID and rating are required.");

			return;
		}

		try {

			// ==========================================
			// 5. Convert String values to numbers
			// ==========================================

			Long bookId = Long.parseLong(bookIdParam);

			Integer ratingValue = Integer.parseInt(ratingParam);

			// ==========================================
			// 6. Create Rating object
			// ==========================================

			Rating rating = new Rating(loggedInUser.getUserId(), bookId, ratingValue);

			// ==========================================
			// 7. Check whether user already rated
			// ==========================================

			Rating existingRating = ratingService.getUserRating(loggedInUser.getUserId(), bookId);

			// ==========================================
			// 8. Add or update rating
			// ==========================================

			if (existingRating == null) {

				/*
				 * User has never rated this book. Therefore create a new rating.
				 */
				ratingService.addRating(rating);

			} else {

				/*
				 * User already rated this book.
				 *
				 * Instead of creating another row, update the existing rating.
				 */
				existingRating.setRating(ratingValue);

				ratingService.updateRating(existingRating);
			}

			// ==========================================
			// 9. Return to Books page
			// ==========================================

			response.sendRedirect(request.getContextPath() + "/books/details?id=" + bookId);

		} catch (NumberFormatException e) {

			/*
			 * Happens if bookId or rating is not a number.
			 */
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID or rating.");

		} catch (IllegalArgumentException e) {

			/*
			 * Handles validation errors from RatingService.
			 */
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());

		} catch (IllegalStateException e) {

			/*
			 * Handles cases such as attempting to create a duplicate rating.
			 */
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
	}
}