package com.notif1ed.util;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
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
        
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        content.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 10);"
        );
        content.setMaxWidth(450);
        
        // Welcome icon
        Label icon = new Label("👋");
        icon.setStyle(
            "-fx-font-size: 64px;"
        );
        
        // Greeting based on time of day
        String greeting = getTimeBasedGreeting();
        Label greetingLabel = new Label(greeting);
        greetingLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #2196F3;"
        );
        
        // User name
        Label nameLabel = new Label(userName + "!");
        nameLabel.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #333;"
        );
        
        // Welcome message
        Label messageLabel = new Label("Welcome back to Notif1ed");
        messageLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: #666;"
        );
        
        // Subtitle
        Label subtitleLabel = new Label("Your student notification system is ready");
        subtitleLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #999;"
        );
        
        // Quick stats or info (optional enhancement)
        VBox statsBox = new VBox(5);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setStyle(
            "-fx-background-color: #f5f5f5;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 15;"
        );
        
        Label statsLabel = new Label("Ready to manage students and subjects");
        statsLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: #666;" +
            "-fx-font-style: italic;"
        );
        
        statsBox.getChildren().add(statsLabel);
        
        // Continue button
        Button continueBtn = new Button("Get Started");
        continueBtn.setStyle(
            "-fx-background-color: #2196F3;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 15px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12 40;" +
            "-fx-background-radius: 25;" +
            "-fx-cursor: hand;"
        );
        
        continueBtn.setOnMouseEntered(e -> {
            continueBtn.setStyle(
                "-fx-background-color: #1976D2;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 12 40;" +
                "-fx-background-radius: 25;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(33,150,243,0.4), 10, 0, 0, 3);"
            );
        });
        
        continueBtn.setOnMouseExited(e -> {
            continueBtn.setStyle(
                "-fx-background-color: #2196F3;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 12 40;" +
                "-fx-background-radius: 25;" +
                "-fx-cursor: hand;"
            );
        });
        
        continueBtn.setOnAction(e -> dialog.close());
        
        content.getChildren().addAll(
            icon,
            greetingLabel,
            nameLabel,
            messageLabel,
            subtitleLabel,
            statsBox,
            continueBtn
        );
        
        Scene scene = new Scene(content);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        
        // Center on owner
        dialog.setX(ownerStage.getX() + ownerStage.getWidth() / 2 - 225);
        dialog.setY(ownerStage.getY() + ownerStage.getHeight() / 2 - 250);
        
        // Fade-in animation
        content.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(400), content);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        
        dialog.show();
        fadeIn.play();
        
        // Auto-close after 3 seconds (optional - can be removed if user should click)
        // PauseTransition delay = new PauseTransition(Duration.seconds(3));
        // delay.setOnFinished(e -> dialog.close());
        // delay.play();
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
