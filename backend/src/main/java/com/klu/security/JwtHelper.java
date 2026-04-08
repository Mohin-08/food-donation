package com.klu.security;

public class JwtHelper {

    public boolean isValidToken(String token) {
        return token != null && token.startsWith("Bearer ");
    }

    public String extractToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}