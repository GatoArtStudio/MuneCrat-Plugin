package com.github.gatoartstudios.munecraft.models;

public class UserDiscordModel {
    private Long idDiscord;
    private String discordName;
    private String password;
    private String minecraftName;

    public UserDiscordModel(Long idDiscord, String discordName, String password, String minecraftName) {
        this.idDiscord = idDiscord;
        this.discordName = discordName;
        this.password = password;
        this.minecraftName = minecraftName;
    }

    public Long getIdDiscord() {
        return idDiscord;
    }

    public void setIdDiscord(Long idDiscord) {
        this.idDiscord = idDiscord;
    }

    public String getDiscordName() {
        return discordName;
    }

    public void setDiscordName(String discordName) {
        this.discordName = discordName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMinecraftName() {
        return minecraftName;
    }

    public void setMinecraftName(String minecraftName) {
        this.minecraftName = minecraftName;
    }
}
