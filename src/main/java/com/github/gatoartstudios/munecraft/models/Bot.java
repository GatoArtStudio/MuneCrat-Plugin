package com.github.gatoartstudios.munecraft.models;

public class Bot {
    private String name;
    private String token;
    private String clientId = null;
    private String clientSecret = null;

    public Bot() {
    }

    public Bot(String name, String token) {
        this.name = name;
        this.token = token;
    }

    public Bot(String name, String token, String clientId, String clientSecret) {
        this.name = name;
        this.token = token;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @Override
    public String toString() {
        return "Bot{" +
                "name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                '}';
    }
}
