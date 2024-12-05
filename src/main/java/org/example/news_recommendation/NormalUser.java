package org.example.news_recommendation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.List;

public class NormalUser extends Person {
    private List<String> preferredCategories;
    private MongoDatabase database;

    public NormalUser(String username, String password, String email, List<String> preferredCategories, MongoDatabase database) {
        super(username, password, email);
        this.preferredCategories = preferredCategories;
        this.database = database;
    }

    public List<String> getPreferredCategories() {
        return preferredCategories;
    }

    public void setPreferredCategories(List<String> preferredCategories) {
        this.preferredCategories = preferredCategories;
    }

    public boolean login(String username, String password) {
        MongoCollection<Document> userCollection = database.getCollection("User_full_details_table");
        Document user = userCollection.find(new Document("username", username).append("password", password)).first();
        return user != null;
    }

    public boolean isDuplicateUsername(String username) {
        MongoCollection<Document> userCollection = database.getCollection("User_full_details_table");
        return userCollection.countDocuments(new Document("username", username)) > 0;
    }

    public void saveLoginDetails(String username) {
        MongoCollection<Document> loginDetailsCollection = database.getCollection("User_login_Information");
        Document loginRecord = new Document("username", username)
                .append("Login_time", LocalDateTime.now().toString());
        loginDetailsCollection.insertOne(loginRecord);
    }

    public boolean createAccount(String fullname, int age, String username, String email, String password, List<String> categories) {
        MongoCollection<Document> userCollection = database.getCollection("User_full_details_table");
        MongoCollection<Document> articlePointsCollection = database.getCollection("Article_Points");

        if (isDuplicateUsername(username)) {
            return false; // Username already exists
        }

        Document newUser = new Document("fullname", fullname)
                .append("age", age)
                .append("username", username)
                .append("email", email)
                .append("password", password)
                .append("interests", categories);
        userCollection.insertOne(newUser);

        Document userPoints = new Document("username", username);
        for (String category : new String[]{"Business & Finance", "Sports", "Technology", "World News", "Entertainment", "Health & Science"}) {
            userPoints.append(category, categories.contains(category) ? 10 : 0);
        }
        articlePointsCollection.insertOne(userPoints);
        return true;
    }

    @Override
    public void viewNews() {
        System.out.println("User is viewing preferred news.");
    }

    public void viewPreferredNews() {
        System.out.println("Displaying news for categories: " + preferredCategories);
    }

    public void rateNews(Articles news, int rating) {
        news.updateRating(rating);
        System.out.println("Rated news: " + news.getTitle() + " with " + rating + " stars.");
    }

    public void editProfile(String newEmail, String newPassword) {
        setEmail(newEmail);
        setPassword(newPassword);
        System.out.println("Profile updated.");
    }




}
