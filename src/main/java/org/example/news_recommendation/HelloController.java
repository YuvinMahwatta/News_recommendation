package org.example.news_recommendation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class HelloController {

    @FXML
    private TextField passwordtextfield;

    @FXML
    private TextField usertextfield;

    @FXML
    void handlesignin(ActionEvent event) {
        String demouser = "username";
        String demopassword = "password";
        String username = usertextfield.getText();
        String password = passwordtextfield.getText();

        if (username.equals(demouser) && password.equals(demopassword)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login");
            alert.setHeaderText(null);
            alert.setContentText("Welcome " + username);
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect username or password, Please enter again");
            alert.showAndWait();


        }
    }
}





