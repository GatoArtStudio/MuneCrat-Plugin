package com.github.gatoartstudios.munecraft.models;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FurnaceModel implements ConfigurationSerializable {
    private String name;
    private UUID uuid;
    private Location location;
    private int index;

    public FurnaceModel(String name, UUID uuid, Location location, int index) {
        this.name = name;
        this.uuid = uuid;
        this.location = location;
        this.index = index;
    }

    public FurnaceModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("uuid", uuid);
        map.put("location", location);
        map.put("index", index);
        return map;
    }

    public static FurnaceModel deserialize(Map<String, Object> map) {
        String name = (String) map.get("name");
        UUID uuid = UUID.fromString((String) map.get("uuid"));
        Location location = (Location) map.get("location");
        int index = (int) map.get("index");
        return new FurnaceModel(name, uuid, location, index);
    }
}
