package com.github.gatoartstudios.munecraft.models;

public class RolDiscordModel {
    private long idRol;
    private String name;
    private long guildId;
    private boolean admin = false;
    private boolean builder = false;
    private boolean mod = false;
    private boolean development = false;
    private boolean helper = false;
    private boolean vip = false;
    private boolean booter = false;
    private boolean verified = false;

    public RolDiscordModel() {
    }

    public RolDiscordModel(long idRol, String name, long guildId) {
        this.idRol = idRol;
        this.name = name;
        this.guildId = guildId;
    }

    public long getIdRol() {
        return idRol;
    }

    public void setIdRol(long idRol) {
        this.idRol = idRol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isBuilder() {
        return builder;
    }

    public void setBuilder(boolean builder) {
        this.builder = builder;
    }

    public boolean isMod() {
        return mod;
    }

    public void setMod(boolean mod) {
        this.mod = mod;
    }

    public boolean isDevelopment() {
        return development;
    }

    public void setDevelopment(boolean development) {
        this.development = development;
    }

    public boolean isHelper() {
        return helper;
    }

    public void setHelper(boolean helper) {
        this.helper = helper;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public boolean isBooter() {
        return booter;
    }

    public void setBooter(boolean booter) {
        this.booter = booter;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
