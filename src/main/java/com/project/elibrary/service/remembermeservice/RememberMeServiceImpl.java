package com.project.elibrary.service.remembermeservice;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import com.project.elibrary.bean.remember.RememberMe;
import com.project.elibrary.dao.remember.RememberMeDao;
import com.project.elibrary.dao.remember.RememberMeDaoImpl;

public class RememberMeServiceImpl implements RememberMeService {

    /*
     * SecureRandom is used instead of the normal Random class
     * because this token will be used for authentication.
     */
    private final SecureRandom secureRandom;

    private final RememberMeDao rememberMeDao;

    /*
     * Remember-Me lifetime.
     *
     * 7 days = 7 × 24 hours.
     */
    private static final long REMEMBER_ME_DAYS = 7;


    public RememberMeServiceImpl() {

        this.secureRandom = new SecureRandom();

        this.rememberMeDao =
                new RememberMeDaoImpl();
    }


    /**
     * Creates a new secure remember-me token.
     */
    @Override
    public String createToken(Long userId) {

        if (userId == null) {
            return null;
        }

        /*
         * Generate 32 random bytes.
         *
         * 32 bytes = 256 bits of randomness.
         */
        byte[] randomBytes = new byte[32];

        secureRandom.nextBytes(randomBytes);

        /*
         * Convert the random bytes into a
         * URL-safe Base64 string.
         */
        String rawToken =
                Base64.getUrlEncoder()
                      .withoutPadding()
                      .encodeToString(randomBytes);

        /*
         * Only the HASH of the token is stored
         * in the database.
         */
        String tokenHash =
                hashToken(rawToken);

        /*
         * Token expires exactly 7 days from now.
         */
        LocalDateTime expiresAt =
                LocalDateTime.now()
                             .plusDays(REMEMBER_ME_DAYS);

        RememberMe rememberMe =
                new RememberMe();

        rememberMe.setUserId(userId);
        rememberMe.setTokenHash(tokenHash);
        rememberMe.setExpiresAt(expiresAt);

        boolean saved =
                rememberMeDao.save(rememberMe);

        if (!saved) {
            return null;
        }

        /*
         * Return the RAW token.
         *
         * This goes into the browser cookie.
         *
         * The raw token is never stored in the database.
         */
        return rawToken;
    }


    /**
     * Validates a raw token received from the browser.
     */
    @Override
    public RememberMe validateToken(String token) {

        if (token == null || token.isBlank()) {
            return null;
        }

        /*
         * Hash the browser token so that it can be
         * compared with the hash stored in the database.
         */
        String tokenHash =
                hashToken(token);

        RememberMe rememberMe =
                rememberMeDao.findByTokenHash(tokenHash);

        if (rememberMe == null) {
            return null;
        }

        /*
         * Check whether the token has expired.
         */
        if (rememberMe.getExpiresAt() == null
                || rememberMe.getExpiresAt()
                            .isBefore(LocalDateTime.now())) {

            /*
             * Remove expired token from database.
             */
            rememberMeDao.deleteByTokenHash(tokenHash);

            return null;
        }

        return rememberMe;
    }


    /**
     * Deletes a token using the raw browser token.
     */
    @Override
    public boolean deleteToken(String token) {

        if (token == null || token.isBlank()) {
            return false;
        }

        String tokenHash =
                hashToken(token);

        return rememberMeDao.deleteByTokenHash(
                tokenHash
        );
    }


    /**
     * Deletes all remember-me tokens belonging
     * to a specific user.
     */
    @Override
    public boolean deleteTokensForUser(Long userId) {

        if (userId == null) {
            return false;
        }

        return rememberMeDao.deleteByUserId(userId);
    }


    /**
     * Creates a SHA-256 hash of the token.
     *
     * The database stores this hash instead of the
     * actual authentication token.
     */
    private String hashToken(String token) {

        try {

            MessageDigest messageDigest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    messageDigest.digest(
                            token.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            /*
             * Convert the hash bytes to a hexadecimal string.
             */
            StringBuilder hex =
                    new StringBuilder();

            for (byte b : hash) {

                hex.append(
                        String.format(
                                "%02x",
                                b
                        )
                );
            }

            return hex.toString();

        } catch (NoSuchAlgorithmException e) {

            throw new IllegalStateException(
                    "SHA-256 algorithm is not available.",
                    e
            );
        }
    }
}