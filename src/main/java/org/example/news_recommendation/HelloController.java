package org.example.news_recommendation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import com.mongodb.client.MongoDatabase;
import org.example.news_recommendation.Database.DatabaseConnector;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML private Button adminloginbtn;
    @FXML private Pane adminloginpane;
    @FXML private TextField adminpassword;
    @FXML private Button adminsignupbtn;
    @FXML private TextField adminusername;
    @FXML private Pane createadminaccountpane;
    @FXML private Pane createuseraccountpane;
    @FXML private Button createuserbtn;
    @FXML private Button loginadminbtn;
    @FXML private Button loginuserbtn;
    @FXML private Pane openningpane;
    @FXML private TextField userageid;
    @FXML private TextField useremailid;
    @FXML private TextField userfullnameid;
    @FXML private Button userloginbtn;
    @FXML private Pane userloginpane;
    @FXML private TextField userpassword;
    @FXML private Button usersignupbtn;
    @FXML private TextField usersignupconfpassword;
    @FXML private TextField usersignuppassword;
    @FXML private TextField usersignupusern;
    @FXML private TextField userusername;
    @FXML private Button userloginbackbtn;
    @FXML private Button adminoginbackbtn;
    @FXML private Button createadminbackbtn;
    @FXML private Button creatuserbackbtn;
    @FXML private CheckBox buisnessbox;
    @FXML private CheckBox sportbox;
    @FXML private CheckBox technologybox;
    @FXML private CheckBox worldnewsbox;
    @FXML private CheckBox entertainmentbox;
    @FXML private CheckBox healthbox;

    private NormalUser normalUser; // Declare a NormalUser instance

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Get the database instance from DatabaseConnector
            MongoDatabase database = DatabaseConnector.getDatabase();

            // Initialize NormalUser instance
            normalUser = new NormalUser("", "", "", new ArrayList<>(), database);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Connection Error", "Could not connect to MongoDB.");
        }
    }

    @FXML
    private void handlesignin(ActionEvent event) throws IOException {
        String username = userusername.getText();
        String password = userpassword.getText();

        // Use NormalUser's login method
        if (normalUser.login(username, password)) {
            normalUser.saveLoginDetails(username);
            showAlert(Alert.AlertType.INFORMATION, "Login", "Welcome " + username);
            currentUsername = username;

            // Load Mainpage FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Mainpage.fxml"));
            Parent signUpRoot = loader.load();

            // Get the controller for the Mainpage
            Mainpage mainPageController = loader.getController();
            mainPageController.setCurrentUsername(username);

            // Set up the new scene
            Stage stage = (Stage) userloginbtn.getScene().getWindow();
            stage.setScene(new Scene(signUpRoot));
            stage.show();
        } else {
            showAlert(Alert.AlertType.ERROR, "Login", "Incorrect username or password");
        }
    }

    public static String currentUsername;

    @FXML
    private void loginAsAdmin(ActionEvent event) throws IOException {
        String username = adminusername.getText();
        String password = adminpassword.getText();

        if (checkCredentials2(username, password)) {
            currentUsername = username;
            saveAdminLoginDetails(username);
            showAlert(Alert.AlertType.INFORMATION, "Admin Login", "Welcome Admin " + username);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminMain.fxml"));
            Scene newScene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(newScene);
            stage.show();
        } else {
            showAlert(Alert.AlertType.ERROR, "Admin Login", "Incorrect username or password, or you do not have Admin access.");
        }
    }

    private boolean checkCredentials2(String username, String password) {
        try {
            // Find user with matching username and password
            MongoDatabase database = DatabaseConnector.getDatabase();
            Document user = database.getCollection("Admin_full_Information").find(new Document("username", username)
                    .append("password", password)).first();
            return user != null;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Login Error", "An error occurred while checking credentials.");
        }
        return false;
    }

    private void saveAdminLoginDetails(String username) {
        try {
            // Insert login record
            MongoDatabase database = DatabaseConnector.getDatabase();
            Document loginRecord = new Document("username", username)
                    .append("Login_time", LocalDateTime.now().toString());
            database.getCollection("Admin_login_information").insertOne(loginRecord);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not save login details.");
        }
    }

    @FXML
    private void CreateUser(ActionEvent event) {
        String fullname = userfullnameid.getText();
        String age = userageid.getText();
        String username = usersignupusern.getText();
        String email = useremailid.getText();
        String password = usersignuppassword.getText();
        String confirmpassword = usersignupconfpassword.getText();
        List<String> selectedCheckboxes = getcheckboxvalues();

        // Check if all required fields are filled
        if (fullname.isEmpty() || age.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "All fields must be filled out.");
            return;
        }

        // Validate email format
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return;
        }

        // Validate age as a number and within range
        int ageInt;
        try {
            ageInt = Integer.parseInt(age);
            if (ageInt < 0 || ageInt > 100) {
                showAlert(Alert.AlertType.ERROR, "Invalid Age", "Age must be between 0 and 100.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Age", "Age must be a number.");
            return;
        }

        // Validate that passwords match
        if (!password.equals(confirmpassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match.");
            return;
        }

        // Validate that at least one checkbox is selected
        if (selectedCheckboxes.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Preference Selection", "Please select at least one interest.");
            return;
        }

        // Use NormalUser's createAccount method
        if (!normalUser.createAccount(fullname, ageInt, username, email, password, selectedCheckboxes)) {
            showAlert(Alert.AlertType.ERROR, "Duplicate Username", "Username is already taken. Please choose another one.");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully!");
        resetUserSignupForm();
    }

    private void resetUserSignupForm() {
        userfullnameid.setText("");
        userageid.setText("");
        usersignupusern.setText("");
        useremailid.setText("");
        usersignuppassword.setText("");
        usersignupconfpassword.setText("");
        technologybox.setSelected(false);
        sportbox.setSelected(false);
        worldnewsbox.setSelected(false);
        entertainmentbox.setSelected(false);
        healthbox.setSelected(false);
    }

    private List<String> getcheckboxvalues() {
        List<String> selectedValues = new ArrayList<>();
        if (buisnessbox.isSelected()) selectedValues.add("Business & Finance");
        if (sportbox.isSelected()) selectedValues.add("Sports");
        if (technologybox.isSelected()) selectedValues.add("Technology");
        if (worldnewsbox.isSelected()) selectedValues.add("World News");
        if (entertainmentbox.isSelected()) selectedValues.add("Entertainment");
        if (healthbox.isSelected()) selectedValues.add("Health & Science");
        return selectedValues;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void PaneNav(ActionEvent actionEvent) {
        if (actionEvent.getSource() == loginuserbtn) {
            userloginpane.toFront();
        }
        if (actionEvent.getSource() == loginadminbtn) {
            adminloginpane.toFront();
        }
        if (actionEvent.getSource() == userloginbackbtn) {
            openningpane.toFront();
        }
        if (actionEvent.getSource() == adminoginbackbtn) {
            openningpane.toFront();
        }
        if (actionEvent.getSource() == createuserbtn) {
            createuseraccountpane.toFront();
        }
        if (actionEvent.getSource() == adminsignupbtn) {
            createadminaccountpane.toFront();
        }
        if (actionEvent.getSource() == createadminbackbtn) {
            adminloginpane.toFront();
        }
        if (actionEvent.getSource() == creatuserbackbtn) {
            userloginpane.toFront();
        }
    }
}