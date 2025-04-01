package com.github.gatoartstudios.munecraft.models;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class DiscordChannels {
    private TextChannel logChannel = null;
    private TextChannel warningChannel = null;
    private TextChannel announcementChannel = null;
    private TextChannel sanctionChannel = null;
    private TextChannel reportChannel = null;
    private TextChannel messageChannel = null;
    private TextChannel commandChannel = null;
    private TextChannel alertChannel = null;
    private TextChannel playerActivityChannel = null;

    public DiscordChannels() {
    }

    public DiscordChannels(TextChannel playerActivityChannel, TextChannel alertChannel, TextChannel commandChannel, TextChannel messageChannel, TextChannel reportChannel, TextChannel sanctionChannel, TextChannel announcementChannel, TextChannel warningChannel, TextChannel logChannel) {
        this.playerActivityChannel = playerActivityChannel;
        this.alertChannel = alertChannel;
        this.commandChannel = commandChannel;
        this.messageChannel = messageChannel;
        this.reportChannel = reportChannel;
        this.sanctionChannel = sanctionChannel;
        this.announcementChannel = announcementChannel;
        this.warningChannel = warningChannel;
        this.logChannel = logChannel;
    }

    public TextChannel getLogChannel() {
        return logChannel;
    }

    public void setLogChannel(TextChannel logChannel) {
        this.logChannel = logChannel;
    }

    public TextChannel getWarningChannel() {
        return warningChannel;
    }

    public void setWarningChannel(TextChannel warningChannel) {
        this.warningChannel = warningChannel;
    }

    public TextChannel getAnnouncementChannel() {
        return announcementChannel;
    }

    public void setAnnouncementChannel(TextChannel announcementChannel) {
        this.announcementChannel = announcementChannel;
    }

    public TextChannel getSanctionChannel() {
        return sanctionChannel;
    }

    public void setSanctionChannel(TextChannel sanctionChannel) {
        this.sanctionChannel = sanctionChannel;
    }

    public TextChannel getReportChannel() {
        return reportChannel;
    }

    public void setReportChannel(TextChannel reportChannel) {
        this.reportChannel = reportChannel;
    }

    public TextChannel getMessageChannel() {
        return messageChannel;
    }

    public void setMessageChannel(TextChannel messageChannel) {
        this.messageChannel = messageChannel;
    }

    public TextChannel getCommandChannel() {
        return commandChannel;
    }

    public void setCommandChannel(TextChannel commandChannel) {
        this.commandChannel = commandChannel;
    }

    public TextChannel getAlertChannel() {
        return alertChannel;
    }

    public void setAlertChannel(TextChannel alertChannel) {
        this.alertChannel = alertChannel;
    }

    public TextChannel getPlayerActivityChannel() {
        return playerActivityChannel;
    }

    public void setPlayerActivityChannel(TextChannel playerActivityChannel) {
        this.playerActivityChannel = playerActivityChannel;
    }
}
