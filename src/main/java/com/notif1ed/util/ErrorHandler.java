package com.notif1ed.util;

import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Centralized error handling utility.
 * Provides consistent error logging and user notification across the application.
 * 
 * @author Notif1ed Development Team
 * @version 1.0.0
 */
public class ErrorHandler {
    
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);
    
    /**
     * Private constructor to prevent instantiation.
     */
    private ErrorHandler() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Handles an exception by logging it and showing a user-friendly error message.
     * 
     * @param stage the stage to show the error notification on
     * @param exception the exception that occurred
     * @param userMessage the user-friendly message to display
     */
    public static void handle(Stage stage, Exception exception, String userMessage) {
        // Log the technical details
        log.error("Error occurred: {}", userMessage, exception);
        
        // Show user-friendly message
        if (stage != null) {
            ToastNotification.showError(stage, userMessage);
        }
    }
    
    /**
     * Handles a database error specifically.
     * 
     * @param stage the stage to show the error notification on
     * @param exception the SQL exception
     * @param operation the operation that failed (e.g., "load students")
     */
    public static void handleDatabaseError(Stage stage, Exception exception, String operation) {
        String message = String.format("Failed to %s. Please try again.", operation);
        handle(stage, exception, message);
    }
    
    /**
     * Logs a warning message.
     * 
     * @param message the warning message
     */
    public static void logWarning(String message) {
        log.warn(message);
    }
    
    /**
     * Logs an info message.
     * 
     * @param message the info message
     */
    public static void logInfo(String message) {
        log.info(message);
    }
    
    /**
     * Logs a debug message.
     * 
     * @param message the debug message
     */
    public static void logDebug(String message) {
        log.debug(message);
    }
}
