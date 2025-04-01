package com.github.gatoartstudios.munecraft;

import com.github.gatoartstudios.munecraft.command.DevelopmentCommand;
import com.github.gatoartstudios.munecraft.config.ConfigManager;
import com.github.gatoartstudios.munecraft.core.commands.ServiceComman;
import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.databases.DatabaseManager;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.listener.HandlesMinecraftEvents;
import com.github.gatoartstudios.munecraft.listener.HandlesSystemEvents;
import com.github.gatoartstudios.munecraft.listener.DimensionRestrictor;
import com.github.gatoartstudios.munecraft.listener.ServerLoadListener;
import com.github.gatoartstudios.munecraft.services.DiscordBot;
import com.github.gatoartstudios.munecraft.services.ReadLog;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for the Munecraft plugin.
 * <p>
 * This class is responsible for initializing and managing the plugin's services and configurations.
 */
public final class Munecraft extends JavaPlugin {

    private ServiceComman serviceComman;
    private DiscordBot discordBot;
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private HandlesSystemEvents handlesSystemEvents;

    @Override
    public void onLoad() {
        LoggerCustom.info("Plugin has been loaded");
        LoggerCustom.info("Plugin version: " + getDescription().getVersion() + " by " + getDescription().getAuthors());

        // Instantiate the class responsible for retrieving the plugin settings
        configManager = new ConfigManager(this, "config.yml");

        // Create an instance and add services that run in another thread, such as bots, API services, and other services external to Minecraft
        serviceComman = new ServiceComman();
        discordBot = new DiscordBot(this);
        ReadLog readLog = new ReadLog();

        serviceComman.addService(readLog);
        serviceComman.addService(discordBot);
    }

    /**
     * This method is called when the plugin is enabled, it is responsible for
     * initializing the plugin and setting up the database.
     */
    @Override
    public void onEnable() {
        LoggerCustom.info("Plugin has been enabled");
        EventDispatcher.dispatchLogging("[Server] The server has been started");

        // This class is responsible for handling most plugin events, very important
        handlesSystemEvents = new HandlesSystemEvents(this);

        // We load the settings from the plugin configuration file
        configManager.init();
        // We create an instance for the database, which will be used by other packages in the plugin
        databaseManager = DatabaseManager.getInstance(this);

        // We run all the services added in the Service Command
        serviceComman.execute();

        // Register events
        registerEvents();
        registerCommands();
    }

    /**
     * This method is called when the plugin is disabled, it is responsible for
     * stopping the plugin and removing all the services added.
     */
    @Override
    public void onDisable() {
        LoggerCustom.warning("The plugin has been disabled");
        EventDispatcher.dispatchLogging("[Server] The server has been shut down");

        serviceComman.undo();
    }

    /**
     * Registers the events of the plugin, such as {@link HandlesMinecraftEvents}, {@link ServerLoadListener} and {@link DimensionRestrictor}
     */
    void registerEvents() {
        // Logs all plugin events, necessary to monitor game events
        getServer().getPluginManager().registerEvents(new HandlesMinecraftEvents(), this);
        getServer().getPluginManager().registerEvents(new ServerLoadListener(), this);
        getServer().getPluginManager().registerEvents(new DimensionRestrictor(), this);
    }

    /**
     * Registers all plugin commands, such as {@link DevelopmentCommand}
     */
    void registerCommands() {
        // Records all plugin commands, necessary to monitor game events
        getCommand("development").setExecutor(new DevelopmentCommand(this));
    }

    /**
     * Returns the plugin configuration manager
     * @return the plugin configuration manager
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }
}
