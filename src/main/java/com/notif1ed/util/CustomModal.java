package com.notif1ed.util;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.ParallelTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Modern Modal Dialog System with Backdrop
 * Provides styled, responsive modal dialogs with better UX than JavaFX Alert
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
        
        // Create dialog with backdrop
        Stage dialog = new Stage();
        dialog.initOwner(ownerStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setResizable(false);
        
        // Backdrop
        StackPane backdrop = new StackPane();
        backdrop.setStyle("-fx-background-color: rgba(0, 0, 0, 0.65);");
        
        // Content card - responsive sizing with improved spacing
        VBox content = new VBox(22);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40, 45, 40, 45));
        content.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 18;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 25, 0, 0, 8);"
        );
        content.setPrefWidth(440);
        content.setMaxWidth(440);
        
        // Icon with circular background
        StackPane iconContainer = new StackPane();
        VBox iconBg = new VBox();
        iconBg.setPrefSize(70, 70);
        iconBg.setMinSize(70, 70);
        iconBg.setMaxSize(70, 70);
        iconBg.setStyle(
            "-fx-background-color: #FFF3E0;" +
            "-fx-background-radius: 35;"
        );
        
        Label icon = new Label("⚠");
        icon.setStyle(
            "-fx-font-size: 42px;" +
            "-fx-text-fill: #FF9800;"
        );
        
        iconContainer.getChildren().addAll(iconBg, icon);
        iconContainer.setMaxHeight(70);
        
        // Title with better typography
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 22px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #212121;"
        );
        
        // Message with improved readability
        Label messageLabel = new Label(message);
        messageLabel.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-text-fill: #616161;" +
            "-fx-wrap-text: true;" +
            "-fx-text-alignment: center;" +
            "-fx-line-spacing: 2;"
        );
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(370);
        
        // Buttons with modern styling
        HBox buttonBox = new HBox(12);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button confirmBtn = createButton(confirmText, "#2196F3", 150);
        Button cancelBtn = createButton(cancelText, "#E0E0E0", 150, "#424242");
        
        confirmBtn.setOnAction(e -> {
            result.set(true);
            dialog.close();
        });
        
        cancelBtn.setOnAction(e -> dialog.close());
        
        buttonBox.getChildren().addAll(cancelBtn, confirmBtn);
        content.getChildren().addAll(iconContainer, titleLabel, messageLabel, buttonBox);
        
        backdrop.getChildren().add(content);
        StackPane.setAlignment(content, Pos.CENTER);
        
        Scene scene = new Scene(backdrop);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        
        // Position and size
        dialog.setX(ownerStage.getX());
        dialog.setY(ownerStage.getY());
        dialog.setWidth(ownerStage.getWidth());
        dialog.setHeight(ownerStage.getHeight());
        
        // Animation
        animateDialog(backdrop, content);
        
        // Click backdrop to close
        backdrop.setOnMouseClicked(e -> {
            if (e.getTarget() == backdrop) {
                dialog.close();
            }
        });
        
        dialog.showAndWait();
        
        return result.get();
    }
    
    /**
     * Show an information dialog
     */
    public static void showInfo(Stage ownerStage, String title, String message) {
        Stage dialog = new Stage();
        dialog.initOwner(ownerStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setResizable(false);
        
        StackPane backdrop = new StackPane();
        backdrop.setStyle("-fx-background-color: rgba(0, 0, 0, 0.65);");
        
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(35, 40, 35, 40));
        content.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 25, 0, 0, 8);"
        );
        content.setPrefWidth(400);
        content.setMaxWidth(400);
        
        Label icon = new Label("ℹ");
        icon.setStyle(
            "-fx-font-size: 50px;" +
            "-fx-text-fill: #2196F3;" +
            "-fx-effect: dropshadow(gaussian, rgba(33,150,243,0.3), 8, 0, 0, 3);"
        );
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #1a1a1a;"
        );
        
        Label messageLabel = new Label(message);
        messageLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #424242;" +
            "-fx-wrap-text: true;" +
            "-fx-text-alignment: center;"
        );
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(330);
        
        Button okBtn = createButton("OK", "#2196F3", 150);
        okBtn.setOnAction(e -> dialog.close());
        
        content.getChildren().addAll(icon, titleLabel, messageLabel, okBtn);
        backdrop.getChildren().add(content);
        StackPane.setAlignment(content, Pos.CENTER);
        
        Scene scene = new Scene(backdrop);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        
        dialog.setX(ownerStage.getX());
        dialog.setY(ownerStage.getY());
        dialog.setWidth(ownerStage.getWidth());
        dialog.setHeight(ownerStage.getHeight());
        
        animateDialog(backdrop, content);
        
        backdrop.setOnMouseClicked(e -> {
            if (e.getTarget() == backdrop) {
                dialog.close();
            }
        });
        
        dialog.showAndWait();
    }
    
    /**
     * Show an error dialog
     */
    public static void showError(Stage ownerStage, String title, String message) {
        Stage dialog = new Stage();
        dialog.initOwner(ownerStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setResizable(false);
        
        StackPane backdrop = new StackPane();
        backdrop.setStyle("-fx-background-color: rgba(0, 0, 0, 0.65);");
        
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(35, 40, 35, 40));
        content.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 25, 0, 0, 8);"
        );
        content.setPrefWidth(400);
        content.setMaxWidth(400);
        
        Label icon = new Label("✕");
        icon.setStyle(
            "-fx-font-size: 50px;" +
            "-fx-text-fill: #F44336;" +
            "-fx-effect: dropshadow(gaussian, rgba(244,67,54,0.3), 8, 0, 0, 3);"
        );
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #1a1a1a;"
        );
        
        Label messageLabel = new Label(message);
        messageLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #424242;" +
            "-fx-wrap-text: true;" +
            "-fx-text-alignment: center;"
        );
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(330);
        
        Button okBtn = createButton("OK", "#F44336", 150);
        okBtn.setOnAction(e -> dialog.close());
        
        content.getChildren().addAll(icon, titleLabel, messageLabel, okBtn);
        backdrop.getChildren().add(content);
        StackPane.setAlignment(content, Pos.CENTER);
        
        Scene scene = new Scene(backdrop);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        
        dialog.setX(ownerStage.getX());
        dialog.setY(ownerStage.getY());
        dialog.setWidth(ownerStage.getWidth());
        dialog.setHeight(ownerStage.getHeight());
        
        animateDialog(backdrop, content);
        
        backdrop.setOnMouseClicked(e -> {
            if (e.getTarget() == backdrop) {
                dialog.close();
            }
        });
        
        dialog.showAndWait();
    }
    
    /**
     * Show a warning dialog
     */
    public static void showWarning(Stage ownerStage, String title, String message) {
        Stage dialog = new Stage();
        dialog.initOwner(ownerStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setResizable(false);
        
        StackPane backdrop = new StackPane();
        backdrop.setStyle("-fx-background-color: rgba(0, 0, 0, 0.65);");
        
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(35, 40, 35, 40));
        content.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 25, 0, 0, 8);"
        );
        content.setPrefWidth(400);
        content.setMaxWidth(400);
        
        Label icon = new Label("⚠");
        icon.setStyle(
            "-fx-font-size: 50px;" +
            "-fx-text-fill: #FF9800;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.3), 8, 0, 0, 3);"
        );
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #1a1a1a;"
        );
        
        Label messageLabel = new Label(message);
        messageLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #424242;" +
            "-fx-wrap-text: true;" +
            "-fx-text-alignment: center;"
        );
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(330);
        
        Button okBtn = createButton("OK", "#FF9800", 150);
        okBtn.setOnAction(e -> dialog.close());
        
        content.getChildren().addAll(icon, titleLabel, messageLabel, okBtn);
        backdrop.getChildren().add(content);
        StackPane.setAlignment(content, Pos.CENTER);
        
        Scene scene = new Scene(backdrop);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        
        dialog.setX(ownerStage.getX());
        dialog.setY(ownerStage.getY());
        dialog.setWidth(ownerStage.getWidth());
        dialog.setHeight(ownerStage.getHeight());
        
        animateDialog(backdrop, content);
        
        backdrop.setOnMouseClicked(e -> {
            if (e.getTarget() == backdrop) {
                dialog.close();
            }
        });
        
        dialog.showAndWait();
    }
    
    /**
     * Show an input dialog
     */
    public static Optional<String> showInput(Stage ownerStage, String title, String message, String defaultValue) {
        AtomicReference<String> result = new AtomicReference<>(null);
        
        Stage dialog = new Stage();
        dialog.initOwner(ownerStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setResizable(false);
        
        StackPane backdrop = new StackPane();
        backdrop.setStyle("-fx-background-color: rgba(0, 0, 0, 0.65);");
        
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(35, 40, 35, 40));
        content.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 25, 0, 0, 8);"
        );
        content.setPrefWidth(420);
        content.setMaxWidth(420);
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #1a1a1a;"
        );
        
        Label messageLabel = new Label(message);
        messageLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #424242;" +
            "-fx-wrap-text: true;" +
            "-fx-text-alignment: center;"
        );
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);
        
        TextField inputField = new TextField(defaultValue);
        inputField.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-padding: 10;" +
            "-fx-border-color: #2196F3;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;"
        );
        inputField.setPrefWidth(350);
        inputField.setMaxWidth(350);
        
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button okBtn = createButton("OK", "#2196F3", 130);
        Button cancelBtn = createButton("Cancel", "#757575", 130);
        
        okBtn.setOnAction(e -> {
            result.set(inputField.getText());
            dialog.close();
        });
        
        cancelBtn.setOnAction(e -> dialog.close());
        
        inputField.setOnAction(e -> {
            result.set(inputField.getText());
            dialog.close();
        });
        
        buttonBox.getChildren().addAll(okBtn, cancelBtn);
        content.getChildren().addAll(titleLabel, messageLabel, inputField, buttonBox);
        backdrop.getChildren().add(content);
        StackPane.setAlignment(content, Pos.CENTER);
        
        Scene scene = new Scene(backdrop);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        
        dialog.setX(ownerStage.getX());
        dialog.setY(ownerStage.getY());
        dialog.setWidth(ownerStage.getWidth());
        dialog.setHeight(ownerStage.getHeight());
        
        animateDialog(backdrop, content);
        
        backdrop.setOnMouseClicked(e -> {
            if (e.getTarget() == backdrop) {
                dialog.close();
            }
        });
        
        inputField.requestFocus();
        inputField.selectAll();
        
        dialog.showAndWait();
        
        return Optional.ofNullable(result.get());
    }
    
    /**
     * Create a styled button with specific width
     */
    private static Button createButton(String text, String color, double width) {
        return createButton(text, color, width, "white");
    }
    
    /**
     * Create a styled button with specific width and text color
     */
    private static Button createButton(String text, String bgColor, double width, String textColor) {
        Button btn = new Button(text);
        btn.setPrefWidth(width);
        btn.setStyle(
            "-fx-background-color: " + bgColor + ";" +
            "-fx-text-fill: " + textColor + ";" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 14 20;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 2);"
        );
        
        btn.setOnMouseEntered(e -> {
            btn.setOpacity(0.85);
            btn.setScaleX(1.02);
            btn.setScaleY(1.02);
        });
        btn.setOnMouseExited(e -> {
            btn.setOpacity(1.0);
            btn.setScaleX(1.0);
            btn.setScaleY(1.0);
        });
        
        return btn;
    }
    
    /**
     * Animate dialog entrance
     */
    private static void animateDialog(StackPane backdrop, VBox content) {
        backdrop.setOpacity(0);
        content.setScaleX(0.85);
        content.setScaleY(0.85);
        content.setOpacity(0);
        
        FadeTransition backdropFade = new FadeTransition(Duration.millis(200), backdrop);
        backdropFade.setFromValue(0);
        backdropFade.setToValue(1);
        
        FadeTransition contentFade = new FadeTransition(Duration.millis(300), content);
        contentFade.setFromValue(0);
        contentFade.setToValue(1);
        
        ScaleTransition contentScale = new ScaleTransition(Duration.millis(300), content);
        contentScale.setFromX(0.85);
        contentScale.setFromY(0.85);
        contentScale.setToX(1.0);
        contentScale.setToY(1.0);
        
        ParallelTransition showAnimation = new ParallelTransition(
            backdropFade,
            contentFade,
            contentScale
        );
        
        showAnimation.play();
    }
}
