package org.example.news_recommendation;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.bson.Document;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Mainpage implements Initializable {

    // FXML components for displaying user details
    @FXML
    private Label categoriesuser;

    @FXML
    private CheckBox editbuisnessbox;

    @FXML
    private CheckBox editentertainmentbox;

    @FXML
    private CheckBox edithealthbox;

    @FXML
    private CheckBox editsportbox;

    @FXML
    private CheckBox edittechnologybox;

    @FXML
    private CheckBox editworldnewsbox;

    @FXML
    private Label mainuserage;

    @FXML
    private Label mainuseremail;

    @FXML
    private Label mainusername;

    @FXML
    private Button recommendedbtn;

    @FXML
    private TextField usercurrentpwedit;


    @FXML
    private TextField usereditage;

    @FXML
    private Button usereditbckbtn;

    @FXML
    private Button usereditbtn;

    @FXML
    private TextField usereditemail;

    @FXML
    private TextField usereditname;

    @FXML
    private Pane usereditpane;

    @FXML
    private Button userhomepagebtn;

    @FXML
    private Pane userhomepagepane;

    @FXML
    private Button userlogoutbtn;

    @FXML
    private TextField usernewpwedit;

    @FXML
    private Button userprofilebckbtn;

    @FXML
    private Pane userprofilepane;

    @FXML
    private Button userrecommendbckbtn;

    @FXML
    private Pane userrecommendedpane;

    @FXML
    private Button usersavebtn;

    @FXML
    private Button yourprofilebtn;

    @FXML
    private Button allarticlesbckbtn;

    @FXML
    private Button allarticlesbtn;

    @FXML
    private Pane allarticlespane;

    @FXML
    private TableView<Document> allarticlestable;

    @FXML
    private TableColumn<Document, String> articlestitle;

    @FXML
    private TableView<Document> recommendedtable;

    @FXML
    private TableColumn<Document, String> rectitle;

    @FXML
    private TableColumn<Document, String> savcategory;

    @FXML
    private Button savedarticlesbtn;

    @FXML
    private Pane savedarticlespane;

    @FXML
    private TableView<Document> savedarticlestable;

    @FXML
    private TableColumn<Document, String> savtitle;

    @FXML
    private TableColumn<Document, String> reccategory;





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        loadUserProfile();
        loadarticles2();
        Recommended();
        saved();

    }


    // MongoDB connection setup
    private MongoDatabase database;

    private MongoClient mongoClient;


    public Mainpage() {
        // Connect to MongoDB (Make sure MongoDB is running)
        this.database = MongoClients.create("mongodb://localhost:27017").getDatabase("News_recommendation_system");
    }

    // Method to fetch user data by username from MongoDB
    private Optional<Document> getUserByUsername(String username) {
        MongoCollection<Document> collection = database.getCollection("users");
        Document query = new Document("username", username);
        Document userDoc = collection.find(query).first(); // Returns the first matching user
        return Optional.ofNullable(userDoc); // Return the user document (or empty if not found)
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
    static String currentUsername = HelloController.currentUsername;

    // Method to load and display user profile details in the labels
    private void loadUserProfile() {

        MongoDatabase database = getDatabase();
        MongoCollection<Document> collection = database.getCollection("User_full_details_table");

        Document query = new Document("username", currentUsername);
        Document userDocument = collection.find(query).first();

        if (userDocument != null) {
            mainusername.setText(userDocument.getString("fullname"));
            mainuserage.setText(String.valueOf(userDocument.getInteger("age")));
            mainuseremail.setText(userDocument.getString("email"));

            List<String> preferences = (List<String>) userDocument.get("interests");
            if (preferences != null) {
                categoriesuser.setText(String.join(", ", preferences));
            }
        } else {
            // If no user found, show an appropriate message (could be handled in UI as well)
            System.out.println("User not found");
            mainusername.setText("User not found");
            mainuserage.setText("N/A");
            mainuseremail.setText("N/A");
            categoriesuser.setText("N/A");
        }
    }

    public static void setCurrentUsername(String username) {

    }

    @FXML
    private void handleButtonActions(ActionEvent event) {
        if (event.getSource() == userprofilebckbtn) {
            userhomepagepane.toFront(); // Navigate to userhomepagepane
        }
        if (event.getSource() == yourprofilebtn) {
            // Load user details
            userprofilepane.toFront(); // Navigate to userprofilepane
        }
        if (event.getSource() == userhomepagebtn) {
            userhomepagepane.toFront();
        }
        if (event.getSource() == recommendedbtn) {
            userrecommendedpane.toFront();
        }
        if (event.getSource() == userrecommendbckbtn) {
            userhomepagepane.toFront();
        }
        if (event.getSource() == usereditbckbtn) {
            userprofilepane.toFront();
        }
        if (event.getSource() == usereditbtn) {
            loadEditPane(); // Load the current user's data into the edit pane
            usereditpane.toFront();
        }
        if (event.getSource() == allarticlesbtn) {
            allarticlespane.toFront();
        }
        if (event.getSource() == allarticlesbckbtn) {
            userhomepagepane.toFront();
        }
        if (event.getSource() == savedarticlesbtn) {
            savedarticlespane.toFront();
        }


    }

    @FXML
    private void handleLogoutButtonClick() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log-Out");
        alert.setHeaderText("Are you sure you want to Log-Out?");
        Optional<ButtonType> output = alert.showAndWait();

        if (output.isPresent() && output.get() == ButtonType.OK) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent signUpRoot = loader.load();

            Stage stage = (Stage) userlogoutbtn.getScene().getWindow();
            stage.setScene(new Scene(signUpRoot));
            stage.show();
        }
    }

    String username = HelloController.currentUsername;

    private void updatepoints(String category) {
        MongoCollection<Document> Articlepointscollection = database.getCollection("Article_Points");

        Articlepointscollection.updateOne(
                Filters.eq("username", HelloController.currentUsername),
                Updates.inc(category,2)
        );

    }

    private boolean validateLogin(String username, String password) {
        MongoDatabase database = mongoClient.getDatabase("News_recommendation_system");
        MongoCollection<Document> userCollection = database.getCollection("User_full_details_table");
        Document userDoc = userCollection.find(new Document("username", username).append("password", password)).first();
        return userDoc!=null;
    }

    @FXML
    private void handleConfirmEditButtonClick() {
        String fullName = usereditname.getText().trim();
        String currentpass = usercurrentpwedit.getText().trim();
        String password = usernewpwedit.getText().trim();

        if (!validateLogin(username, currentpass)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Incorrect current password!");
            return;
        }

        // Validate the inputs
        if (fullName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Name cannot be empty.");
            return;
        }

        String ageText = usereditage.getText().trim();
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

        String email = usereditemail.getText().trim();
        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) { // Validate email format
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Enter a valid email address.");
            return;
        }

        // Collect preferences
        List<String> updatedPreferences = new ArrayList<>();
        if (editbuisnessbox.isSelected()) updatedPreferences.add("Business & Finance");
        if (editentertainmentbox.isSelected()) updatedPreferences.add("Entertainment");
        if (edithealthbox.isSelected()) updatedPreferences.add("Health & Science");
        if (editsportbox.isSelected()) updatedPreferences.add("Sports");
        if (edittechnologybox.isSelected()) updatedPreferences.add("Technology");
        if (editworldnewsbox.isSelected()) updatedPreferences.add("World News");

        if (updatedPreferences.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "At least one preference must be selected.");
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
                MongoCollection<Document> collection = database.getCollection("User_full_details_table");

                // Find the original preferences
                Document query = new Document("username", currentUsername);
                Document userDocument = collection.find(query).first();
                List<String> originalPreferences = (List<String>) userDocument.get("interests");

                // Calculate newly selected and deselected categories
                List<String> newlySelected = new ArrayList<>(updatedPreferences);
                newlySelected.removeAll(originalPreferences);

                List<String> deselected = new ArrayList<>(originalPreferences);
                deselected.removeAll(updatedPreferences);

                // Update points for newly selected and deselected categories
                MongoCollection<Document> Articlepointscollection = database.getCollection("Article_Points");
                for (String category : newlySelected) {
                    Articlepointscollection.updateOne(
                            Filters.eq("username", currentUsername),
                            Updates.inc(category, 10)
                    );
                }
                for (String category : deselected) {
                    Articlepointscollection.updateOne(
                            Filters.eq("username", currentUsername),
                            Updates.inc(category, -10)
                    );
                }

                // Update user details
                Document updateFields = new Document();
                updateFields.put("fullname", fullName);
                updateFields.put("age", age);
                updateFields.put("email", email);
                updateFields.put("password", password);
                updateFields.put("interests", updatedPreferences);

                Document updateQuery = new Document("$set", updateFields);
                collection.updateOne(query, updateQuery);

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Information updated successfully.");

                currentUsername = username;
                // Reload the profile pane with updated data
                loadUserProfile();
                userprofilepane.toFront();

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update information in the database.");
            }
        }
        usercurrentpwedit.setText("");
        usernewpwedit.setText("");
    }

    @FXML
    private void loadEditPane() {
        // Fetch current user details
        MongoCollection<Document> collection = database.getCollection("User_full_details_table");
        Document query = new Document("username", currentUsername);
        Document userDocument = collection.find(query).first();

        if (userDocument != null) {
            // Populate text fields with user details
            usereditname.setText(userDocument.getString("fullname"));
            usereditemail.setText(userDocument.getString("email"));
            usereditage.setText(String.valueOf(userDocument.getInteger("age")));

            // Reset all checkboxes
            editbuisnessbox.setSelected(false);
            editentertainmentbox.setSelected(false);
            edithealthbox.setSelected(false);
            editsportbox.setSelected(false);
            edittechnologybox.setSelected(false);
            editworldnewsbox.setSelected(false);

            // Set checkboxes based on the user's interests
            List<String> preferences = (List<String>) userDocument.get("interests");
            if (preferences != null) {
                for (String preference : preferences) {
                    switch (preference.trim()) {
                        case "Business & Finance":
                            editbuisnessbox.setSelected(true);
                            break;
                        case "Entertainment":
                            editentertainmentbox.setSelected(true);
                            break;
                        case "Health & Science":
                            edithealthbox.setSelected(true);
                            break;
                        case "Sports":
                            editsportbox.setSelected(true);
                            break;
                        case "Technology":
                            edittechnologybox.setSelected(true);
                            break;
                        case "World News":
                            editworldnewsbox.setSelected(true);
                            break;
                    }
                }
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "User details could not be loaded.");
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadarticles2(){

        MongoDatabase database1 = mongoClient.getDatabase("News_recommendation_system");
        MongoCollection<Document> collection = database1.getCollection("News_Articles");

        List<Document> articles2 = collection.find().into(new ArrayList<>());
        ObservableList<Document> data2 = FXCollections.observableArrayList(articles2);

        articlestitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getString("title")));
        allarticlestable.setItems(data2);
    }

    @FXML
    private void onViewButtonClicked() {
        Document selectedArticle = allarticlestable.getSelectionModel().getSelectedItem();
        if (selectedArticle != null) {
            String title = selectedArticle.getString("title");
            String content = selectedArticle.getString("content");
            String category = selectedArticle.getString("category");

            // Load the WebView FXML
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/news_recommendation/Web.fxml"));
                Parent root = loader.load();

                // Get the controller of the WebView FXML
                WebViewController controller = loader.getController();


                // Pass the content to the WebViewController
                controller.displayContent(title, content, category);
                updatepoints(category);

                // Create and show the stage
                Stage stage = new Stage();
                stage.setTitle("Article View");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Helper method to get the points for a specific category from userArticlesCollection
    private int getCategoryPoints(String username, String category, MongoCollection<Document> userArticlesCollection) {
        Document userPointsDoc = userArticlesCollection.find(Filters.eq("username", username)).first();
        if (userPointsDoc != null) {
            return userPointsDoc.getInteger(category, 0);
        }
        return 0; // Return 0 if no points data exists
    }

    @FXML
    public void Recommended() {
        MongoDatabase database = mongoClient.getDatabase("News_recommendation_system");
        MongoCollection<Document> NewsArticleCollection = database.getCollection("News_Articles");
        MongoCollection<Document> Articlepointscollection = database.getCollection("Article_Points");

        // Get the current logged-in user's username
        String currentUsername = HelloController.currentUsername;

        // Retrieve the user's points from the userArticlesCollection
        Document Retrievepoints = Articlepointscollection.find(Filters.eq("username", currentUsername)).first();

        if (Retrievepoints == null) {
            System.out.println("User data not found.");
            return;
        }

        // Get the categories with points greater than 5
        List<String> preferredCategories = new ArrayList<>();
        for (String category : Retrievepoints.keySet()) {
            if (!category.equals("_id") && !category.equals("username")) {
                int points = Retrievepoints.getInteger(category, 0);
                if (points >= 10) {
                    preferredCategories.add(category);
                }
            }
        }

        if (preferredCategories.isEmpty()) {
            System.out.println("No preferred categories with points > 5.");
            return;
        }

        // Query the ArticleCollection for articles in the preferred categories
        List<Document> articles = NewsArticleCollection.find(Filters.in("category", preferredCategories))
                .into(new ArrayList<>());

        // Sort the articles based on the category points in descending order
        articles.sort((a1, a2) -> {
            int points1 = getCategoryPoints(currentUsername, a1.getString("category"), Articlepointscollection);
            int points2 = getCategoryPoints(currentUsername, a2.getString("category"), Articlepointscollection);
            return Integer.compare(points2, points1); // Sort in descending order
        });

        // Bind the filtered and sorted articles to the TableView
        ObservableList<Document> articleData = FXCollections.observableArrayList(articles);

        reccategory.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getString("category")));
        rectitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getString("title")));
        recommendedtable.setItems(articleData);
    }

    @FXML
    private void saved() {
        // Access the necessary collections
        MongoDatabase database = mongoClient.getDatabase("News_recommendation_system");
        MongoCollection<Document> NewsArticleCollection = database.getCollection("News_Articles");
        MongoCollection<Document> SavedarticlesCollection = database.getCollection("Saved_Articles");

        // Find the saved articles for the logged-in user
        Document userSaved = SavedarticlesCollection.find(Filters.eq("username", currentUsername)).first();

        if (userSaved != null && userSaved.containsKey("saved")) {
            // Retrieve the saved article titles
            List<String> savedTitles = userSaved.getList("saved", String.class);

            // Find the articles in the articles collection that match the saved titles
            List<Document> savedArticles = NewsArticleCollection.find(Filters.in("title", savedTitles)).into(new ArrayList<>());

            // Bind the data to the TableView
            ObservableList<Document> userData = FXCollections.observableArrayList(savedArticles);

            savcategory.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getString("category")));
            savtitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getString("title")));

            savedarticlestable.setItems(userData);
        } else {
            // If no saved articles, clear the table and notify the user
            savedarticlestable.setItems(FXCollections.observableArrayList());
            System.out.println("No saved articles found for user: " + currentUsername);
        }
    }





}