package org.example.news_recommendation;

public class Admin extends Person {

    public Admin(String username, String password, String email) {
        super(username, password, email);
    }

    @Override
    public void viewNews() {
        // Logic to view all news in the system
        System.out.println("Admin is viewing all news.");
    }

    public void addNews(Articles news) {
        // Logic to add a new news article to the database
        System.out.println("News added: " + news.getTitle());
    }
}
