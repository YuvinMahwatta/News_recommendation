package org.example.news_recommendation;

import java.time.LocalDateTime;

public class Articles {
    private String title;
    private String content;
    private String category;
    private double rating;
    private LocalDateTime timestamp;

    public Articles(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.rating = 0.0;
        this.timestamp = LocalDateTime.now();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getRating() {
        return rating;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getNewsDetails() {
        return "Title: " + title + ", Category: " + category + ", Rating: " + rating + ", Timestamp: " + timestamp;
    }

    public void updateRating(double newRating) {
        this.rating = newRating;
    }
}
