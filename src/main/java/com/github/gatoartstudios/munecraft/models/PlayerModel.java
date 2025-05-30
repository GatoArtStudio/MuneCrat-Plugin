package com.github.gatoartstudios.munecraft.models;

import java.util.UUID;
import java.time.LocalDateTime;

public class PlayerModel {
    private Long idPlayer;
    private UUID uuid = null;
    private String minecraftName;
    private String ip = null;
    private LocalDateTime loginAt = null;
    private LocalDateTime logoutAt = null;
    private boolean active = false;
    private String inventory = null;
    private String inventoryStaff = null;
    private String location = null;
    private boolean premium = false;
    private boolean modeStaff = false;
    private boolean modeStaffChat = false;

    public PlayerModel(String minecraftName) {
        this.minecraftName = minecraftName;
    }

    public PlayerModel(UUID uuid, String minecraftName) {
        this.uuid = uuid;
        this.minecraftName = minecraftName;
    }

    public PlayerModel(UUID uuid, String minecraftName, String ip) {
        this.uuid = uuid;
        this.minecraftName = minecraftName;
        this.ip = ip;
    }

    public PlayerModel(UUID uuid, String minecraftName, String ip, String inventory) {
        this.uuid = uuid;
        this.minecraftName = minecraftName;
        this.ip = ip;
        this.inventory = inventory;
    }

    public PlayerModel(UUID uuid, String minecraftName, String ip, LocalDateTime loginAt, LocalDateTime logoutAt, boolean active, String inventory, String inventoryStaff, String location, boolean premium, boolean modeStaff, boolean modeStaffChat) {
        this.uuid = uuid;
        this.minecraftName = minecraftName;
        this.ip = ip;
        this.loginAt = loginAt;
        this.logoutAt = logoutAt;
        this.active = active;
        this.inventory = inventory;
        this.inventoryStaff = inventoryStaff;
        this.location = location;
        this.premium = premium;
        this.modeStaff = modeStaff;
        this.modeStaffChat = modeStaffChat;
    }

    public PlayerModel(Long idPlayer, UUID uuid, String minecraftName, String ip, LocalDateTime loginAt, LocalDateTime logoutAt, boolean active, String inventory, String inventoryStaff, String location, boolean premium, boolean modeStaff, boolean modeStaffChat) {
        this.idPlayer = idPlayer;
        this.uuid = uuid;
        this.minecraftName = minecraftName;
        this.ip = ip;
        this.loginAt = loginAt;
        this.logoutAt = logoutAt;
        this.active = active;
        this.inventory = inventory;
        this.inventoryStaff = inventoryStaff;
        this.location = location;
        this.premium = premium;
        this.modeStaff = modeStaff;
        this.modeStaffChat = modeStaffChat;
    }


    public Long getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(Long idPlayer) {
        this.idPlayer = idPlayer;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getMinecraftName() {
        return minecraftName;
    }

    public void setMinecraftName(String minecraftName) {
        this.minecraftName = minecraftName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public LocalDateTime getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(LocalDateTime loginAt) {
        this.loginAt = loginAt;
    }

    public LocalDateTime getLogoutAt() {
        return logoutAt;
    }

    public void setLogoutAt(LocalDateTime logoutAt) {
        this.logoutAt = logoutAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getInventoryStaff() {
        return inventoryStaff;
    }

    public void setInventoryStaff(String inventoryStaff) {
        this.inventoryStaff = inventoryStaff;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public boolean isModeStaff() {
        return modeStaff;
    }

    public void setModeStaff(boolean modeStaff) {
        this.modeStaff = modeStaff;
    }

    public boolean isModeStaffChat() {
        return modeStaffChat;
    }

    public void setModeStaffChat(boolean modeStaffChat) {
        this.modeStaffChat = modeStaffChat;
    }
}
