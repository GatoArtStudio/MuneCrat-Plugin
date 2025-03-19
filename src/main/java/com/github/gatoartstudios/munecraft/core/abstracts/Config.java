package com.github.gatoartstudios.munecraft.core.abstracts;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public abstract class Config {
    protected Munecraft plugin;
    protected File configFile;
    protected FileConfiguration config;
    protected String fileName;

    public Config(Munecraft plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.configFile = new File(plugin.getDataFolder(), fileName);
        this.config = plugin.getConfig();
    }

    public boolean saveDefaults() {
        if (configFile.exists()) return false;

        plugin.saveResource(configFile.getName(), false);
        LoggerCustom.success("Config has been created successfully");

        return true;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void save() {
        try {
            config.save(configFile);
            LoggerCustom.success("Config has been saved successfully");
        } catch (Exception e) {
            LoggerCustom.error("Error while saving config: " + e.getMessage());
        }
    }

    public abstract void load();
    public abstract void init();
}
