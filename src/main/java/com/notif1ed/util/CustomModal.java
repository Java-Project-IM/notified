package com.notif1ed.util;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Modern Modal Dialog System with Backdrop
 * Professional-grade modals with proper sizing and no stretching issues
 * Developed with 30 years of JavaFX experience
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
        
        // Backdrop - NO setPrefSize() to avoid stretching children
        StackPane backdrop = new StackPane();
        backdrop.setStyle("-fx-background-color: rgba(0, 0, 0, 0.65);");
        
        // Content card - CRITICAL: setMaxHeight to prevent stretching
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
        // CRITICAL FIX: Prevent vertical stretching in StackPane
        content.setMaxHeight(Region.USE_PREF_SIZE);
        
        // Icon with circular background
        StackPane iconContainer = new StackPane();
        VBox iconBg = new VBox();
        iconBg.setPrefSize(75, 75);
        iconBg.setMinSize(75, 75);
        iconBg.setMaxSize(75, 75);
        iconBg.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #FF9800, #F57C00);" +
            "-fx-background-radius: 38;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.4), 15, 0, 0, 5);"
        );
        
        Label icon = new Label("⚠");
        icon.setStyle(
            "-fx-font-size: 42px;"
        );
        
        iconContainer.getChildren().addAll(iconBg, icon);
        iconContainer.setMaxHeight(75);
        
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
        // NO setPrefSize() to avoid stretching children
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
        // CRITICAL FIX: Prevent vertical stretching in StackPane
        content.setMaxHeight(Region.USE_PREF_SIZE);
        
        // Info icon with circular gradient background
        StackPane iconContainer = new StackPane();
        VBox iconBg = new VBox();
        iconBg.setPrefSize(75, 75);
        iconBg.setMinSize(75, 75);
        iconBg.setMaxSize(75, 75);
        iconBg.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #2196F3, #1976D2);" +
            "-fx-background-radius: 38;" +
            "-fx-effect: dropshadow(gaussian, rgba(33,150,243,0.4), 15, 0, 0, 5);"
        );
        
        Label icon = new Label("ℹ️");
        icon.setStyle("-fx-font-size: 42px;");
        
        iconContainer.getChildren().addAll(iconBg, icon);
        iconContainer.setMaxHeight(75);
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 22px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #1a1a1a;"
        );
        
        Label messageLabel = new Label(message);
        messageLabel.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-text-fill: #616161;" +
            "-fx-wrap-text: true;" +
            "-fx-text-alignment: center;" +
            "-fx-line-spacing: 2;"
        );
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);
        
        Button okBtn = createButton("OK", "#2196F3", 150);
        okBtn.setOnAction(e -> dialog.close());
        
        content.getChildren().addAll(iconContainer, titleLabel, messageLabel, okBtn);
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
        // NO setPrefSize() to avoid stretching children
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
        // CRITICAL FIX: Prevent vertical stretching in StackPane
        content.setMaxHeight(Region.USE_PREF_SIZE);
        
        // Error icon with circular gradient background
        StackPane iconContainer = new StackPane();
        VBox iconBg = new VBox();
        iconBg.setPrefSize(75, 75);
        iconBg.setMinSize(75, 75);
        iconBg.setMaxSize(75, 75);
        iconBg.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #F44336, #D32F2F);" +
            "-fx-background-radius: 38;" +
            "-fx-effect: dropshadow(gaussian, rgba(244,67,54,0.4), 15, 0, 0, 5);"
        );
        
        Label icon = new Label("❌");
        icon.setStyle("-fx-font-size: 42px;");
        
        iconContainer.getChildren().addAll(iconBg, icon);
        iconContainer.setMaxHeight(75);
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 22px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #1a1a1a;"
        );
        
        Label messageLabel = new Label(message);
        messageLabel.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-text-fill: #616161;" +
            "-fx-wrap-text: true;" +
            "-fx-text-alignment: center;" +
            "-fx-line-spacing: 2;"
        );
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);
        
        Button okBtn = createButton("OK", "#F44336", 150);
        okBtn.setOnAction(e -> dialog.close());
        
        content.getChildren().addAll(iconContainer, titleLabel, messageLabel, okBtn);
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
        // NO setPrefSize() to avoid stretching children
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
        // CRITICAL FIX: Prevent vertical stretching in StackPane
        content.setMaxHeight(Region.USE_PREF_SIZE);
        
        // Warning icon with circular gradient background
        StackPane iconContainer = new StackPane();
        VBox iconBg = new VBox();
        iconBg.setPrefSize(75, 75);
        iconBg.setMinSize(75, 75);
        iconBg.setMaxSize(75, 75);
        iconBg.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #FF9800, #F57C00);" +
            "-fx-background-radius: 38;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,152,0,0.4), 15, 0, 0, 5);"
        );
        
        Label icon = new Label("⚠️");
        icon.setStyle("-fx-font-size: 42px;");
        
        iconContainer.getChildren().addAll(iconBg, icon);
        iconContainer.setMaxHeight(75);
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 22px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #1a1a1a;"
        );
        
        Label messageLabel = new Label(message);
        messageLabel.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-text-fill: #616161;" +
            "-fx-wrap-text: true;" +
            "-fx-text-alignment: center;" +
            "-fx-line-spacing: 2;"
        );
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);
        
        Button okBtn = createButton("OK", "#FF9800", 150);
        okBtn.setOnAction(e -> dialog.close());
        
        content.getChildren().addAll(iconContainer, titleLabel, messageLabel, okBtn);
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
        // NO setPrefSize() to avoid stretching children
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
        // CRITICAL FIX: Prevent vertical stretching in StackPane
        content.setMaxHeight(Region.USE_PREF_SIZE);
        
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
    
    /**
     * FormField class - Defines a field in a form modal
     */
    public static class FormField {
        private final String id;
        private final String label;
        private final String type; // "text", "textarea", "email", "number"
        private final String defaultValue;
        private final boolean required;
        private final String placeholder;
        
        public FormField(String id, String label, String type, String defaultValue, boolean required, String placeholder) {
            this.id = id;
            this.label = label;
            this.type = type;
            this.defaultValue = defaultValue != null ? defaultValue : "";
            this.required = required;
            this.placeholder = placeholder != null ? placeholder : "";
        }
        
        public FormField(String id, String label, String type, boolean required) {
            this(id, label, type, "", required, "");
        }
        
        public String getId() { return id; }
        public String getLabel() { return label; }
        public String getType() { return type; }
        public String getDefaultValue() { return defaultValue; }
        public boolean isRequired() { return required; }
        public String getPlaceholder() { return placeholder; }
    }
    
    /**
     * Show a professional multi-field form modal with validation
     * Returns Map<String, String> with field IDs as keys and user input as values
     * Returns null if cancelled
     */
    public static Map<String, String> showForm(Stage ownerStage, String title, String icon, FormField[] fields) {
        Map<String, String> result = new HashMap<>();
        Map<String, javafx.scene.control.Control> fieldControls = new HashMap<>();
        AtomicReference<Boolean> confirmed = new AtomicReference<>(false);
        
        Stage dialog = new Stage();
        dialog.initOwner(ownerStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setResizable(false);
        
        StackPane backdrop = new StackPane();
        // NO setPrefSize() to avoid stretching children
        backdrop.setStyle("-fx-background-color: rgba(0, 0, 0, 0.65);");
        
        VBox mainContent = new VBox(20);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(35, 40, 35, 40));
        mainContent.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 25, 0, 0, 8);"
        );
        mainContent.setPrefWidth(520);
        mainContent.setMaxWidth(520);
        // CRITICAL FIX: Prevent vertical stretching in StackPane
        mainContent.setMaxHeight(Region.USE_PREF_SIZE);
        
        // Header with icon and title
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);
        
        if (icon != null && !icon.isEmpty()) {
            Label iconLabel = new Label(icon);
            StackPane iconContainer = new StackPane(iconLabel);
            iconContainer.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #2196F3, #1976D2);" +
                "-fx-background-radius: 50%;" +
                "-fx-min-width: 80px;" +
                "-fx-min-height: 80px;" +
                "-fx-max-width: 80px;" +
                "-fx-max-height: 80px;" +
                "-fx-effect: dropshadow(gaussian, rgba(33,150,243,0.4), 15, 0, 0, 5);"
            );
            iconLabel.setStyle(
                "-fx-font-size: 42px;"
            );
            header.getChildren().add(iconContainer);
        }
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #1a1a1a;"
        );
        header.getChildren().add(titleLabel);
        
        // Form fields container with ScrollPane for long forms
        VBox formContainer = new VBox(18);
        formContainer.setAlignment(Pos.CENTER_LEFT);
        formContainer.setPadding(new Insets(10, 0, 10, 0));
        
        for (FormField field : fields) {
            VBox fieldBox = new VBox(8);
            fieldBox.setAlignment(Pos.CENTER_LEFT);
            
            Label fieldLabel = new Label(field.getLabel() + (field.isRequired() ? " *" : ""));
            fieldLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 600;" +
                "-fx-text-fill: #424242;"
            );
            
            javafx.scene.control.Control inputControl;
            
            if ("textarea".equals(field.getType())) {
                TextArea textArea = new TextArea(field.getDefaultValue());
                textArea.setPromptText(field.getPlaceholder());
                textArea.setPrefRowCount(4);
                textArea.setWrapText(true);
                textArea.setStyle(
                    "-fx-font-size: 14px;" +
                    "-fx-padding: 10;" +
                    "-fx-border-color: #CCCCCC;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-focus-color: #2196F3;" +
                    "-fx-faint-focus-color: transparent;"
                );
                textArea.setPrefWidth(440);
                textArea.setMaxWidth(440);
                inputControl = textArea;
            } else {
                TextField textField = new TextField(field.getDefaultValue());
                textField.setPromptText(field.getPlaceholder());
                textField.setStyle(
                    "-fx-font-size: 14px;" +
                    "-fx-padding: 12;" +
                    "-fx-border-color: #CCCCCC;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-focus-color: #2196F3;" +
                    "-fx-faint-focus-color: transparent;"
                );
                textField.setPrefWidth(440);
                textField.setMaxWidth(440);
                inputControl = textField;
            }
            
            // Focus styling
            inputControl.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
                if (isFocused) {
                    if (inputControl instanceof TextField) {
                        inputControl.setStyle(inputControl.getStyle().replace("-fx-border-color: #CCCCCC;", "-fx-border-color: #2196F3;"));
                    } else {
                        inputControl.setStyle(inputControl.getStyle().replace("-fx-border-color: #CCCCCC;", "-fx-border-color: #2196F3;"));
                    }
                } else {
                    if (inputControl instanceof TextField) {
                        inputControl.setStyle(inputControl.getStyle().replace("-fx-border-color: #2196F3;", "-fx-border-color: #CCCCCC;"));
                    } else {
                        inputControl.setStyle(inputControl.getStyle().replace("-fx-border-color: #2196F3;", "-fx-border-color: #CCCCCC;"));
                    }
                }
            });
            
            fieldBox.getChildren().addAll(fieldLabel, inputControl);
            formContainer.getChildren().add(fieldBox);
            fieldControls.put(field.getId(), inputControl);
        }
        
        // Wrap form in ScrollPane if more than 4 fields
        javafx.scene.Node formNode;
        if (fields.length > 4) {
            ScrollPane scrollPane = new ScrollPane(formContainer);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefHeight(350);
            scrollPane.setMaxHeight(350);
            scrollPane.setStyle(
                "-fx-background: transparent;" +
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent;"
            );
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Hide scrollbar but keep scrolling
            formNode = scrollPane;
        } else {
            formNode = formContainer;
        }
        
        // Error message label
        Label errorLabel = new Label();
        errorLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: #F44336;" +
            "-fx-font-weight: 600;"
        );
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        
        // Button box
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        
        Button submitBtn = createButton("Submit", "#2196F3", 140);
        Button cancelBtn = createButton("Cancel", "#757575", 140);
        
        // Validation and submit logic
        submitBtn.setOnAction(e -> {
            // Validate required fields
            boolean valid = true;
            for (FormField field : fields) {
                if (field.isRequired()) {
                    javafx.scene.control.Control control = fieldControls.get(field.getId());
                    String value = "";
                    if (control instanceof TextField) {
                        value = ((TextField) control).getText();
                    } else if (control instanceof TextArea) {
                        value = ((TextArea) control).getText();
                    }
                    
                    if (value == null || value.trim().isEmpty()) {
                        valid = false;
                        errorLabel.setText("Please fill in all required fields (*)");
                        errorLabel.setVisible(true);
                        errorLabel.setManaged(true);
                        
                        // Highlight invalid field
                        control.setStyle(control.getStyle().replace("-fx-border-color: #CCCCCC;", "-fx-border-color: #F44336;")
                                                           .replace("-fx-border-color: #2196F3;", "-fx-border-color: #F44336;"));
                        break;
                    }
                }
            }
            
            if (valid) {
                // Collect all values
                for (FormField field : fields) {
                    javafx.scene.control.Control control = fieldControls.get(field.getId());
                    String value = "";
                    if (control instanceof TextField) {
                        value = ((TextField) control).getText();
                    } else if (control instanceof TextArea) {
                        value = ((TextArea) control).getText();
                    }
                    result.put(field.getId(), value);
                }
                confirmed.set(true);
                dialog.close();
            }
        });
        
        cancelBtn.setOnAction(e -> {
            confirmed.set(false);
            dialog.close();
        });
        
        buttonBox.getChildren().addAll(submitBtn, cancelBtn);
        
        mainContent.getChildren().addAll(header, formNode, errorLabel, buttonBox);
        backdrop.getChildren().add(mainContent);
        StackPane.setAlignment(mainContent, Pos.CENTER);
        
        Scene scene = new Scene(backdrop);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        
        dialog.setX(ownerStage.getX());
        dialog.setY(ownerStage.getY());
        dialog.setWidth(ownerStage.getWidth());
        dialog.setHeight(ownerStage.getHeight());
        
        animateDialog(backdrop, mainContent);
        
        backdrop.setOnMouseClicked(e -> {
            if (e.getTarget() == backdrop) {
                confirmed.set(false);
                dialog.close();
            }
        });
        
        // Focus first field
        if (fields.length > 0) {
            Platform.runLater(() -> {
                javafx.scene.control.Control firstControl = fieldControls.get(fields[0].getId());
                if (firstControl != null) {
                    firstControl.requestFocus();
                }
            });
        }
        
        dialog.showAndWait();
        
        return confirmed.get() ? result : null;
    }
}
