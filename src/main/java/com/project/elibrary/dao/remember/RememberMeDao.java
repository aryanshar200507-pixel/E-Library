package com.project.elibrary.dao.remember;

import com.project.elibrary.bean.remember.RememberMe;

public interface RememberMeDao {

    /**
     * Saves a new remember-me token.
     *
     * @param rememberMe token information
     * @return true if saved successfully
     */
    boolean save(RememberMe rememberMe);

    /**
     * Finds a remember-me token using its hash.
     *
     * @param tokenHash hashed token
     * @return RememberMe if found, otherwise null
     */
    RememberMe findByTokenHash(String tokenHash);

    /**
     * Deletes a remember-me token.
     *
     * @param tokenHash hashed token
     * @return true if deleted successfully
     */
    boolean deleteByTokenHash(String tokenHash);

    /**
     * Deletes all remember-me tokens belonging to a user.
     *
     * @param userId user ID
     * @return true if deletion succeeds
     */
    boolean deleteByUserId(Long userId);
}