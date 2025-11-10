package com.notif1ed.util;

import java.time.LocalDateTime;

/**
 * Session manager for handling user authentication state.
 * Implements Singleton pattern to maintain a single session across the application.
 * 
 * @author Notif1ed Development Team
 * @version 1.0.0
 */
public class SessionManager {
    
    private static SessionManager instance;
    
    private Integer userId;
    private String userName;
    private String userEmail;
    private LocalDateTime loginTime;
    
    /**
     * Private constructor to enforce singleton pattern.
     */
    private SessionManager() {
        // Private constructor
    }
    
    /**
     * Gets the singleton instance of SessionManager.
     * 
     * @return the SessionManager instance
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    /**
     * Logs in a user and stores their session information.
     * 
     * @param userId the user's ID from the database
     * @param name the user's full name
     * @param email the user's email address
     */
    public void login(int userId, String name, String email) {
        this.userId = userId;
        this.userName = name;
        this.userEmail = email;
        this.loginTime = LocalDateTime.now();
    }
    
    /**
     * Logs out the current user and clears session data.
     */
    public void logout() {
        this.userId = null;
        this.userName = null;
        this.userEmail = null;
        this.loginTime = null;
    }
    
    /**
     * Checks if a user is currently logged in.
     * 
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return userId != null;
    }
    
    /**
     * Gets the current user's ID.
     * 
     * @return the user ID, or null if not logged in
     */
    public Integer getUserId() {
        return userId;
    }
    
    /**
     * Gets the current user's name.
     * 
     * @return the user name, or null if not logged in
     */
    public String getUserName() {
        return userName;
    }
    
    /**
     * Gets the current user's email.
     * 
     * @return the user email, or null if not logged in
     */
    public String getUserEmail() {
        return userEmail;
    }
    
    /**
     * Gets the time when the user logged in.
     * 
     * @return the login time, or null if not logged in
     */
    public LocalDateTime getLoginTime() {
        return loginTime;
    }
    
    /**
     * Gets the duration of the current session in minutes.
     * 
     * @return session duration in minutes, or 0 if not logged in
     */
    public long getSessionDurationMinutes() {
        if (loginTime == null) {
            return 0;
        }
        return java.time.Duration.between(loginTime, LocalDateTime.now()).toMinutes();
    }
}
