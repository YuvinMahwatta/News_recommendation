package org.example.news_recommendation;

import java.util.List;

public class User {
    private String fullname;
    private int age;
    private String username;
    private String email;
    private List<String> interests;



        // Constructor
        public User(String fullname, int age, String username, String email, List<String> interests) {
            this.fullname = fullname;
            this.age = age;
            this.username = username;
            this.email = email;
            this.interests = interests;
        }

        // Getters
        public String getFullname() {
            return fullname;
        }

        public int getAge() {
            return age;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public List<String> getInterests() {
            return interests;
        }
    }


