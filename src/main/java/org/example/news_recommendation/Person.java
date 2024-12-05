package org.example.news_recommendation;

public abstract class Person {
    private String username;
    private String password;
    private String email;
    private String fullname;
    private int age;

    public Person(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Person(String username, String password, String email, String fullname, int age) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullname = fullname;
        this.age = age;
    }

    public Person(String username, String email, String fullname, int age) {
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.age = age;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean login(String username, String password) {
        // Logic for login (e.g., validate credentials against MongoDB)
        System.out.println(username + " logged in.");
        return false;
    }

    public void signup(String username, String password, String email) {
        // Logic for signup (e.g., save user details in MongoDB)
        System.out.println(username + " signed up.");
    }

    public abstract void viewNews();
}
