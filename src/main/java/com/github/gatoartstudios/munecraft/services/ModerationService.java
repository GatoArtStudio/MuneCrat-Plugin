package com.github.gatoartstudios.munecraft.services;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class ModerationService {

    /**
     * Gets the list of online players.
     * @return a list of names of all players that are currently online.
     */
    public static List<String> getOnlinePlayers()  {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }
}