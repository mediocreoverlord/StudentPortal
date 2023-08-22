package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {
    @FXML
    private Button button_register;

    @FXML
    private Button button_login;

    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_password;
    @FXML
    private TextField tf_studentid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button_register.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!tf_username.getText().trim().isEmpty() || tf_password.getText().trim().isEmpty() || tf_studentid.getText().trim().isEmpty()){
                    DBUtils.registerUser(event, tf_username.getText(), tf_password.getText(), Integer.parseInt(tf_studentid.getText()));
                } else{
                    System.out.println("please input all info");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all info to register");
                    alert.show();
                }
            }
        });

        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "sample.fxml", "Log-in", null, 0);
            }
        });
    }
}
