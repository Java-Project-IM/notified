package com.notif1ed.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Modern Modal Dialog System
 * Provides styled, customizable modal dialogs with better UX than JavaFX Alert
 */
public class CustomModal {
    
    /**
     * Show a confirmation dialog
     * @param ownerStage Parent stage
     * @param title Dialog title
     * @param message Confirmation message
     * @return true if user clicked Confirm, false otherwise
     */
    public static boolean showConfirmation(Stage ownerStage, String title, String message) {
        return showConfirmation(ownerStage, title, message, "Confirm", "Cancel");
    }
    
    /**
     * Show a confirmation dialog with custom button labels
     */
    public static boolean showConfirmation(Stage ownerStage, String title, String message, 
                                          String confirmText, String cancelText) {
        AtomicBoolean result = new AtomicBoolean(false);
        
        Stage dialog = createDialog(ownerStage, title, 400, 200);
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(30));
        
        // Icon
        Label icon = new Label("?");
        icon.setStyle(
            "-fx-font-size: 48px;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-weight: bold;"
        );
        
        // Message
        Label messageLabel = new Label(message);
        messageLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #333;" +
            "-fx-wrap-text: true;" +
            "-fx-text-alignment: center;"
        );
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);
        
        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button confirmBtn = createButton(confirmText, "#2196F3");
        Button cancelBtn = createButton(cancelText, "#757575");
        
        confirmBtn.setOnAction(e -> {
            result.set(true);
            dialog.close();
        });
        
        cancelBtn.setOnAction(e -> dialog.close());
        
        buttonBox.getChildren().addAll(confirmBtn, cancelBtn);
        content.getChildren().addAll(icon, messageLabel, buttonBox);
        
        Scene scene = new Scene(content);
        dialog.setScene(scene);
        dialog.showAndWait();
        
        return result.get();
    }
    
    /**
     * Show an information dialog
     */
    public static void showInfo(Stage ownerStage, String title, String message) {
        Stage dialog = createDialog(ownerStage, title, 400, 200);
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(30));
        
        Label icon = new Label("ℹ");
        icon.setStyle(
            "-fx-font-size: 48px;" +
            "-fx-text-fill: #2196F3;" +
            "-fx-font-weight: bold;"
        );
        
        Label messageLabel = new Label(message);
        messageLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #333;" +
            "-fx-wrap-text: true;" +
            "-fx-text-alignment: center;"
        );
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);
        
        Button okBtn = createButton("OK", "#2196F3");
        okBtn.setOnAction(e -> dialog.close());
        
        content.getChildren().addAll(icon, messageLabel, okBtn);
        
        Scene scene = new Scene(content);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    
    /**
     * Show an error dialog
     */
    public static void showError(Stage ownerStage, String title, String message) {
        Stage dialog = createDialog(ownerStage, title, 400, 200);
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(30));
        
        Label icon = new Label("✕");
        icon.setStyle(
            "-fx-font-size: 48px;" +
            "-fx-text-fill: #F44336;" +
            "-fx-font-weight: bold;"
        );
        
        Label messageLabel = new Label(message);
        messageLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #333;" +
            "-fx-wrap-text: true;" +
            "-fx-text-alignment: center;"
        );
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);
        
        Button okBtn = createButton("OK", "#F44336");
        okBtn.setOnAction(e -> dialog.close());
        
        content.getChildren().addAll(icon, messageLabel, okBtn);
        
        Scene scene = new Scene(content);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    
    /**
     * Show a warning dialog
     */
    public static void showWarning(Stage ownerStage, String title, String message) {
        Stage dialog = createDialog(ownerStage, title, 400, 200);
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(30));
        
        Label icon = new Label("⚠");
        icon.setStyle(
            "-fx-font-size: 48px;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-font-weight: bold;"
        );
        
        Label messageLabel = new Label(message);
        messageLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #333;" +
            "-fx-wrap-text: true;" +
            "-fx-text-alignment: center;"
        );
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);
        
        Button okBtn = createButton("OK", "#FF9800");
        okBtn.setOnAction(e -> dialog.close());
        
        content.getChildren().addAll(icon, messageLabel, okBtn);
        
        Scene scene = new Scene(content);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    
    /**
     * Show an input dialog
     */
    public static Optional<String> showInput(Stage ownerStage, String title, String message, String defaultValue) {
        AtomicReference<String> result = new AtomicReference<>(null);
        
        Stage dialog = createDialog(ownerStage, title, 400, 220);
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(30));
        
        Label messageLabel = new Label(message);
        messageLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #333;" +
            "-fx-wrap-text: true;"
        );
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);
        
        TextField inputField = new TextField(defaultValue);
        inputField.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-padding: 10;" +
            "-fx-border-color: #ddd;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;"
        );
        inputField.setMaxWidth(350);
        
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button okBtn = createButton("OK", "#2196F3");
        Button cancelBtn = createButton("Cancel", "#757575");
        
        okBtn.setOnAction(e -> {
            result.set(inputField.getText());
            dialog.close();
        });
        
        cancelBtn.setOnAction(e -> dialog.close());
        
        // Allow Enter key to submit
        inputField.setOnAction(e -> {
            result.set(inputField.getText());
            dialog.close();
        });
        
        buttonBox.getChildren().addAll(okBtn, cancelBtn);
        content.getChildren().addAll(messageLabel, inputField, buttonBox);
        
        Scene scene = new Scene(content);
        dialog.setScene(scene);
        
        // Focus input field
        inputField.requestFocus();
        inputField.selectAll();
        
        dialog.showAndWait();
        
        return Optional.ofNullable(result.get());
    }
    
    /**
     * Create a styled dialog stage
     */
    private static Stage createDialog(Stage owner, String title, double width, double height) {
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle(title);
        dialog.setWidth(width);
        dialog.setHeight(height);
        dialog.setResizable(false);
        
        // Center on owner
        dialog.setX(owner.getX() + owner.getWidth() / 2 - width / 2);
        dialog.setY(owner.getY() + owner.getHeight() / 2 - height / 2);
        
        return dialog;
    }
    
    /**
     * Create a styled button
     */
    private static Button createButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 30;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );
        
        // Hover effect
        btn.setOnMouseEntered(e -> btn.setOpacity(0.8));
        btn.setOnMouseExited(e -> btn.setOpacity(1.0));
        
        return btn;
    }
}
