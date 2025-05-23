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
     *
     * @param plugin
     */
    public FurnaceMenu(Munecraft plugin) {
        this.plugin = plugin;
        furnaceRepository = new FurnaceRepository(plugin);
        locationOriginWorld = furnaceRepository.getOriginWorld();
        if (locationOriginWorld != null) {
            buildMatrix();
        }
    }

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
     *
     * @param player
     * @return
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
     *
     * @param callback
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
     *
     * @param index
     * @return
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
     *
     * @param index
     * @param origin
     * @return
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
     *
     * @param location
     * @return
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
     *
     * @return
     */
    public Location getLocationOriginWorld() {
        return locationOriginWorld;
    }

    /**
     *
     * @param locationOriginWorld
     */
    public void setLocationOriginWorld(Location locationOriginWorld) {
        this.locationOriginWorld = locationOriginWorld;
        furnaceRepository.setOriginWorld(locationOriginWorld);
        buildMatrix();
    }
}
