package com.notif1ed.util;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Modern Toast Notification System for JavaFX
 * Displays non-blocking, auto-dismissing notifications with animations
 */
public class ToastNotification {
    
    public enum ToastType {
        SUCCESS("#4CAF50", "✓", "Success"),
        INFO("#2196F3", "ℹ", "Info"),
        WARNING("#FF9800", "⚠", "Warning"),
        ERROR("#F44336", "✕", "Error");
        
        private final String color;
        private final String icon;
        private final String title;
        
        ToastType(String color, String icon, String title) {
            this.color = color;
            this.icon = icon;
            this.title = title;
        }
    }
    
    /**
     * Show a toast notification
     * @param ownerStage The parent stage (for positioning)
     * @param type Toast type (SUCCESS, INFO, WARNING, ERROR)
     * @param message The message to display
     */
    public static void show(Stage ownerStage, ToastType type, String message) {
        show(ownerStage, type, message, 3000);
    }
    
    /**
     * Show a toast notification with custom duration
     * @param ownerStage The parent stage (for positioning)
     * @param type Toast type
     * @param message The message to display
     * @param durationMs Duration in milliseconds before auto-dismiss
     */
    public static void show(Stage ownerStage, ToastType type, String message, int durationMs) {
        Stage toastStage = new Stage();
        toastStage.initOwner(ownerStage);
        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);
        
        // Create toast container
        VBox toastBox = new VBox(5);
        toastBox.setAlignment(Pos.CENTER_LEFT);
        toastBox.setStyle(
            "-fx-background-color: " + type.color + ";" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 15 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 5);"
        );
        toastBox.setMaxWidth(400);
        
        // Icon and title
        Label iconLabel = new Label(type.icon);
        iconLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;"
        );
        
        Label titleLabel = new Label(type.title);
        titleLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;"
        );
        
        // Message
        Label messageLabel = new Label(message);
        messageLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: white;" +
            "-fx-wrap-text: true;"
        );
        messageLabel.setMaxWidth(350);
        messageLabel.setWrapText(true);
        
        // Header with icon and title
        javafx.scene.layout.HBox header = new javafx.scene.layout.HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getChildren().addAll(iconLabel, titleLabel);
        
        toastBox.getChildren().addAll(header, messageLabel);
        
        StackPane root = new StackPane(toastBox);
        root.setStyle("-fx-background-color: transparent;");
        
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        toastStage.setScene(scene);
        
        // Position toast at bottom-right of owner stage
        toastStage.setAlwaysOnTop(true);
        
        // Calculate position
        double ownerX = ownerStage.getX();
        double ownerY = ownerStage.getY();
        double ownerWidth = ownerStage.getWidth();
        double ownerHeight = ownerStage.getHeight();
        
        toastStage.show();
        
        double toastWidth = toastStage.getWidth();
        double toastHeight = toastStage.getHeight();
        
        // Position: bottom-right with padding
        double startX = ownerX + ownerWidth - toastWidth - 20;
        double startY = ownerY + ownerHeight + 50; // Start below screen
        double endY = ownerY + ownerHeight - toastHeight - 20;
        
        toastStage.setX(startX);
        toastStage.setY(startY);
        
        // Slide-in animation
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), toastBox);
        slideIn.setFromY(50);
        slideIn.setToY(0);
        
        // Fade-in
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), toastBox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        
        ParallelTransition showTransition = new ParallelTransition(slideIn, fadeIn);
        
        // Fade-out animation
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), toastBox);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> toastStage.close());
        
        // Auto-dismiss after duration
        PauseTransition delay = new PauseTransition(Duration.millis(durationMs));
        delay.setOnFinished(e -> fadeOut.play());
        
        SequentialTransition sequence = new SequentialTransition(showTransition, delay);
        sequence.play();
        
        // Allow click to dismiss
        toastBox.setOnMouseClicked(e -> {
            sequence.stop();
            fadeOut.play();
        });
    }
    
    // Convenience methods
    public static void showSuccess(Stage ownerStage, String message) {
        show(ownerStage, ToastType.SUCCESS, message);
    }
    
    public static void showInfo(Stage ownerStage, String message) {
        show(ownerStage, ToastType.INFO, message);
    }
    
    public static void showWarning(Stage ownerStage, String message) {
        show(ownerStage, ToastType.WARNING, message);
    }
    
    public static void showError(Stage ownerStage, String message) {
        show(ownerStage, ToastType.ERROR, message);
    }
}
