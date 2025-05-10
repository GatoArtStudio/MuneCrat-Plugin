package com.github.gatoartstudios.munecraft;

import com.github.gatoartstudios.munecraft.command.DevelopmentCommand;
import com.github.gatoartstudios.munecraft.command.LoginCommand;
import com.github.gatoartstudios.munecraft.command.StaffChatCommand;
import com.github.gatoartstudios.munecraft.command.StaffCommand;
import com.github.gatoartstudios.munecraft.config.ConfigManager;
import com.github.gatoartstudios.munecraft.core.commands.ServiceCommand;
import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.databases.mysql.DBBuilderMySQL;
import com.github.gatoartstudios.munecraft.databases.DatabaseManager;
import com.github.gatoartstudios.munecraft.databases.mysql.DAO.MySQLGraveDAO;
import com.github.gatoartstudios.munecraft.databases.mysql.DAO.MySQLPlayerDAO;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.listener.*;
import com.github.gatoartstudios.munecraft.services.discord.DiscordBot;
import com.github.gatoartstudios.munecraft.services.ReadLog;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * Main class for the Munecraft plugin.
 * <p>
 * This class is responsible for initializing and managing the plugin's services and configurations.
 */
public final class Munecraft extends JavaPlugin {

    private ServiceCommand serviceCommand;
    private DiscordBot discordBot;
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private HandlesSystemEvents handlesSystemEvents;
    private EventsTowardsMinecraft  eventsTowardsMinecraft;
    private MySQLPlayerDAO playerDAO;
    private MySQLGraveDAO graveDAO;

    @Override
    public void onLoad() {
        LoggerCustom.info("Plugin has been loaded");
        LoggerCustom.info("Plugin version: " + getDescription().getVersion() + " by " + getDescription().getAuthors());

        // Instantiate the class responsible for retrieving the plugin settings
        configManager = new ConfigManager(this, "config.yml");

        // Create an instance and add services that run in another thread, such as bots, API services, and other services external to Minecraft
        serviceCommand = new ServiceCommand();
        discordBot = new DiscordBot(this);
        ReadLog readLog = new ReadLog();

        serviceCommand.addService(readLog);
        serviceCommand.addService(discordBot);
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
        eventsTowardsMinecraft = new EventsTowardsMinecraft();

        // We load the settings from the plugin configuration file
        configManager.init();
        // We create an instance for the database, which will be used by other packages in the plugin
        databaseManager = DatabaseManager.getInstance(this);
        DBBuilderMySQL.executeSqlFileFromResources(databaseManager.getConnection(), "database/schema.sql");

        // Initialize the player DAO
        playerDAO = new MySQLPlayerDAO();
        graveDAO = new MySQLGraveDAO();

        // We run all the services added in the Service Command
        serviceCommand.execute();

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

        serviceCommand.undo();
    }

    /**
     * Registers the events of the plugin, such as {@link HandlesMinecraftEvents}, {@link ServerLoadListener} and {@link DimensionRestrictor}
     */
    void registerEvents() {
        // Logs all plugin events, necessary to monitor game events
        getServer().getPluginManager().registerEvents(new HandlesMinecraftEvents(), this);
        getServer().getPluginManager().registerEvents(new HandlesPlayerEvents(this), this);
        getServer().getPluginManager().registerEvents(new ServerLoadListener(), this);
        getServer().getPluginManager().registerEvents(new DimensionRestrictor(), this);
        getServer().getPluginManager().registerEvents(new GraveSystema(this), this); // Register GraveSystema with Munecraft instance
        getServer().getPluginManager().registerEvents(new HandlerLoginPlayer(this), this);
    }

    /**
     * Registers all plugin commands, such as {@link DevelopmentCommand}
     */
    void registerCommands() {
        // Records all plugin commands, necessary to monitor game events
        Objects.requireNonNull(getCommand("development")).setExecutor(new DevelopmentCommand(this));
        Objects.requireNonNull(getCommand("staff")).setExecutor(new StaffCommand(this));
        Objects.requireNonNull(getCommand("staffchat")).setExecutor(new StaffChatCommand(this));
        Objects.requireNonNull(getCommand("login")).setExecutor(new LoginCommand());
    }

    /**
     * Returns the plugin configuration manager
     * @return the plugin configuration manager
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Returns the plugin database manager
     * @return the plugin database manager
     */
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    /**
     * Returns the player DAO
     * @return the player DAO
     */
    public MySQLPlayerDAO getPlayerDAO() {
        return playerDAO;
    }

    /**
     * Returns the grave DAO
     * @return the grave DAO
     */
    public MySQLGraveDAO getGraveDAO() {
        return graveDAO;
    }
}
