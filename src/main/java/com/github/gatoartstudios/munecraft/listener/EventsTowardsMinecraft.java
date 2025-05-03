package com.github.gatoartstudios.munecraft.listener;

import com.github.gatoartstudios.munecraft.core.event.EventListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;

public class EventsTowardsMinecraft extends EventListener {

    @Override
    public void onMessageToMinecraft(String message) {

        Bukkit.broadcast(
                Component.text("[STAFF] ")
                        .append(
                                Component.text(message)
                                        .color(TextColor.color(0, 255, 0))
                        )
                        .color(TextColor.color(255, 0, 0))
        );
    }
}
