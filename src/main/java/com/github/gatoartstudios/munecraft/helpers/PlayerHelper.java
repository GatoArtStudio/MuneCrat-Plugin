package com.github.gatoartstudios.munecraft.helpers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Helper class for player-related operations.
 */
public class PlayerHelper {

    /**
     * Serializes a player's inventory to a Base64 encoded string.
     *
     * @param player The player whose inventory is to be serialized.
     * @return The Base64 encoded string representing the inventory.
     */
    public static String serializeInventory(Player player) {
        try {
            PlayerInventory inventory = player.getInventory();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(inventory.getSize());
            for (ItemStack item : inventory.getContents()) {
                dataOutput.writeObject(item);
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            LoggerCustom.error("Error while serializing inventory: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deserializes a Base64 encoded string to a player's inventory.
     *
     * @param player The player whose inventory is to be deserialized.
     * @param data The Base64 encoded string representing the inventory.
     */
    public static void deserializeInventory(Player player, String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            PlayerInventory inventory = player.getInventory();

            int size = dataInput.readInt();
            for (int i = 0; i < size; i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
        } catch (IOException | ClassNotFoundException e) {
            LoggerCustom.error("Error while deserializing inventory: " + e.getMessage());
        }
    }

    /**
     * Serializes a Location object to a string.
     *
     * @param location The location to serialize.
     * @return The serialized string representing the location.
     */
    public static String serializeLocation(Location location) {
        return location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getWorld().getUID();
    }

    /**
     * Deserializes a string to a Location object.
     *
     * @param data The serialized string representing the location.
     * @return The deserialized Location object.
     */
    public static Location deserializeLocation(String data) {
        String[] parts = data.split(";");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid location data.");
        }
        double x = Double.parseDouble(parts[0]);
        double y = Double.parseDouble(parts[1]);
        double z = Double.parseDouble(parts[2]);
        return new Location(Bukkit.getWorld(UUID.fromString(parts[3])), x, y, z);
    }

    public static UUID getOfflinePlayerUUID(String name) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
    }
}
