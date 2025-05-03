package com.github.gatoartstudios.munecraft.config;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.core.abstracts.Config;
import com.github.gatoartstudios.munecraft.models.MessagesPluginModel;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigMessages extends Config {
    private static FileConfiguration config;
    private MessagesPluginModel messagesPluginModel;

    public ConfigMessages(Munecraft plugin, String fileName) {
        super(plugin, fileName);
    }

    @Override
    public void load() {
        saveDefaults();
        load();
    }

    @Override
    public void init() {
        // Load config
        config = plugin.getConfig();

        // Load messages
        if (config.contains("messages")) {
            messagesPluginModel = new MessagesPluginModel(
                    config.getString("messages.hello"),
                    config.getString("messages.goodbye"),
                    config.getString("messages.welcome"),
                    config.getString("messages.playerJoin"),
                    config.getString("messages.farewell"),
                    config.getString("messages.error"),
                    config.getString("messages.success"),
                    config.getString("messages.info"),
                    config.getString("messages.warning"),
                    config.getString("messages.debug")
            );
        }
    }
}
