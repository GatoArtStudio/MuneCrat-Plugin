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

    /**
     * Saves the default configuration to the configuration file if it does not already exist.
     * Logs success or error messages based on the outcome of the save operation.
     * @return true if the configuration was created, false if it already existed
     */
    public boolean saveDefaults() {
        if (configFile.exists()) return false;

        plugin.saveResource(configFile.getName(), false);
        LoggerCustom.success("Config has been created successfully");

        return true;
    }

    /**
     * Returns the configuration object.
     * @return the configuration object
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Saves the current configuration to the configuration file.
     * Logs success or error messages based on the outcome of the save operation.
     */
    public void save() {
        try {
            config.save(configFile);
            LoggerCustom.success("Config has been saved successfully");
        } catch (Exception e) {
            LoggerCustom.error("Error while saving config: " + e.getMessage());
        }
    }

    /**
     * This method is called when the plugin is loaded and it is necessary to load the configuration.
     * It is called after the plugin has been enabled and before the plugin is fully initialized.
     */
    public abstract void load();

    /**
     * This method is called when the plugin is fully initialized and it is necessary to initialize
     * the configuration. It is called after the plugin has been enabled and after the plugin has been
     * loaded.
     */
    public abstract void init();
}
