package com.github.gatoartstudios.munecraft.helpers;

import com.github.gatoartstudios.munecraft.core.event.EventDispatcher;
import com.github.gatoartstudios.munecraft.models.UUIDResponseModel;
import com.google.gson.Gson;
import okhttp3.*;
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
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Helper class for player-related operations.
 */
public class PlayerHelper {
    private static final String ENDPOINT_GEYSERMC_GET_ID = "https://api.geysermc.org/v2/utils/uuid/bedrock_or_java/%s?prefix=.";

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder()
            .callTimeout(5, TimeUnit.SECONDS)
            .build();

    private static final Gson GSON = new Gson();

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

    public static Optional<UUID> getOfflinePlayerUUID(String playerName) {
        // Detectar si es jugador de Floodgate (Bedrock)
        if (isBedrockName(playerName)) {
            // UUID en formato Floodgate (00000000-0000-0000-000X-XXXXXXXXXXXX)
            // Donde X es un número arbitrario pero único por nombre
            // Floodgate usa su propia forma, esto es una emulación cercana
            return generateFloodgateStyleUUID(playerName);
        } else {
            // Estilo Java Offline
            return Optional.of(UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(StandardCharsets.UTF_8)));
        }
    }

    private static boolean isBedrockName(String name) {
        // Detecta si el nombre es típico de Floodgate
        return name.startsWith(".");
    }

    private static Optional<UUID> generateFloodgateStyleUUID(String playerName) {

        String url = String.format(ENDPOINT_GEYSERMC_GET_ID, playerName);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                LoggerCustom.error("Error while getting Floodgate UUID: " + response);
                return Optional.empty();
            }

            String json = response.body().string();
            UUIDResponseModel uuidResponseModel = GSON.fromJson(json, UUIDResponseModel.class);
            String dashedId = withDashes(uuidResponseModel.getId());

            LoggerCustom.info("Floodgate UUID: " + dashedId);
            return Optional.of(UUID.fromString(dashedId));

        } catch (IOException e) {
            LoggerCustom.error("Error while getting Floodgate UUID: " + e.getMessage());
            EventDispatcher.dispatchAlert("Error while getting Floodgate UUID: " + e.getMessage());
            return Optional.empty();
        }
    }

    private static String withDashes(String rawId) {
        return rawId.replaceFirst(
                "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{12})",
                "$1-$2-$3-$4-$5"
        );
    }
}
