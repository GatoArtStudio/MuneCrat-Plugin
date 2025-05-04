package com.github.gatoartstudios.munecraft.shared;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerLoginState {
    // Map to store the login state of players by their UUID
    private final Map<UUID, Boolean> playerLoginState = new ConcurrentHashMap<>();

    // Singleton instance of PlayerLoginState
    private static final PlayerLoginState instance = new PlayerLoginState();

    // Private constructor to enforce singleton pattern
    private PlayerLoginState() {}

    // Get the singleton instance
    public static PlayerLoginState getInstance() {
        return instance;
    }

    // Add or update a player's login state
    public void put(UUID uuid, boolean state) {
        playerLoginState.put(uuid, state);
    }

    // Set a player's login state
    public void set(UUID uuid, boolean state) {
        playerLoginState.put(uuid, state);
    }

    // Get a player's login state (default to false if not found)
    public boolean get(UUID uuid) {
        return playerLoginState.getOrDefault(uuid, false);
    }

    // Remove a player's login state
    public void remove(UUID uuid) {
        playerLoginState.remove(uuid);
    }

    // Clear all login states
    public void clear() {
        playerLoginState.clear();
    }

    // Get all login states
    public Map<UUID, Boolean> getAll() {
        return playerLoginState;
    }
}
