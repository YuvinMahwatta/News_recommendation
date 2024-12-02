package org.example.news_recommendation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.mongodb.client.*;
import com.mongodb.ConnectionString;
import javafx.stage.Stage;
import org.bson.Document;


public class HelloController implements Initializable {

    @FXML
    private Button adminloginbtn;

    @FXML
    private Pane adminloginpane;

    @FXML
    private TextField adminpassword;

    @FXML
    private Button adminsignupbtn;

    @FXML
    private TextField adminusername;

    @FXML
    private Pane createadminaccountpane;

    @FXML
    private Pane createuseraccountpane;

    @FXML
    private Button createuserbtn;

    @FXML
    private Button loginadminbtn;

    @FXML
    private Button loginuserbtn;

    @FXML
    private Pane openningpane;

    @FXML
    private TextField userageid;

    @FXML
    private TextField useremailid;

    @FXML
    private TextField userfullnameid;

    @FXML
    private Button userloginbtn;

    @FXML
    private Pane userloginpane;

    @FXML
    private TextField userpassword;

    @FXML
    private Button usersignupbtn;

    @FXML
    private TextField usersignupconfpassword;

    @FXML
    private TextField usersignuppassword;

    @FXML
    private TextField usersignupusern;

    @FXML
    private TextField userusername;

    @FXML
    private Button userloginbackbtn;

    @FXML
    private Button adminoginbackbtn;

    @FXML
    private Button createadminbackbtn;

    @FXML
    private Button creatuserbackbtn;

    @FXML
    private CheckBox buisnessbox;

    @FXML
    private CheckBox sportbox;

    @FXML
    private CheckBox technologybox;

    @FXML
    private CheckBox worldnewsbox;

    @FXML
    private CheckBox entertainmentbox;

    @FXML
    private CheckBox healthbox;


    private MongoClient mongoClient;
    private MongoDatabase database;

    private MongoCollection<Document> userLoginDetailsCollection;

    private MongoCollection<Document> User_full_details_table;

    private MongoCollection<Document> Admin_full_Information;

    private MongoCollection<Document> AdminLoginInformationCollection;

    private MongoCollection<Document> Articlepointscollection;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Connect to MongoDB
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("News_recommendation_system");
            userLoginDetailsCollection = database.getCollection("User_login_Information");
            User_full_details_table = database.getCollection("User_full_details_table");
            Admin_full_Information =  database.getCollection("Admin_full_Information");
            AdminLoginInformationCollection =  database.getCollection("Admin_login_information");
            Articlepointscollection =  database.getCollection("Article_Points");


        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Connection Error", "Could not connect to MongoDB.");
        }
    }

    @FXML
    private void handlesignin(ActionEvent event) throws IOException {
        String username = userusername.getText();
        String password = userpassword.getText();

        if (checkCredentials(username, password)) {
            saveLoginDetails(username);
            showAlert(Alert.AlertType.INFORMATION, "Login", "Welcome " + username);
            currentUsername = username;

            // Load Mainpage FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Mainpage.fxml"));
            Parent signUpRoot = loader.load();

            // Get the controller for the Mainpage
            Mainpage mainPageController = loader.getController();

            Mainpage.setCurrentUsername(username);
            // Set up the new scene
            Stage stage = (Stage) userloginbtn.getScene().getWindow();
            stage.setScene(new Scene(signUpRoot));
            stage.show();
        } else {
            showAlert(Alert.AlertType.ERROR, "Login", "Incorrect username or password");
        }
    }

    public static String currentUsername;
    private boolean checkCredentials(String username, String password) {
        try {
            // Find user with matching username and password
            Document user = User_full_details_table.find(new Document("username", username)
                    .append("password", password)).first();
            return user != null;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Login Error", "An error occurred while checking credentials.");
        }
        return false;
    }
    private boolean checkCredentials2(String username, String password) {
        try {
            // Find user with matching username and password
            Document user =  Admin_full_Information.find(new Document("username", username)
                    .append("password", password)).first();
            return user != null;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Login Error", "An error occurred while checking credentials.");
        }
        return false;
    }

    private void saveLoginDetails(String username) {
        try {
            // Insert login record
            Document loginRecord = new Document("username", username)
                    .append("Login_time", LocalDateTime.now().toString());
            userLoginDetailsCollection.insertOne(loginRecord);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not save login details.");
        }
    }

    private void saveAdminLoginDetails(String username) {
        try {
            // Insert login record
            Document loginRecord = new Document("username", username)
                    .append("Login_time", LocalDateTime.now().toString());
            AdminLoginInformationCollection.insertOne(loginRecord);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not save login details.");
        }
    }



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
        if (fullname.isEmpty() || age.isEmpty() || username.isEmpty() ||
                email.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()) {
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

        // Check for duplicate username in MongoDB
        if (isUserDuplicate(username)) {
            showAlert(Alert.AlertType.ERROR, "Duplicate Username", "Username is already taken. Please choose another one.");
            return;
        }

        // Save the user to MongoDB if all validations pass
        Document newUser = new Document("fullname", fullname)
                .append("age", ageInt)
                .append("username", username)
                .append("email", email)
                .append("password", password) // Consider hashing passwords for security
                .append("interests", selectedCheckboxes);


        User_full_details_table.insertOne(newUser);

        //adding points according to the categories

        Document usercategories = new Document("username",username);
        for (String category : new String[]{"Business & Finance","Sports","Technology","World News","Entertainment","Health & Science"}) {
            if (selectedCheckboxes.contains(category)) {
                usercategories.append(category, 10);

            }else {
                usercategories.append(category, 0);
            }

        }

        Articlepointscollection.insertOne(usercategories);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully!");
    }

    private List<String> getcheckboxvalues() {
        List<String> selectedValues = new ArrayList<>();

        // Check each checkbox and add the corresponding word to the list if selected
        if (buisnessbox.isSelected()) selectedValues.add("Business & Finance");
        if (sportbox.isSelected()) selectedValues.add("Sports");
        if (technologybox.isSelected()) selectedValues.add("Technology ");
        if (worldnewsbox.isSelected()) selectedValues.add("World News");
        if (entertainmentbox.isSelected()) selectedValues.add("Entertainment");
        if (healthbox.isSelected()) selectedValues.add("Health & Science");

        return selectedValues;
    }

    private boolean isUserDuplicate(String username) {
        Document query = new Document("username", username);
        long count = User_full_details_table.countDocuments(query);
        return count > 0; // Returns true if a duplicate username is found
    }

}