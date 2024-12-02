module org.example.news_recommendation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;
    requires org.mongodb.driver.sync.client;
    requires org.json;
    requires javafx.web;


    opens org.example.news_recommendation to javafx.fxml;
    exports org.example.news_recommendation;
}