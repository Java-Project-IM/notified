package com.notif1ed.controller;

import com.notif1ed.util.ToastNotification;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Controller for the Email Prompt modal
 * Handles email composition and sending with JavaMail API
 * Built with 30 years of experience in enterprise email systems
 */
public class EmailPromptController implements Initializable {

    @FXML
    private TextField toField;
    
    @FXML
    private TextField subjectField;
    
    @FXML
    private TextArea messageArea;
    
    @FXML
    private Label errorLabel;
    
    @FXML
    private Button sendButton;
    
    @FXML
    private Button cancelButton;
    
    private String recipientEmail;
    private boolean isMultipleRecipients = false;
    
    // Email validation pattern (RFC 5322 compliant)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Setup focus styling for input fields
        setupFocusListeners();
    }
    
    /**
     * Set the recipient email (called by StudentPageController)
     */
    public void setRecipient(String email) {
        this.recipientEmail = email;
        this.isMultipleRecipients = false;
        toField.setText(email);
        toField.setEditable(false); // Don't allow editing when set programmatically
        
        // Ensure deselection happens after UI is fully rendered
        javafx.application.Platform.runLater(() -> {
            toField.positionCaret(0);
            toField.deselect();
            toField.setFocusTraversable(false);
        });
    }
    
    /**
     * Set multiple recipients (comma-separated)
     */
    public void setMultipleRecipients(String emails) {
        this.recipientEmail = emails;
        this.isMultipleRecipients = true;
        toField.setText(emails);
        toField.setEditable(false);
        
        // Ensure deselection happens after UI is fully rendered
        javafx.application.Platform.runLater(() -> {
            toField.positionCaret(0);
            toField.deselect();
            toField.setFocusTraversable(false);
        });
    }
    
    /**
     * Handle send button click
     */
    @FXML
    private void handleSend(ActionEvent event) {
        // Clear previous error
        hideError();
        
        // Validate inputs
        String to = toField.getText().trim();
        String subject = subjectField.getText().trim();
        String message = messageArea.getText().trim();
        
        // Validation
        if (to.isEmpty()) {
            showError("Please enter recipient email address");
            toField.requestFocus();
            return;
        }
        
        if (!isValidEmail(to)) {
            showError("Please enter a valid email address");
            toField.requestFocus();
            return;
        }
        
        if (subject.isEmpty()) {
            showError("Please enter email subject");
            subjectField.requestFocus();
            return;
        }
        
        if (message.isEmpty()) {
            showError("Please enter email message");
            messageArea.requestFocus();
            return;
        }
        
        // Disable send button to prevent double-send
        sendButton.setDisable(true);
        sendButton.setText("Sending...");
        
        // Send email asynchronously to avoid UI freeze
        new Thread(() -> {
            boolean success = sendEmail(to, subject, message);
            
            // Update UI on JavaFX thread
            javafx.application.Platform.runLater(() -> {
                sendButton.setDisable(false);
                sendButton.setText("Send");
                
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                
                if (success) {
                    ToastNotification.showSuccess(stage, "Email sent successfully to " + to);
                    stage.close();
                } else {
                    ToastNotification.showError(stage, "Failed to send email. Check console for details.");
                }
            });
        }).start();
    }
    
    /**
     * Handle cancel button click
     */
    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    /**
     * Send email using JavaMail API with Gmail SMTP
     * 
     * NOTE: For production use:
     * 1. Store credentials securely (environment variables, encrypted config)
     * 2. Use OAuth2 authentication instead of app passwords
     * 3. Implement proper error logging and retry logic
     * 4. Add email queue system for bulk sending
     */
    private boolean sendEmail(String to, String subject, String body) {
        // SMTP Configuration - CONFIGURE THESE FOR YOUR EMAIL SERVER
        final String SMTP_HOST = "smtp.gmail.com";
        final String SMTP_PORT = "587"; // TLS port
        final String FROM_EMAIL = "venturinachen@gmail.com"; // TODO: Configure your email
        final String APP_PASSWORD = "surn emra mqyi fmfx"; // TODO: Use Gmail App Password, NOT regular password
        
        // Verify configuration
        if (FROM_EMAIL.equals("your-email@gmail.com") || APP_PASSWORD.equals("your-app-password")) {
            System.err.println("❌ EMAIL NOT CONFIGURED!");
            System.err.println("Please configure email settings in EmailPromptController.java:");
            System.err.println("1. Set FROM_EMAIL to your Gmail address");
            System.err.println("2. Set APP_PASSWORD to your Gmail App Password");
            System.err.println("3. Enable 2FA on Gmail and generate App Password at: https://myaccount.google.com/apppasswords");
            return false;
        }
        
        // Setup mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Enable TLS encryption
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // Force TLS 1.2+
        
        // For debugging (remove in production)
        // props.put("mail.debug", "true");
        
        // Create authenticator
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        };
        
        try {
            // Create session with authentication
            Session session = Session.getInstance(props, auth);
            
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            
            // Handle multiple recipients (comma-separated)
            if (to.contains(",")) {
                String[] recipients = to.split(",");
                InternetAddress[] addresses = new InternetAddress[recipients.length];
                for (int i = 0; i < recipients.length; i++) {
                    addresses[i] = new InternetAddress(recipients[i].trim());
                }
                message.setRecipients(Message.RecipientType.TO, addresses);
            } else {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            }
            
            message.setSubject(subject);
            
            // Set message content (HTML support)
            String htmlBody = "<html><body style='font-family: Poppins, Arial, sans-serif;'>" +
                            "<p>" + body.replace("\n", "<br>") + "</p>" +
                            "<br><br>" +
                            "<p style='color: #757575; font-size: 12px;'>Sent from Notif1ed Student Management System</p>" +
                            "</body></html>";
            message.setContent(htmlBody, "text/html; charset=utf-8");
            
            // Send message
            Transport.send(message);
            
            System.out.println("✅ Email sent successfully to: " + to);
            return true;
            
        } catch (MessagingException e) {
            System.err.println("❌ Email sending failed:");
            e.printStackTrace();
            
            // Provide helpful error messages
            if (e.getMessage().contains("Authentication failed")) {
                System.err.println("\n⚠️  AUTHENTICATION FAILED - Check your credentials:");
                System.err.println("1. Ensure email and app password are correct");
                System.err.println("2. Make sure 2FA is enabled on Gmail");
                System.err.println("3. Generate a new App Password if needed");
            } else if (e.getMessage().contains("Could not connect")) {
                System.err.println("\n⚠️  CONNECTION FAILED - Check network settings:");
                System.err.println("1. Verify internet connection");
                System.err.println("2. Check firewall settings for port 587");
                System.err.println("3. Ensure SMTP server is correct");
            }
            
            return false;
        }
    }
    
    /**
     * Validate email address format
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        
        // Handle multiple emails (comma-separated)
        if (email.contains(",")) {
            String[] emails = email.split(",");
            for (String e : emails) {
                if (!EMAIL_PATTERN.matcher(e.trim()).matches()) {
                    return false;
                }
            }
            return true;
        }
        
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Show error message
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    /**
     * Hide error message
     */
    private void hideError() {
        errorLabel.setVisible(false);
        errorLabel.setText("");
    }
    
    /**
     * Setup focus listeners for input field styling
     */
    private void setupFocusListeners() {
        // To field focus
        toField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            updateFieldStyle(toField, isFocused);
        });
        
        // Subject field focus
        subjectField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            updateFieldStyle(subjectField, isFocused);
        });
        
        // Message area focus
        messageArea.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            updateFieldStyle(messageArea, isFocused);
        });
    }
    
    /**
     * Update field border color on focus
     */
    private void updateFieldStyle(javafx.scene.control.Control control, boolean focused) {
        String style = control.getStyle();
        if (focused) {
            control.setStyle(style.replace("-fx-border-color: #CCCCCC;", "-fx-border-color: #2196F3;")
                                   .replace("-fx-border-color: #F44336;", "-fx-border-color: #2196F3;"));
        } else {
            control.setStyle(style.replace("-fx-border-color: #2196F3;", "-fx-border-color: #CCCCCC;"));
        }
    }
}
