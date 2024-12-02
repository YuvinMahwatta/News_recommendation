package org.example.news_recommendation;

import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.bson.Document;
import org.json.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminMain implements Initializable {

    @FXML
    private TextField adminnewpwedit;

    @FXML
    private TextField admincurrentpwedit;

    @FXML
    private TextField admineditage;

    @FXML
    private Button admineditbckbtn;

    @FXML
    private Button admineditbtn;

    @FXML
    private TextField admineditemail;

    @FXML
    private TextField admineditname;

    @FXML
    private Pane admineditpane;

    @FXML
    private Button adminhomepagebtn;

    @FXML
    private Pane adminhomepagepane;

    @FXML
    private Button adminlogoutbtn;

    @FXML
    private Button adminnewsmanagebtn;

    @FXML
    private Label adminpageage;

    @FXML
    private Label adminpageemail;

    @FXML
    private Label adminpagename;

    @FXML
    private Label adminpageuname;

    @FXML
    private Button adminprofilebckbtn;

    @FXML
    private Button adminprofilebtn;

    @FXML
    private Pane adminprofilepane;

    @FXML
    private Button adminsavebtn;

    @FXML
    private Button adminusermanegbtn;

    @FXML
    private TableColumn<User, String> agecolumn;

    @FXML
    private TableColumn<User, String> categoriescolumn;

    @FXML
    private TableColumn<User, String> emailcolumn;

    @FXML
    private TableColumn<User, String> fullnamecolumn;

    @FXML
    private Pane usermanagepane;

    @FXML
    private TableView<User> usermanagetable;

    @FXML
    private TableColumn<User, String> usernamecolumn;

    @FXML
    private Button userdeletebtn;

    @FXML
    private TextArea articlecontent;

    @FXML
    private TextField articletitle;

    @FXML
    private Pane newsmanagepane;

    @FXML
    private Button addarticlesbtn;

    @FXML
    private Pane addnewspane;

    @FXML
    private Button addnewsbackbtn;

    @FXML
    private Pane articledeletepane;

    @FXML
    private TableColumn<Document, String> contentdel;

    @FXML
    private TableColumn<Document, String> titledel;

    @FXML
    private TableView<Document> deletearticlestable;

    @FXML
    private Button deleteNewsarticlesbtn;

    @FXML
    private Button deletearticlesbtn;

    private MongoDatabase database;


    public AdminMain() {
        // Connect to MongoDB (Make sure MongoDB is running)
        this.database = MongoClients.create("mongodb://localhost:27017").getDatabase("News_recommendation_system");
    }

    String currentUsername = HelloController.currentUsername;

    // Method to fetch user data by username from MongoDB
    private Optional<Document> getUserByUsername(String username) {
        MongoCollection<Document> collection = database.getCollection("users");
        Document query = new Document("username", username);
        Document userDoc = collection.find(query).first(); // Returns the first matching user
        return Optional.ofNullable(userDoc); // Return the user document (or empty if not found)
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        loadUserProfile();
        LoadArticles();

        usernamecolumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        fullnamecolumn.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        agecolumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        emailcolumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // For interests (categories), we'll join the interests list into a single string for display
        categoriescolumn.setCellValueFactory(cellData -> {
            List<String> interests = cellData.getValue().getInterests();
            return new SimpleStringProperty(String.join(", ", interests));
        });


    }

    private MongoDatabase getDatabase() {
        try {
            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
            return mongoClient.getDatabase("News_recommendation_system");
        } catch (Exception e) {
            System.err.println("Failed to connect to MongoDB: " + e.getMessage());
            return null;
        }
    }

    // Method to load and display user profile details in the labels
    private void loadUserProfile() {

        MongoDatabase database = getDatabase();
        MongoCollection<Document> collection = database.getCollection("Admin_full_Information");

        Document query = new Document("username", currentUsername);
        Document userDocument = collection.find(query).first();

        if (userDocument != null) {
            adminpagename.setText(userDocument.getString("fullname"));
            adminpageage.setText(String.valueOf(userDocument.getInteger("age")));
            adminpageemail.setText(userDocument.getString("email"));
            adminpageuname.setText(userDocument.getString("username"));


        }
    }

    public static void setCurrentUsername(String username) {

    }


    @FXML
    private void handleLogoutButton() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log-Out");
        alert.setHeaderText("Are you sure you want to Log-Out?");
        Optional<ButtonType> output = alert.showAndWait();

        if (output.isPresent() && output.get() == ButtonType.OK) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent signUpRoot = loader.load();

            Stage stage = (Stage) adminlogoutbtn.getScene().getWindow();
            stage.setScene(new Scene(signUpRoot));
            stage.show();
        }
    }

    @FXML
    void handleButtonActions2(ActionEvent event) {

        if (event.getSource() == adminprofilebckbtn) {
            adminhomepagepane.toFront();
        }
        if (event.getSource() == adminhomepagebtn) {
            adminhomepagepane.toFront();
        }
        if (event.getSource() == adminprofilebtn) {
            adminprofilepane.toFront();
        }
        if (event.getSource() == admineditbtn) {
            loadEditPane();
            admineditpane.toFront();
        }
        if (event.getSource() == admineditbckbtn) {
            adminprofilepane.toFront();
        }
        if (event.getSource() == adminusermanegbtn) {
            loadUserDetails();
            usermanagepane.toFront();
        }
        if (event.getSource() == userdeletebtn) {
            handleDeleteUser();
        }
        if (event.getSource() == adminnewsmanagebtn) {
            newsmanagepane.toFront();
        }
        if (event.getSource() == addarticlesbtn) {
            addnewspane.toFront();
        }
        if (event.getSource() == addnewsbackbtn) {
            newsmanagepane.toFront();
        }
        if (event.getSource() == deleteNewsarticlesbtn) {
            DeleteArticles ();
        }
        if (event.getSource() == deletearticlesbtn) {
            articledeletepane.toFront();
        }






    }

    public void loadUserDetails() {
        // Fetch data from MongoDB and add to the TableView
        MongoCollection<Document> userCollection = database.getCollection("User_full_details_table");
        MongoCursor<Document> cursor = userCollection.find().iterator();

        ObservableList<User> userList = FXCollections.observableArrayList();

        while (cursor.hasNext()) {
            Document doc = cursor.next();

            // Map the MongoDB document to a User object
            String fullname = doc.getString("fullname");
            int age = doc.getInteger("age");
            String username = doc.getString("username");
            String email = doc.getString("email");
            List<String> interests = (List<String>) doc.get("interests");

            User user = new User(fullname, age, username, email, interests);
            userList.add(user);
        }

        // Set the TableView items
        usermanagetable.setItems(userList);
    }

    @FXML
    private void handleDeleteUser() {
        // Get the selected user from the table
        User selectedUser = usermanagetable.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            // Display confirmation alert
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete User");
            confirmationAlert.setHeaderText("Are you sure you want to delete this user?");
            confirmationAlert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Delete the user from MongoDB
                MongoCollection<Document> userCollection = database.getCollection("User_full_details_table");
                Document query = new Document("username", selectedUser.getUsername());
                userCollection.deleteOne(query);

                // Refresh the TableView
                loadUserDetails();

                // Show success alert
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("User Deleted");
                successAlert.setHeaderText(null);
                successAlert.setContentText("User " + selectedUser.getFullname() + " has been successfully deleted.");
                successAlert.showAndWait();
            }
        } else {
            // Show error alert if no user is selected
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("No User Selected");
            errorAlert.setContentText("Please select a user from the table to delete.");
            errorAlert.showAndWait();
        }
    }

    private MongoClient mongoClient;
    private boolean validateLogin(String username, String password) {
        MongoDatabase database = mongoClient.getDatabase("News_recommendation_system");
        MongoCollection<Document> userCollection = database.getCollection("Admin_full_Information");
        Document userDoc = userCollection.find(new Document("username", username).append("password", password)).first();
        return userDoc!=null;
    }

    @FXML
    private void handleConfirmEditButtonClick2() {
        String fullName = admineditname.getText().trim();
        String currentpass = admincurrentpwedit.getText().trim();
        String password = adminnewpwedit.getText().trim();

        if (!validateLogin(username, currentpass)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Incorrect current password!");
            return;
        }

        // Validate the inputs
        if (fullName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Name cannot be empty.");
            return;
        }

        String ageText = admineditage.getText().trim();
        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age <= 0 || age > 120) { // Validate age range
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Age must be between 1 and 120.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Age must be a valid number.");
            return;
        }

        String email = admineditemail.getText().trim();
        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) { // Validate email format
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Enter a valid email address.");
            return;
        }



        // Confirm changes with the user
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to update your information?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Update the user document in MongoDB
            try {
                MongoCollection<Document> collection = database.getCollection("Admin_full_Information");

                Document updateFields = new Document();
                updateFields.put("fullname", fullName);
                updateFields.put("age", age);
                updateFields.put("email", email);
                updateFields.put("password", password);

                Document updateQuery = new Document("$set", updateFields);

                Document query = new Document("username", currentUsername);
                collection.updateOne(query, updateQuery);

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Information updated successfully.");

                currentUsername = username;
                // Reload the profile pane with updated data
                loadUserProfile();
                adminprofilepane.toFront();

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update information in the database.");
            }
        }
        admincurrentpwedit.setText("");
        adminnewpwedit.setText("");
    }

    String username= HelloController.currentUsername;


    @FXML
    private void loadEditPane() {
        // Fetch current user details
        MongoCollection<Document> collection = database.getCollection("Admin_full_Information");
        Document query = new Document("username", currentUsername);
        Document userDocument = collection.find(query).first();

        if (userDocument != null) {
            // Populate text fields with user details
            admineditname.setText(userDocument.getString("fullname"));
            admineditemail.setText(userDocument.getString("email"));
            admineditage.setText(String.valueOf(userDocument.getInteger("age")));

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
    private String uploadnewsarticles() {
        try {
            String title = articletitle.getText();
            String content = articlecontent.getText();

            // Check if title or content is empty
            if (title.trim().isEmpty() || content.trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please input Article title and Content.");
                return null; // Exit the method if the fields are empty
            }

            JSONObject Json = new JSONObject();
            Json.put("title", title);
            Json.put("content", content);

            URL url = new URL("http://127.0.0.1:5000/upload_article");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = Json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int response = connection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder responses = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        responses.append(inputLine);
                    }

                    // Clear the text fields after successful upload
                    articletitle.setText("");
                    articlecontent.setText("");

                    showAlert(Alert.AlertType.INFORMATION, "Success", "Article Added successfully.");
                    deletearticlestable.refresh();
                    LoadArticles();
                    return responses.toString();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Something went wrong.");
                return "Failed to upload articles. Response code:" + response;
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Something went wrong.");
            return "An error occurred while uploading the article";
        }
    }

    private void LoadArticles (){
        MongoDatabase database1 = mongoClient.getDatabase("News_recommendation_system");
        MongoCollection<Document> collection = database1.getCollection("News_Articles");

        List<Document> articles = collection.find().into(new ArrayList<>());
        ObservableList<Document> data = FXCollections.observableArrayList(articles);

        titledel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getString("title")));
        contentdel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getString("content")));
        deletearticlestable.setItems(data);
    }
    @FXML
    private void DeleteArticles() {
        // Get the selected article from the table
        Document selectedArticle = deletearticlestable.getSelectionModel().getSelectedItem();

        if (selectedArticle == null) {
            // Show an alert if no article is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select an article to delete.");
            alert.showAndWait();
            return;
        }

        // Connect to the MongoDB database
        MongoDatabase database1 = mongoClient.getDatabase("News_recommendation_system");
        MongoCollection<Document> collection = database1.getCollection("News_Articles");

        // Build a query using the article title
        Document query = new Document("title", selectedArticle.getString("title"));

        // Perform the deletion
        DeleteResult result = collection.deleteOne(query);

        // Check if the deletion was successful
        if (result.getDeletedCount() > 0) {
            // Remove the article from the TableView
            deletearticlestable.getItems().remove(selectedArticle);

            // Show a success alert
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Deletion Successful");
            successAlert.setHeaderText(null);
            successAlert.setContentText("The selected article has been deleted.");
            successAlert.showAndWait();
        } else {
            // Show an error alert if the deletion failed
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Deletion Failed");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Could not delete the selected article. Please try again.");
            errorAlert.showAndWait();
        }
    }



}














