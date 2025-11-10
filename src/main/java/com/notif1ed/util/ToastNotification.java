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
        SUCCESS("#4CAF50", "✓", "Success", "#E8F5E9"),
        INFO("#2196F3", "ℹ", "Info", "#E3F2FD"),
        WARNING("#FF9800", "⚠", "Warning", "#FFF3E0"),
        ERROR("#F44336", "✕", "Error", "#FFEBEE");
        
        private final String color;
        private final String icon;
        private final String title;
        private final String bgColor;
        
        ToastType(String color, String icon, String title, String bgColor) {
            this.color = color;
            this.icon = icon;
            this.title = title;
            this.bgColor = bgColor;
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
        toastStage.setAlwaysOnTop(true);
        
        // Create toast container with modern card design
        VBox toastBox = new VBox(12);
        toastBox.setAlignment(Pos.CENTER_LEFT);
        toastBox.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 20 25;" +
            "-fx-border-color: " + type.color + ";" +
            "-fx-border-width: 0 0 0 5;" +
            "-fx-border-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0, 0, 5);"
        );
        toastBox.setMinWidth(320);
        toastBox.setMaxWidth(450);
        
        // Header with icon and title
        javafx.scene.layout.HBox header = new javafx.scene.layout.HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        
        // Icon container with colored background
        StackPane iconContainer = new StackPane();
        javafx.scene.layout.VBox iconBg = new javafx.scene.layout.VBox();
        iconBg.setPrefSize(40, 40);
        iconBg.setMinSize(40, 40);
        iconBg.setMaxSize(40, 40);
        iconBg.setStyle(
            "-fx-background-color: " + type.bgColor + ";" +
            "-fx-background-radius: 20;"
        );
        
        Label iconLabel = new Label(type.icon);
        iconLabel.setStyle(
            "-fx-font-size: 22px;" +
            "-fx-text-fill: " + type.color + ";" +
            "-fx-font-weight: bold;"
        );
        
        iconContainer.getChildren().addAll(iconBg, iconLabel);
        
        // Title and message in vertical layout
        VBox textBox = new VBox(4);
        textBox.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label(type.title);
        titleLabel.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #212121;"
        );
        
        Label messageLabel = new Label(message);
        messageLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: #616161;" +
            "-fx-wrap-text: true;"
        );
        messageLabel.setMaxWidth(350);
        messageLabel.setWrapText(true);
        
        textBox.getChildren().addAll(titleLabel, messageLabel);
        
        header.getChildren().addAll(iconContainer, textBox);
        
        toastBox.getChildren().add(header);
        
        StackPane root = new StackPane(toastBox);
        root.setStyle("-fx-background-color: transparent;");
        
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        toastStage.setScene(scene);
        
        // Show stage to get dimensions
        toastStage.show();
        
        double toastWidth = toastStage.getWidth();
        double toastHeight = toastStage.getHeight();
        
        // Position: TOP-RIGHT corner with padding
        double ownerX = ownerStage.getX();
        double ownerY = ownerStage.getY();
        double ownerWidth = ownerStage.getWidth();
        
        double finalX = ownerX + ownerWidth - toastWidth - 20;
        double finalY = ownerY + 20; // Top position
        
        // Set final position
        toastStage.setX(finalX);
        toastStage.setY(finalY);
        
        // Slide-in animation from RIGHT using TranslateTransition
        toastBox.setTranslateX(toastWidth + 50); // Start off-screen to the right
        toastBox.setOpacity(0);
        
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(400), toastBox);
        slideIn.setFromX(toastWidth + 50);
        slideIn.setToX(0);
        slideIn.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
        
        FadeTransition fadeIn = new FadeTransition(Duration.millis(400), toastBox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        
        ParallelTransition showTransition = new ParallelTransition(slideIn, fadeIn);
        
        // Slide-out animation to the RIGHT
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(350), toastBox);
        slideOut.setToX(toastWidth + 50);
        slideOut.setInterpolator(javafx.animation.Interpolator.EASE_IN);
        
        FadeTransition fadeOut = new FadeTransition(Duration.millis(350), toastBox);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        
        ParallelTransition hideTransition = new ParallelTransition(slideOut, fadeOut);
        hideTransition.setOnFinished(e -> toastStage.close());
        
        // Auto-dismiss after duration
        PauseTransition delay = new PauseTransition(Duration.millis(durationMs));
        delay.setOnFinished(e -> hideTransition.play());
        
        SequentialTransition sequence = new SequentialTransition(showTransition, delay);
        sequence.play();
        
        // Allow click to dismiss with slide-out animation
        toastBox.setOnMouseClicked(e -> {
            sequence.stop();
            hideTransition.play();
        });
        
        // Hover effect - slight scale
        toastBox.setOnMouseEntered(e -> {
            toastBox.setScaleX(1.02);
            toastBox.setScaleY(1.02);
            sequence.pause(); // Pause auto-dismiss on hover
        });
        
        toastBox.setOnMouseExited(e -> {
            toastBox.setScaleX(1.0);
            toastBox.setScaleY(1.0);
            sequence.play(); // Resume auto-dismiss
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
