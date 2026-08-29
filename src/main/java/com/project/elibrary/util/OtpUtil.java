package com.project.elibrary.util;

import java.security.SecureRandom;

/**
 * Utility class for generating OTPs.
 *
 * Currently generates a random 6-digit OTP.
 */
public final class OtpUtil {

    // SecureRandom is used because OTPs are security-related.
    private static final SecureRandom RANDOM = new SecureRandom();

    // Prevent object creation because this is a utility class.
    private OtpUtil() {
    }

    /**
     * Generates a random 6-digit OTP.
     *
     * @return a 6-digit OTP as a String
     */
    public static String generateOtp() {

        // Generates a number between 100000 and 999999.
        int otp = 100000 + RANDOM.nextInt(900000);

        // Return the OTP as String so it can be easily
        // sent through email and treated as text.
        return String.valueOf(otp);
    }
}