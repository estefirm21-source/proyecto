package com.greencert.db.model;

public class User {
    private Integer id;
    private String username;
    private String password;
    private String companyName;

    public User() {}

    public User(String username, String password, String companyName) {
        this.username = username;
        this.password = password;
        this.companyName = companyName;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}
