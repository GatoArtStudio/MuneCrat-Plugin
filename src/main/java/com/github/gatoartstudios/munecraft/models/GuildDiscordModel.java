package com.github.gatoartstudios.munecraft.models;

public class GuildDiscordModel {
    private Long guildId = null;
    private Long logChannelId = null;
    private Long warningChannelId = null;
    private Long announcementChannelId = null;
    private Long sanctionChannelId = null;
    private Long reportChannelId = null;
    private Long messageChannelId = null;
    private Long commandChannelId = null;
    private Long alertChannelId = null;
    private Long playerActivityChannelId = null;

    public GuildDiscordModel(Long guildId, Long logChannelId, Long warningChannelId, Long announcementChannelId, Long sanctionChannelId, Long reportChannelId, Long messageChannelId, Long commandChannelId, Long alertChannelId, Long playerActivityChannelId) {
        this.guildId = guildId;
        this.logChannelId = logChannelId;
        this.warningChannelId = warningChannelId;
        this.announcementChannelId = announcementChannelId;
        this.sanctionChannelId = sanctionChannelId;
        this.reportChannelId = reportChannelId;
        this.messageChannelId = messageChannelId;
        this.commandChannelId = commandChannelId;
        this.alertChannelId = alertChannelId;
        this.playerActivityChannelId = playerActivityChannelId;
    }

    public GuildDiscordModel() {
    }

    public Long getGuildId() {
        return guildId;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public Long getLogChannelId() {
        return logChannelId;
    }

    public void setLogChannelId(Long logChannelId) {
        this.logChannelId = logChannelId;
    }

    public Long getWarningChannelId() {
        return warningChannelId;
    }

    public void setWarningChannelId(Long warningChannelId) {
        this.warningChannelId = warningChannelId;
    }

    public Long getAnnouncementChannelId() {
        return announcementChannelId;
    }

    public void setAnnouncementChannelId(Long announcementChannelId) {
        this.announcementChannelId = announcementChannelId;
    }

    public Long getSanctionChannelId() {
        return sanctionChannelId;
    }

    public void setSanctionChannelId(Long sanctionChannelId) {
        this.sanctionChannelId = sanctionChannelId;
    }

    public Long getReportChannelId() {
        return reportChannelId;
    }

    public void setReportChannelId(Long reportChannelId) {
        this.reportChannelId = reportChannelId;
    }

    public Long getMessageChannelId() {
        return messageChannelId;
    }

    public void setMessageChannelId(Long messageChannelId) {
        this.messageChannelId = messageChannelId;
    }

    public Long getCommandChannelId() {
        return commandChannelId;
    }

    public void setCommandChannelId(Long commandChannelId) {
        this.commandChannelId = commandChannelId;
    }

    public Long getAlertChannelId() {
        return alertChannelId;
    }

    public void setAlertChannelId(Long alertChannelId) {
        this.alertChannelId = alertChannelId;
    }

    public Long getPlayerActivityChannelId() {
        return playerActivityChannelId;
    }

    public void setPlayerActivityChannelId(Long playerActivityChannelId) {
        this.playerActivityChannelId = playerActivityChannelId;
    }
}
