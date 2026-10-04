package com.novablog.api.dto;

public record LoginResponse(String accessToken, String tokenType, long expiresIn, UserProfile user) {
    public record UserProfile(Long id, String username, String displayName, String role) {
    }
}
