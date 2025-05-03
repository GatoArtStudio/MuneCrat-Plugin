package com.github.gatoartstudios.munecraft.models;

public class Discord {

    private long guildId = 0L;
    private long logChannelId = 0L;
    private long warningChannelId = 0L;
    private long announcementChannelId = 0L;
    private long sanctionChannelId = 0L;
    private long reportChannelId = 0L;
    private long messageChannelId = 0L;
    private long commandChannelId = 0L;
    private long alertChannelId = 0L;
    private long playerActivityChannelId = 0L;

    public Discord() {
    }

    public Discord(long guildId, long logChannelId, long warningChannelId, long announcementChannelId, long sanctionChannelId, long reportChannelId, long messageChannelId, long commandChannelId, long alertChannelId, long playerActivityChannelId) {
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

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public long getLogChannelId() {
        return logChannelId;
    }

    public void setLogChannelId(long logChannelId) {
        this.logChannelId = logChannelId;
    }

    public long getWarningChannelId() {
        return warningChannelId;
    }

    public void setWarningChannelId(long warningChannelId) {
        this.warningChannelId = warningChannelId;
    }

    public long getAnnouncementChannelId() {
        return announcementChannelId;
    }

    public void setAnnouncementChannelId(long announcementChannelId) {
        this.announcementChannelId = announcementChannelId;
    }

    public long getSanctionChannelId() {
        return sanctionChannelId;
    }

    public void setSanctionChannelId(long sanctionChannelId) {
        this.sanctionChannelId = sanctionChannelId;
    }

    public long getReportChannelId() {
        return reportChannelId;
    }

    public void setReportChannelId(long reportChannelId) {
        this.reportChannelId = reportChannelId;
    }

    public long getMessageChannelId() {
        return messageChannelId;
    }

    public void setMessageChannelId(long messageChannelId) {
        this.messageChannelId = messageChannelId;
    }

    public long getCommandChannelId() {
        return commandChannelId;
    }

    public void setCommandChannelId(long commandChannelId) {
        this.commandChannelId = commandChannelId;
    }

    public long getAlertChannelId() {
        return alertChannelId;
    }

    public void setAlertChannelId(long alertChannelId) {
        this.alertChannelId = alertChannelId;
    }

    public long getPlayerActivityChannelId() {
        return playerActivityChannelId;
    }

    public void setPlayerActivityChannelId(long playerActivityChannelId) {
        this.playerActivityChannelId = playerActivityChannelId;
    }
}
