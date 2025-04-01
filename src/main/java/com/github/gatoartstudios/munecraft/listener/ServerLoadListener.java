package com.github.gatoartstudios.munecraft.listener;

import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class ServerLoadListener implements Listener {

    /**
     * This method is called when the server is fully started, it notifies
     * the plugin services that the server is ready.
     *
     * @param event The event that is called when the server is fully started.
     */
    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        LoggerCustom.info("The server is fully started, notifying the plugin services...");
        // Notify the plugin services that the server is ready
        EventDispatcher.dispatchLoaded();
    }
}