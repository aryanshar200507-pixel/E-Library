package com.project.elibrary.service.remembermeservice;

import com.project.elibrary.bean.remember.RememberMe;

public interface RememberMeService {

    /**
     * Creates and stores a new remember-me token
     * for the specified user.
     *
     * @param userId user ID
     * @return the raw token that will be sent to the browser
     */
    String createToken(Long userId);

    /**
     * Finds a remember-me record using the hashed token.
     *
     * @param token raw token received from the browser
     * @return RememberMe record if valid, otherwise null
     */
    RememberMe validateToken(String token);

    /**
     * Deletes a remember-me token.
     *
     * @param token raw token received from the browser
     * @return true if deleted successfully
     */
    boolean deleteToken(String token);

    /**
     * Deletes all remember-me tokens belonging
     * to a specific user.
     *
     * @param userId user ID
     * @return true if deletion succeeds
     */
    boolean deleteTokensForUser(Long userId);
}