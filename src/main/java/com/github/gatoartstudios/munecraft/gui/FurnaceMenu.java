package com.github.gatoartstudios.munecraft.gui;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.helpers.CoordinatesHelper;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.models.FurnaceModel;
import com.github.gatoartstudios.munecraft.models.LocationGeneric;
import com.github.gatoartstudios.munecraft.repositories.FurnaceRepository;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class FurnaceMenu {
    private final Munecraft plugin;
    private Location locationOriginWorld;
    private final FurnaceRepository furnaceRepository;

    private final int sizeMatrix = 16;
    private int[][][] matriz = new int[sizeMatrix][sizeMatrix][sizeMatrix];
    private Map<Integer, LocationGeneric> locationsIndex = new HashMap<>();

    /**
     * Constructor for FurnaceMenu.
     * Initializes the FurnaceRepository and builds the matrix if the origin world location is available.
     *
     * @param plugin The Munecraft plugin instance.
     */
    public FurnaceMenu(Munecraft plugin) {
        this.plugin = plugin;
        furnaceRepository = new FurnaceRepository(plugin);
        locationOriginWorld = furnaceRepository.getOriginWorld();
        if (locationOriginWorld != null) {
            buildMatrix();
        }
    }

    /**
     * Builds a 3D matrix of possible furnace locations starting from the origin world location.
     */
    private void buildMatrix() {
        int index = 0;

        for (int i = 0; i < sizeMatrix; i++) {
            for (int j = 0; j < sizeMatrix; j++) {
                for (int k = 0; k < sizeMatrix; k++) {
                    LocationGeneric location = new LocationGeneric(locationOriginWorld.getBlockX() + i, locationOriginWorld.getBlockY() + j, locationOriginWorld.getBlockZ() + k);
                    locationsIndex.put(index, location);
                    matriz[i][j][k] = index++;
                }
            }
        }
    }

    /**
     * Opens the player's furnace menu asynchronously.
     * If the player already has a furnace, opens it; otherwise, finds the next free slot and creates a new furnace.
     *
     * @param player The player for whom to open the furnace menu.
     */
    public void openFurnaceMenuAsync(Player player) {

        if (locationOriginWorld == null) {
            locationOriginWorld = furnaceRepository.getOriginWorld();
        }

        FurnaceModel playerFurnace = furnaceRepository.readByUUID(player.getUniqueId());
        if (playerFurnace != null) {
            Location location = playerFurnace.getLocation();
            int[] chunkCoordinates = CoordinatesHelper.CoordinatesToChunkCoordinates(location.getBlockX(), location.getBlockZ());
            int chunkX = chunkCoordinates[0];
            int chunkZ = chunkCoordinates[1];

            // load chunk
            CompletableFuture<Chunk> chunkFuture = locationOriginWorld.getWorld().getChunkAtAsync(chunkX, chunkZ);
            chunkFuture.thenRun(() -> {

                plugin.getServer()
                        .getRegionScheduler()
                        .run(plugin, location, task -> {
                            Block block = locationOriginWorld.getWorld().getBlockAt(location);
                            if (block.getType() != Material.FURNACE) block.setType(Material.FURNACE);
                            player.openInventory(((Furnace) block.getState()).getInventory());
                            player.sendMessage(
                                    Component.text("Abriendo la horno de " + player.getName())
                                            .color(TextColor.color(0, 255, 0))
                            );
                        });
            });
            return;
        }

        findNextFreeSlot(foundLocation -> {
            if (foundLocation == null) {
                player.sendMessage(
                        Component.text("No hay huecos disponibles")
                                .color(TextColor.color(255, 0, 0))
                );
                return;
            }

            int index = locationToIndex(foundLocation);
            FurnaceModel newPlayerFurnace = new FurnaceModel(
                    player.getName(),
                    player.getUniqueId(),
                    foundLocation,
                    index
            );
            furnaceRepository.create(newPlayerFurnace);

            plugin.getServer()
                    .getRegionScheduler()
                    .run(plugin, foundLocation, task -> {
                       Block block = foundLocation.getWorld().getBlockAt(foundLocation);
                       if (block.getType() != Material.FURNACE) block.setType(Material.FURNACE);
                       Furnace furnaceState = (Furnace) block.getState();
                       player.openInventory(furnaceState.getInventory());
                       player.sendMessage(
                               Component.text("Abriendo la horno de " + player.getName())
                                       .color(TextColor.color(0, 255, 0))
                       );
                    });
        });
    }

    /**
     * Finds the next available free slot for a furnace and returns it via the callback.
     * If no slot is available, the callback receives null.
     *
     * @param callback The callback to receive the found Location or null if none is available.
     */
    public void findNextFreeSlot(Consumer<@Nullable Location> callback) {

        if (locationOriginWorld == null) {
            locationOriginWorld = furnaceRepository.getOriginWorld();
        }

        World world = locationOriginWorld.getWorld();
        if (world == null) {
            callback.accept(null);
            return;
        }

        int originX = locationOriginWorld.getBlockX();
        int originZ = locationOriginWorld.getBlockZ();

        int[] chunkCoordinates = CoordinatesHelper.CoordinatesToChunkCoordinates(originX, originZ);
        int chunkX = chunkCoordinates[0];
        int chunkZ = chunkCoordinates[1];

        // load chunk
        CompletableFuture<Chunk> chunkFuture = world.getChunkAtAsync(chunkX, chunkZ);

        chunkFuture.thenRun(() -> {

            LoggerCustom.debug("Buscando hueco libre en el mundo: " + locationOriginWorld.getWorld().getName());
            plugin.getServer()
                .getRegionScheduler()
                .run(plugin, locationOriginWorld, task -> {
                    Location found = null;
                    LoggerCustom.debug("Iteracion sobre la matrix para encontrar un hueco libre");

                    if (locationsIndex.isEmpty()) {
                        LoggerCustom.debug("La matrix esta vacia. Se va a construir");
                        buildMatrix();
                    }

                    try {
                        for (Map.Entry<Integer, LocationGeneric> location : locationsIndex.entrySet()) {
                            Location locationBlock = new Location(world, location.getValue().getX(), location.getValue().getY(), location.getValue().getZ());
                            if (world.getBlockAt(locationBlock).getType() != Material.FURNACE) {
                                found = locationBlock;
                                break;
                            }
                        }
                    } catch (Exception e) {
                        LoggerCustom.error("Error al iterar la matrix para encontrar un hueco libre" + e.getMessage());
                    }

                    LoggerCustom.debug("Hueco libre encontrado: " + found);
                    callback.accept(found);
                });
        });
    }

    /**
     * Removes the furnace at the specified matrix index.
     * If a furnace exists at the index, it is replaced with air.
     *
     * @param index The index in the matrix to remove the furnace from.
     * @return true if a furnace was removed, false otherwise.
     */
    public boolean removeFurnaceAtIndex(int index) {

        if (locationOriginWorld == null) {
            locationOriginWorld = furnaceRepository.getOriginWorld();
        }

        Location location = indexToLocation(index, locationOriginWorld);
        Block block = locationOriginWorld.getWorld().getBlockAt(location);
        if (block.getType() == Material.FURNACE) {
            block.setType(Material.AIR);
            return true;
        }
        return false;
    }

    /**
     * Converts a matrix index to a Bukkit Location relative to the origin.
     *
     * @param index The index in the matrix.
     * @param origin The origin Location.
     * @return The corresponding Bukkit Location.
     */
    public Location indexToLocation(int index, Location origin) {// coordenadas relativas 0..15
        if (locationsIndex.isEmpty()) {
            LoggerCustom.debug("La matrix esta vacia. Se va a construir");
            buildMatrix();
        }

        double x, y, z;
        x = locationsIndex.get(index).getX();
        y = locationsIndex.get(index).getY();
        z = locationsIndex.get(index).getZ();
        return new Location(origin.getWorld(), x, y, z);
    }

    /**
     * Converts a Bukkit Location to its corresponding matrix index.
     *
     * @param location The Bukkit Location to convert.
     * @return The matrix index, or null if not found.
     */
    public Integer locationToIndex(Location location) {

        if (locationsIndex.isEmpty()) {
            LoggerCustom.debug("La matrix esta vacia. Se va a construir");
            buildMatrix();
        }

        for (Map.Entry<Integer, LocationGeneric> entry : locationsIndex.entrySet()) {
            if (entry.getValue().getX() == location.getBlockX() &&
                entry.getValue().getY() == location.getBlockY() &&
                entry.getValue().getZ() == location.getBlockZ()) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Gets the origin world location.
     *
     * @return The origin world Location.
     */
    public Location getLocationOriginWorld() {
        return locationOriginWorld;
    }

    /**
     * Sets the origin world location and rebuilds the matrix.
     *
     * @param locationOriginWorld The new origin world Location.
     */
    public void setLocationOriginWorld(Location locationOriginWorld) {
        this.locationOriginWorld = locationOriginWorld;
        furnaceRepository.setOriginWorld(locationOriginWorld);
        buildMatrix();
    }
}
