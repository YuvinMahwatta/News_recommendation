package org.example.news_recommendation;

import java.util.List;

public class User extends Person {

    private List<String> interests;



        // Constructor
        public User(String fullname, int age, String username, String email, List<String> interests) {
            super(username,email,fullname,age);
            this.interests = interests;
        }

        public List<String> getInterests() {
            return interests;
        }

    @Override
    public void viewNews() {

    }
}


