package com.project.elibrary.bean.remember;

import java.time.LocalDateTime;

/**
 * Represents a persistent "Remember Me" login token.
 *
 * This object connects a user with a secure remember-me token
 * and stores the time at which that token expires.
 */
public class RememberMe {

    private Long tokenId;

    private Long userId;

    private String tokenHash;

    private LocalDateTime expiresAt;

    private LocalDateTime createdAt;


    public RememberMe() {
    }


    public RememberMe(
            Long tokenId,
            Long userId,
            String tokenHash,
            LocalDateTime expiresAt,
            LocalDateTime createdAt) {

        this.tokenId = tokenId;
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }


    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }


    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}