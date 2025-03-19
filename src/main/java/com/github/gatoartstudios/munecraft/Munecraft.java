package com.github.gatoartstudios.munecraft;

import com.github.gatoartstudios.munecraft.config.ConfigManager;
import com.github.gatoartstudios.munecraft.core.commands.ServiceComman;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.services.DiscordBot;
import org.bukkit.plugin.java.JavaPlugin;

public final class Munecraft extends JavaPlugin {

    private ServiceComman serviceComman;
    private DiscordBot discordBot;
    private ConfigManager configManager;

    @Override
    public void onLoad() {
        LoggerCustom.info("Plugin has been loaded");
        LoggerCustom.info("Plugin version: " + getDescription().getVersion() + " by " + getDescription().getAuthors());

        configManager = new ConfigManager(this, "config.yml");
        serviceComman = new ServiceComman();
        discordBot = new DiscordBot(this);

        serviceComman.addService(discordBot);
    }

    @Override
    public void onEnable() {
        LoggerCustom.info("Plugin has been enabled");

        configManager.init();
        serviceComman.execute();
    }

    @Override
    public void onDisable() {
        LoggerCustom.warning("Plugin has been disabled");

        serviceComman.undo();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
