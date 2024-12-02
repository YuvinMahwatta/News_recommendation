package org.example.news_recommendation;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WebViewController implements Initializable {

    @FXML
    private WebView webView;

    @FXML
    private Button dislikebtn;

    @FXML
    private Button likebtn;

    @FXML
    private Button ratesubmitbtn;

    @FXML
    private Button saveabtn;

    private String category;

    private String articletitle;


    @FXML
    private ComboBox<String> ratecombobox;

    String Username = Mainpage.currentUsername;

    private MongoDatabase database = MongoClients.create("mongodb://localhost:27017").getDatabase("News_recommendation_system");

    public void initialize(URL url, ResourceBundle resourceBundle){
        ObservableList<String> rates = FXCollections.observableArrayList("*","**","***","****","*****");
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

            //userchoicelehandle(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updatepoints(int points){
        if (category == null || category.isEmpty()) {
            System.out.println("Category is not set");
            return;
        }

        MongoCollection<Document> Articlepointscollection = database.getCollection("Article_Points");

        Articlepointscollection.updateOne(
                Filters.eq("username",Username),
                Updates.inc(category, points)
        );


    }

    private void userchoicelehandle(String articletitle){

        MongoCollection<Document> savedarticlescollection = database.getCollection("Saved_Articles");

        Document savedarticlesdoc = savedarticlescollection.find(Filters.eq("username",Username)).first();

        if (savedarticlesdoc !=null) {

            if (savedarticlesdoc.getList("saved",String.class).contains(articletitle)){
                saveabtn.setDisable(true);
            }
        }
        if (savedarticlesdoc.getList("liked",String.class).contains(articletitle) ||
                    savedarticlesdoc.getList("disliked",String.class).contains(articletitle)) {
            likebtn.setDisable(true);
            dislikebtn.setDisable(true);
        }
        if (savedarticlesdoc.getList("rated",String.class).contains(articletitle)) {
            ratecombobox.setDisable(true);
            ratesubmitbtn.setDisable(true);

        }
    }

    private void savearticlehandle(String choicetype,String articletitle) {
        MongoCollection<Document> savedarticlescollection = database.getCollection("Saved_Articles");

        Document savedarticlesdoc = savedarticlescollection.find(Filters.eq("username",Username)).first();

        if (savedarticlesdoc == null) {

            savedarticlesdoc = new Document("username",Username)
                    .append("saved", new ArrayList<String>())
                    .append("liked", new ArrayList<String>())
                    .append("disliked", new ArrayList<String>())
                    .append("rated", new ArrayList<String>());
            savedarticlescollection.insertOne(savedarticlesdoc);
        }

        savedarticlescollection.updateOne(
                Filters.eq("username", Username),
                Updates.addToSet(choicetype,articletitle)
        );

        System.out.println("User" + Username + "" + choicetype + "the article" + articletitle);
    }
@FXML
    private void likehandle() {
        updatepoints(6);
        savearticlehandle("liked",articletitle);

        likebtn.setDisable(true);
        dislikebtn.setDisable(true);
    }
@FXML
    private void dislikehandle() {
        updatepoints(-4);
        savearticlehandle("disliked",articletitle);

        likebtn.setDisable(true);
        dislikebtn.setDisable(true);
    }

    @FXML
    private void ratehandle(){
        if (articletitle == null || articletitle.isEmpty()) {
            System.out.println("Please Select a article title");
            return;
        }

        String Selectrate = ratecombobox.getSelectionModel().getSelectedItem();
        if (Selectrate == null) {
            System.out.println("Please select a rating");
            return;
        }

        int point = Evaluatepoints(Selectrate);

        updatepoints(point);

        savearticlehandle("rated",articletitle);
    }


    private int Evaluatepoints(String rate) {
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
        savearticlehandle("saved",articletitle);
        updatepoints(2);
        saveabtn.setDisable(true);

    }
}


