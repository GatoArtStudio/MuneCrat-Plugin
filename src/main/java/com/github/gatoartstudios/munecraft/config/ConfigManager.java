package com.github.gatoartstudios.munecraft.config;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.core.abstracts.Config;
import com.github.gatoartstudios.munecraft.models.Bot;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager extends Config {
    private static FileConfiguration config;
    private Bot bot;

    public ConfigManager(Munecraft plugin, String fileName) {
        super(plugin, fileName);
    }

    @Override
    public void init() {
        saveDefaults();
        load();
    }

    @Override
    public void load() {
        // Load config
        config = plugin.getConfig();


        // Load bot
        if (config.contains("discord.bot")) {
            bot = new Bot(
                    config.getString("discord.bot.name"),
                    config.getString("discord.bot.token"),
                    config.getString("discord.bot.clientId"),
                    config.getString("discord.bot.clientSecret")
            );
        }
    }

    public Bot getBot() {
        return bot;
    }
}
