package org.example.news_recommendation.Database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DatabaseConnector {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "News_recommendation_system";
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    // Static block to initialize the database connection
    static {
        try {
            mongoClient = MongoClients.create(CONNECTION_STRING);
            database = mongoClient.getDatabase(DATABASE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not connect to MongoDB.");
        }
    }

    // Method to get the database instance
    public static MongoDatabase getDatabase() {
        return database;
    }
}