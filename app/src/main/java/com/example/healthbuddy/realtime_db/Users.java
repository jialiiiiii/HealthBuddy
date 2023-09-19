package com.example.healthbuddy.realtime_db;

public class Users {

    String tokenId, name, email;

    public Users() {
    }

    public Users(String tokenId, String name, String email) {
        this.tokenId = tokenId;
        this.name = name;
        this.email = email;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
