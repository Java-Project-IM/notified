package notif1ed;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLoginClick(ActionEvent event) {
        // You can add login validation here later
        System.out.println("Login clicked for: " + emailField.getText());
    }

    @FXML
    private void handleSignUpRedirect(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Notifyed - Sign Up");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
