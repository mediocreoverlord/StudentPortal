package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button button_login;

    @FXML
    private Button button_register;
    @FXML
   private TextField tf_username;
    @FXML
    private TextField tf_password;
    @FXML
    private TextField tf_studentid;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              //  DBUtils.loginUser(event, tf_username.getText(), tf_password.getText(), Integer.parseInt(tf_studentid.getText()));
                if(!tf_studentid.getText().trim().isEmpty()){
                    DBUtils.loginUser(event, tf_username.getText(), tf_password.getText(), Integer.parseInt(tf_studentid.getText()));
                } else{
                    System.out.println("please input all info");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all info to login");
                    alert.show();
                }
            }
        });
        button_register.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"registration.fxml", "Sign Up", null, 0);
            }
        });

    }
}
