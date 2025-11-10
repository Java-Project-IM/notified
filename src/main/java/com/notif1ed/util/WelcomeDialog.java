package com.notif1ed.util;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.ParallelTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.time.LocalTime;

/**
 * Welcome Dialog for user authentication
 * Shows a personalized greeting after successful login
 */
public class WelcomeDialog {
    
    /**
     * Show a welcome dialog with personalized greeting
     * @param ownerStage The parent stage
     * @param userName The name of the logged-in user
     */
    public static void show(Stage ownerStage, String userName) {
        Stage dialog = new Stage();
        dialog.initOwner(ownerStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setResizable(false);
        
        // Create backdrop with semi-transparent dark overlay
        StackPane backdrop = new StackPane();
        backdrop.setStyle("-fx-background-color: rgba(0, 0, 0, 0.65);");
        // CRITICAL FIX: Do NOT set backdrop preferred size - this causes children to stretch
        // The backdrop will automatically fill the dialog stage
        
        // Create the main content card - FIXED WIDTH, NATURAL HEIGHT
        VBox content = new VBox(18);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(35, 45, 35, 45));
        content.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ffffff 0%, #f8f9fa 100%);" +
            "-fx-background-radius: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 30, 0, 0, 10);"
        );
        // Set fixed width only
        content.setPrefWidth(480);
        content.setMaxWidth(480);
        content.setMinWidth(480);
        
        // CRITICAL FIX: Prevent VBox from stretching vertically
        // This tells the VBox to use only its preferred height (calculated from children)
        // instead of growing to fill the parent StackPane
        content.setMaxHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
        
        // Decorative top accent bar
        VBox accentBar = new VBox();
        accentBar.setPrefHeight(5);
        accentBar.setPrefWidth(480);
        accentBar.setStyle(
            "-fx-background-color: linear-gradient(to right, #2196F3, #21CBF3);" +
            "-fx-background-radius: 20 20 0 0;"
        );
        
        // Welcome icon with gradient background circle
        StackPane iconContainer = new StackPane();
        VBox iconCircle = new VBox();
        iconCircle.setPrefSize(80, 80);
        iconCircle.setMinSize(80, 80);
        iconCircle.setMaxSize(80, 80);
        iconCircle.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #2196F3, #21CBF3);" +
            "-fx-background-radius: 40;" +
            "-fx-effect: dropshadow(gaussian, rgba(33,150,243,0.4), 15, 0, 0, 5);"
        );
        
        Label icon = new Label("👋");
        icon.setStyle(
            "-fx-font-size: 42px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
        );
        
        iconContainer.getChildren().addAll(iconCircle, icon);
        // Remove fixed height to allow natural sizing
        iconContainer.setMaxHeight(80);
        
        // Greeting based on time of day
        String greeting = getTimeBasedGreeting();
        Label greetingLabel = new Label(greeting);
        greetingLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #2196F3;" +
            "-fx-font-family: 'System';"
        );
        
        // User name with larger, bolder style
        Label nameLabel = new Label(userName + "!");
        nameLabel.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #1a1a1a;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 2, 0, 0, 1);"
        );
        
        // Welcome message
        Label messageLabel = new Label("Welcome back to Notif1ed");
        messageLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: #424242;"
        );
        
        // Subtitle
        Label subtitleLabel = new Label("Your student notification system is ready");
        subtitleLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: #757575;" +
            "-fx-font-style: italic;"
        );
        
        // Quick stats box with modern card design
        VBox statsBox = new VBox(5);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #E3F2FD, #F1F8FE);" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 12 20;" +
            "-fx-border-color: #2196F3;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 12;"
        );
        statsBox.setPrefWidth(360);
        statsBox.setMaxWidth(360);
        
        Label statsIcon = new Label("✨");
        statsIcon.setStyle("-fx-font-size: 20px;");
        
        Label statsLabel = new Label("Ready to manage students and subjects");
        statsLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #1976D2;" +
            "-fx-font-weight: 500;"
        );
        
        statsBox.getChildren().addAll(statsIcon, statsLabel);
        
        // Continue button with gradient and hover effect
        Button continueBtn = new Button("Get Started");
        continueBtn.setPrefWidth(200);
        continueBtn.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #2196F3, #1976D2);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12 40;" +
            "-fx-background-radius: 25;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(33,150,243,0.4), 10, 0, 0, 4);"
        );
        
        continueBtn.setOnMouseEntered(e -> {
            continueBtn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #1E88E5, #1565C0);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 12 40;" +
                "-fx-background-radius: 25;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(33,150,243,0.6), 15, 0, 0, 6);" +
                "-fx-scale-y: 1.05;" +
                "-fx-scale-x: 1.05;"
            );
        });
        
        continueBtn.setOnMouseExited(e -> {
            continueBtn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #2196F3, #1976D2);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 12 40;" +
                "-fx-background-radius: 25;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(33,150,243,0.4), 10, 0, 0, 4);"
            );
        });
        
        continueBtn.setOnAction(e -> dialog.close());
        
        // Add spacing between elements
        VBox nameSection = new VBox(5);
        nameSection.setAlignment(Pos.CENTER);
        nameSection.getChildren().addAll(greetingLabel, nameLabel);
        
        VBox messageSection = new VBox(5);
        messageSection.setAlignment(Pos.CENTER);
        messageSection.getChildren().addAll(messageLabel, subtitleLabel);
        
        // Assemble the content - note the reduced spacing
        content.getChildren().addAll(
            iconContainer,
            nameSection,
            messageSection,
            statsBox,
            continueBtn
        );
        
        // Add content to backdrop
        backdrop.getChildren().add(content);
        StackPane.setAlignment(content, Pos.CENTER);
        
        Scene scene = new Scene(backdrop);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        
        // Position dialog to cover the entire owner stage
        dialog.setX(ownerStage.getX());
        dialog.setY(ownerStage.getY());
        dialog.setWidth(ownerStage.getWidth());
        dialog.setHeight(ownerStage.getHeight());
        
        // Animations - fade in backdrop and scale in content
        backdrop.setOpacity(0);
        content.setScaleX(0.85);
        content.setScaleY(0.85);
        content.setOpacity(0);
        
        FadeTransition backdropFade = new FadeTransition(Duration.millis(250), backdrop);
        backdropFade.setFromValue(0);
        backdropFade.setToValue(1);
        
        FadeTransition contentFade = new FadeTransition(Duration.millis(350), content);
        contentFade.setFromValue(0);
        contentFade.setToValue(1);
        
        ScaleTransition contentScale = new ScaleTransition(Duration.millis(350), content);
        contentScale.setFromX(0.85);
        contentScale.setFromY(0.85);
        contentScale.setToX(1.0);
        contentScale.setToY(1.0);
        
        ParallelTransition showAnimation = new ParallelTransition(
            backdropFade,
            contentFade,
            contentScale
        );
        
        dialog.show();
        showAnimation.play();
        
        // Close on backdrop click
        backdrop.setOnMouseClicked(e -> {
            if (e.getTarget() == backdrop) {
                dialog.close();
            }
        });
    }
    
    /**
     * Get greeting based on time of day
     */
    private static String getTimeBasedGreeting() {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        
        if (hour >= 5 && hour < 12) {
            return "Good Morning";
        } else if (hour >= 12 && hour < 17) {
            return "Good Afternoon";
        } else if (hour >= 17 && hour < 21) {
            return "Good Evening";
        } else {
            return "Welcome";
        }
    }
}
