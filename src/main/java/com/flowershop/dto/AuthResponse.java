package com.flowershop.dto;

public class AuthResponse {
    private String token;
    private String username;
    private String message;
    private boolean success;

    // Constructors
    public AuthResponse() {}

    public AuthResponse(String token, String username, String message, boolean success) {
        this.token = token;
        this.username = username;
        this.message = message;
        this.success = success;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}