package com.github.gatoartstudios.munecraft.models;

public class UserGuildDiscordModel {
    private Long id;
    private Long idDiscord;
    private Long guildId;
    private Long idRol;

    public UserGuildDiscordModel(Long id, Long idDiscord, Long guildId, Long idRol) {
        this.id = id;
        this.idDiscord = idDiscord;
        this.guildId = guildId;
        this.idRol = idRol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdDiscord() {
        return idDiscord;
    }

    public void setIdDiscord(Long idDiscord) {
        this.idDiscord = idDiscord;
    }

    public Long getGuildId() {
        return guildId;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }
}
