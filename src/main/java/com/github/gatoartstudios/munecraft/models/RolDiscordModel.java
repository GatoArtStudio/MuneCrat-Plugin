package com.github.gatoartstudios.munecraft.models;

public class RolDiscordModel {
    private long idRol;
    private String name;
    private long guildId;
    private Boolean isAdmin = false;
    private Boolean isBuilder = false;
    private Boolean isMod = false;
    private Boolean isDevelopment = false;
    private Boolean isHelper = false;
    private Boolean isVip = false;
    private Boolean isBooter = false;
    private Boolean isVerified = false;

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

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean getBuilder() {
        return isBuilder;
    }

    public void setBuilder(Boolean builder) {
        isBuilder = builder;
    }

    public Boolean getMod() {
        return isMod;
    }

    public void setMod(Boolean mod) {
        isMod = mod;
    }

    public Boolean getDevelopment() {
        return isDevelopment;
    }

    public void setDevelopment(Boolean development) {
        isDevelopment = development;
    }

    public Boolean getHelper() {
        return isHelper;
    }

    public void setHelper(Boolean helper) {
        isHelper = helper;
    }

    public Boolean getVip() {
        return isVip;
    }

    public void setVip(Boolean vip) {
        isVip = vip;
    }

    public Boolean getBooter() {
        return isBooter;
    }

    public void setBooter(Boolean booter) {
        isBooter = booter;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }
}
