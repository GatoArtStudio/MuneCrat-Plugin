package com.github.gatoartstudios.munecraft.gui;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.models.FurnaceModel;
import com.github.gatoartstudios.munecraft.repository.FurnaceRepository;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class FurnaceMenu {
    private final Munecraft plugin;
    private Location locationOriginWorld;
    private final int sizeMatrix = 16;
    private final FurnaceRepository furnaceRepository;


    public FurnaceMenu(Munecraft plugin) {
        this.plugin = plugin;
        furnaceRepository = new FurnaceRepository(plugin);
        locationOriginWorld = furnaceRepository.getOriginWorld();
    }

    public boolean openFurnaceMenu(Player player) {

        FurnaceModel playerFurnace = furnaceRepository.readByUUID(player.getUniqueId());
        Location locationFindFree;

        if (playerFurnace == null) {
            locationFindFree = findNextFreeSlot();
            if (locationFindFree == null) return false;

            int index = locationToIndex(locationFindFree, locationOriginWorld);
            playerFurnace = new FurnaceModel(
                    player.getName(),
                    player.getUniqueId(),
                    locationFindFree,
                    index
            );
            furnaceRepository.create(playerFurnace);
        }

        locationFindFree = playerFurnace.getLocation();

        if (locationFindFree == null) return false;

        Block block = locationOriginWorld.getWorld().getBlockAt(locationFindFree);
        if (block.getType() != Material.FURNACE) {
            block.setType(Material.FURNACE);
        }
        Furnace furnace = (Furnace) block.getState();
        player.openInventory(furnace.getInventory());
        return true;
    }

    public @Nullable Location findNextFreeSlot() {
        for (int i = 0; i < sizeMatrix * sizeMatrix * sizeMatrix; i++) {
            Location location = indexToLocation(i, locationOriginWorld);
            if (locationOriginWorld.getWorld().getBlockAt(location).getType() != Material.FURNACE) {
                return location;
            }
        }
        // No free slots
        return null;
    }

    public boolean removeFurnaceAtIndex(int index) {
        Location location = indexToLocation(index, locationOriginWorld);
        Block block = locationOriginWorld.getWorld().getBlockAt(location);
        if (block.getType() == Material.FURNACE) {
            block.setType(Material.AIR);
            return true;
        }
        return false;
    }

    public Location indexToLocation(int index, Location origin) {
        int x = index % sizeMatrix;
        int y = (index / (sizeMatrix * sizeMatrix)) % sizeMatrix;
        int z = (index / sizeMatrix) % sizeMatrix;
        return new Location(origin.getWorld(), x, y, z);
    }

    public int locationToIndex(Location location, Location origin) {
        int dx = location.getBlockX() - origin.getBlockX();
        int dy = location.getBlockY() - origin.getBlockY();
        int dz = location.getBlockZ() - origin.getBlockZ();
        return dx + (dz * 16) + (dy * 16 * 16);
    }

    public Location getLocationOriginWorld() {
        return locationOriginWorld;
    }

    public void setLocationOriginWorld(Location locationOriginWorld) {
        this.locationOriginWorld = locationOriginWorld;
        furnaceRepository.setOriginWorld(locationOriginWorld);
    }
}
