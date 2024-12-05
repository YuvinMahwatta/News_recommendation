package org.example.news_recommendation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.bson.Document;
import org.example.news_recommendation.Database.DatabaseConnector;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WebViewController implements Initializable {
    @FXML private WebView webView;
    @FXML private Button dislikebtn;
    @FXML private Button likebtn;
    @FXML private Button ratesubmitbtn;
    @FXML private Button saveabtn;
    @FXML private ComboBox<String> ratecombobox;
    private String category;
    private String articletitle;
    String Username = Mainpage.currentUsername;

    private MongoDatabase database;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get the database instance from DatabaseConnector
        this.database = DatabaseConnector.getDatabase();
        ObservableList<String> rates = FXCollections.observableArrayList("*", "**", "***", "****", "*****");
        ratecombobox.setItems(rates);
    }

    public void displayContent(String title, String content, String category) {
        try {
            this.category = category;
            this.articletitle = title;
            // Load the HTML template
            File file = new File("src/main/resources/org/example/news_recommendation/index.html");
            String html = new String(Files.readAllBytes(file.toPath()));
            // Replace placeholders with title and content
            html = html.replace("<!-- title-placeholder -->", title)
                    .replace("<!-- content-placeholder -->", content);
            // Display the HTML content in the WebView
            WebEngine webEngine = webView.getEngine();
            webEngine.loadContent(html);
            checkArticleActions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkArticleActions() {
        MongoCollection<Document> savedarticlescollection = database.getCollection("Saved_Articles");
        Document savedarticlesdoc = savedarticlescollection.find(Filters.eq("username", Username)).first();
        if (savedarticlesdoc != null) {
            List<String> likedArticles = savedarticlesdoc.getList("liked", String.class);
            List<String> dislikedArticles = savedarticlesdoc.getList("disliked", String.class);
            List<String> ratedArticles = savedarticlesdoc.getList("rated", String.class);
            List<String> savedArticles = savedarticlesdoc.getList("saved", String.class);
            if (likedArticles != null && likedArticles.contains(articletitle)) {
                likebtn.setDisable(true);
            }
            if (dislikedArticles != null && dislikedArticles.contains(articletitle)) {
                dislikebtn.setDisable(true);
            }
            if (ratedArticles != null && ratedArticles.contains(articletitle)) {
                ratecombobox.setDisable(true);
                ratesubmitbtn.setDisable(true);
            }
            if (savedArticles != null && savedArticles.contains(articletitle)) {
                saveabtn.setDisable(true);
            }
        }
    }

    private void savearticlehandle(String choicetype, String articletitle) {
        MongoCollection<Document> savedarticlescollection = database.getCollection("Saved_Articles");
        Document savedarticlesdoc = savedarticlescollection.find(Filters.eq("username", Username)).first();
        if (savedarticlesdoc == null) {
            savedarticlesdoc = new Document("username", Username)
                    .append("saved", new ArrayList<String>())
                    .append("liked", new ArrayList<String>())
                    .append("disliked", new ArrayList<String>())
                    .append("rated", new ArrayList<String>());
            savedarticlescollection.insertOne(savedarticlesdoc);
        }
        List<String> actionList = savedarticlesdoc.getList(choicetype, String.class);
        if (actionList != null && actionList.contains(articletitle)) {
            showAlert("Action Already Taken", "You have already performed this action on the article.");
            return;
        }
        savedarticlescollection.updateOne(
                Filters.eq("username", Username),
                Updates.addToSet(choicetype, articletitle)
        );
    }

    private void updatePoints(int points) {
        if (category == null || category.isEmpty()) {
            System.out.println("Category is not set");
            return;
        }
        MongoCollection<Document> pointsCollection = database.getCollection("Article_Points");
        pointsCollection.updateOne(
                Filters.eq("username", Username),
                Updates.inc(category, points)
        );
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void likehandle() {
        savearticlehandle("liked", articletitle);
        updatePoints(6);
        likebtn.setDisable(true);
        dislikebtn.setDisable(true);
        showAlert("Article Liked", "You liked the article: " + articletitle);
    }

    @FXML
    private void dislikehandle() {
        savearticlehandle("disliked", articletitle);
        updatePoints(-4);
        likebtn.setDisable(true);
        dislikebtn.setDisable(true);
        showAlert("Article Disliked", "You disliked the article: " + articletitle);
    }

    @FXML
    private void ratehandle() {
        String selectedRate = ratecombobox.getSelectionModel().getSelectedItem();
        if (selectedRate == null) {
            showAlert("No Rating Selected", "Please select a rating before submitting.");
            return;
        }
        int points = evaluatePoints(selectedRate);
        savearticlehandle("rated", articletitle);
        updatePoints(points);
        ratecombobox.setDisable(true);
        ratesubmitbtn.setDisable(true);
        showAlert("Article Rated", "You rated the article: " + articletitle + " with " + selectedRate);
    }

    private int evaluatePoints(String rate) {
        switch (rate) {
            case "*": return 1;
            case "**": return 2;
            case "***": return 3;
            case "****": return 4;
            case "*****": return 5;
            default: return 0;
        }
    }

    @FXML
    private void SavedArticles() {
        savearticlehandle("saved", articletitle);
        updatePoints(2);
        saveabtn.setDisable(true);
        showAlert("Article Saved", "You saved the article: " + articletitle);
    }
}