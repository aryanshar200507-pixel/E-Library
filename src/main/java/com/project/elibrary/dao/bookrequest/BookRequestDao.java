package com.project.elibrary.dao.bookrequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import com.project.elibrary.bean.bookrequest.BookRequest;
import com.project.elibrary.config.DatabaseConnection;

public class BookRequestDao {

    // Add a new book request
    public boolean addRequest(BookRequest request) {

        String sql = "INSERT INTO book_request "
                   + "(user_id, title, author, week_start) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, request.getUserId());
            ps.setString(2, request.getTitle());
            ps.setString(3, request.getAuthor());
            ps.setDate(4, request.getWeekStart());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

 // Get current week's book requests
    public List<BookRequest> getRequests(Date weekStart) {

        List<BookRequest> list = new ArrayList<>();

        String sql = "SELECT * FROM book_request "
                   + "WHERE week_start = ? "
                   + "ORDER BY created_at DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, weekStart);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    BookRequest request = new BookRequest();

                    request.setRequestId(rs.getLong("request_id"));
                    request.setUserId(rs.getLong("user_id"));
                    request.setTitle(rs.getString("title"));
                    request.setAuthor(rs.getString("author"));
                    request.setWeekStart(rs.getDate("week_start"));
                    request.setCreatedAt(rs.getTimestamp("created_at"));

                    list.add(request);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    // Count book requests
    public int getRequestCount(Date weekStart) {

        String sql = "SELECT COUNT(*) FROM book_request WHERE week_start = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, weekStart);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
 // Check if the same book was already requested this week
    public boolean isDuplicateRequest(String title, Date weekStart) {

        String sql = "SELECT COUNT(*) FROM book_request "
                   + "WHERE title = ? AND week_start = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setDate(2, weekStart);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    } 
    
 // Check whether a book request belongs to the given week
    public boolean isCurrentWeekRequest(long requestId, Date weekStart) {

        String sql = "SELECT COUNT(*) FROM book_request "
                   + "WHERE request_id = ? AND week_start = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Set the request ID
            ps.setLong(1, requestId);

            // Set the current week's Monday
            ps.setDate(2, weekStart);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Delete a book request
    public boolean deleteRequest(long requestId) {

        String sql = "DELETE FROM book_request WHERE request_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, requestId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
 // Get the total number of votes for a specific book request
    public int getVoteCount(long requestId) {

        // SQL query to count votes for the given request
        String sql = "SELECT COUNT(*) FROM book_request_vote "
                   + "WHERE request_id = ?";

        // Open database connection and prepare the SQL statement
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Set the book request ID in the SQL query
            ps.setLong(1, requestId);

            // Execute the query and get the result
            try (ResultSet rs = ps.executeQuery()) {

                // Check if a result was returned
                if (rs.next()) {

                    // Return the total number of votes
                    return rs.getInt(1);
                }
            }

        } catch (Exception e) {

            // Print the error if the database operation fails
            e.printStackTrace();
        }

        // Return 0 if there are no votes or an error occurs
        return 0;
    }
}

