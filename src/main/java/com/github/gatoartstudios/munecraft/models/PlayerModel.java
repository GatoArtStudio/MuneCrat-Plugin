package com.github.gatoartstudios.munecraft.models;

import java.util.UUID;
import java.time.LocalDateTime;

public class PlayerModel {
    private UUID uuid;
    private String minecraftName;
    private String ip = null;
    private String inventory = null;
    private String inventoryStaff = null;
    private Boolean StaffMode = false;
    private Boolean isPremium = false;
    private LocalDateTime lastLogin = null;
    private Boolean staffChatMode = false;

    public PlayerModel(UUID uuid, String minecraftName, String ip, String inventory, String inventoryStaff) {
        this.uuid = uuid;
        this.minecraftName = minecraftName;
        this.ip = ip;
        this.inventory = inventory;
        this.inventoryStaff = inventoryStaff;
    }

    public PlayerModel(UUID uuid, String minecraftName, String ip, String inventory, String inventoryStaff, Boolean StaffMode) {
        this.uuid = uuid;
        this.minecraftName = minecraftName;
        this.ip = ip;
        this.inventory = inventory;
        this.inventoryStaff = inventoryStaff;
        this.StaffMode = StaffMode;
    }

    public PlayerModel(UUID uuid, String minecraftName, String ip, String inventory, String inventoryStaff, Boolean staffMode, Boolean isPremium) {
        this.uuid = uuid;
        this.minecraftName = minecraftName;
        this.ip = ip;
        this.inventory = inventory;
        this.inventoryStaff = inventoryStaff;
        StaffMode = staffMode;
        this.isPremium = isPremium;
    }

    public PlayerModel(UUID uuid, String minecraftName, String ip, String inventory, String inventoryStaff, Boolean staffMode, Boolean isPremium, LocalDateTime lastLogin) {
        this.uuid = uuid;
        this.minecraftName = minecraftName;
        this.ip = ip;
        this.inventory = inventory;
        this.inventoryStaff = inventoryStaff;
        this.StaffMode = staffMode;
        this.isPremium = isPremium;
        this.lastLogin = lastLogin;
    }

    public PlayerModel(UUID uuid, String minecraftName, String ip, String inventory, String inventoryStaff, Boolean staffMode, Boolean isPremium, LocalDateTime lastLogin, Boolean staffChatMode) {
        this.uuid = uuid;
        this.minecraftName = minecraftName;
        this.ip = ip;
        this.inventory = inventory;
        this.inventoryStaff = inventoryStaff;
        this.StaffMode = staffMode;
        this.isPremium = isPremium;
        this.lastLogin = lastLogin;
        this.staffChatMode = staffChatMode;
    }

    public PlayerModel() {
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

    public Boolean getStaffMode() {
        return StaffMode;
    }

    public void setStaffMode(Boolean staffMode) {
        StaffMode = staffMode;
    }

    public Boolean getPremium() {
        return isPremium;
    }

    public void setPremium(Boolean premium) {
        isPremium = premium;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Boolean getStaffChatMode() {
        return staffChatMode;
    }

    public void setStaffChatMode(Boolean staffChatMode) {
        this.staffChatMode = staffChatMode;
    }
}
