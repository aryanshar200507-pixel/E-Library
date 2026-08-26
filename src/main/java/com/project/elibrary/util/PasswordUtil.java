package com.project.elibrary.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for securely hashing and verifying passwords.
 *
 * BCrypt is used so that passwords are not stored as plain text
 * in the database.
 *
 * This class is used by the service layer whenever a password
 * needs to be stored or verified.
 */
public final class PasswordUtil {

    // Prevent object creation because this class only provides
    // static utility methods.
    private PasswordUtil() {
    }

    /**
     * Hashes a plain-text password using BCrypt.
     *
     * BCrypt automatically generates a salt when gensalt()
     * is used. The salt is included in the resulting hash,
     * so it does not need to be stored separately.
     *
     * The original password should never be stored in the database.
     *
     * @param password plain-text password
     * @return BCrypt hashed password
     */
    public static String hasPassword(String password) {

        return BCrypt.hashpw(
                password,
                BCrypt.gensalt()
        );
    }

    /**
     * Checks whether a plain-text password matches a BCrypt hash.
     *
     * The plain-text password is not converted manually.
     * BCrypt.checkpw() extracts the salt from the stored hash
     * and performs the comparison.
     *
     * @param password plain-text password entered by the user
     * @param hashedPassword password hash stored in the database
     * @return true if the password matches, otherwise false
     */
    public static boolean verifyPassword(
            String password,
            String hashedPassword) {

        return BCrypt.checkpw(
                password,
                hashedPassword
        );
    }
}