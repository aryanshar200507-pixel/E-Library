package com.project.elibrary.dao.bookrequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.project.elibrary.config.DatabaseConnection;

public class BookRequestVoteDao {

    // Add a vote
    public boolean addVote(long requestId, long userId) {

        String sql = "INSERT INTO book_request_vote "
                   + "(request_id, user_id) "
                   + "VALUES (?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, requestId);
            ps.setLong(2, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Check if user already voted
    public boolean hasVoted(long requestId, long userId) {

        String sql = "SELECT COUNT(*) FROM book_request_vote "
                   + "WHERE request_id = ? AND user_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, requestId);
            ps.setLong(2, userId);

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

    // Get total votes for a request
    public int getVoteCount(long requestId) {

        String sql = "SELECT COUNT(*) FROM book_request_vote "
                   + "WHERE request_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, requestId);

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
}
