package com.github.gatoartstudios.munecraft.config;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.core.abstracts.Config;
import com.github.gatoartstudios.munecraft.core.event.EventListener;
import com.github.gatoartstudios.munecraft.models.Bot;
import com.github.gatoartstudios.munecraft.models.SQL;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager extends Config {
    private static FileConfiguration config;
    private Bot bot;
    private SQL sql;

    public ConfigManager(Munecraft plugin, String fileName) {
        super(plugin, fileName);

        new EventHandler(this);
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

        // Load SQL
        if (config.contains("database")) {
            sql = new SQL(
                    config.getString("database.host"),
                    config.getInt("database.port"),
                    config.getString("database.database"),
                    config.getString("database.username"),
                    config.getString("database.password"),
                    config.getString("database.type")
            );
        }
    }

    public Bot getBot() {
        return bot;
    }

    public SQL getSql() {
        return sql;
    }

    private static class EventHandler extends EventListener {
        private ConfigManager configManager;

        public EventHandler(ConfigManager configManager) {
            this.configManager = configManager;
        }
    }
}
