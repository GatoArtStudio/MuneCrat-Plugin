package com.github.gatoartstudios.munecraft.repositories;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.core.event.EventListener;
import com.github.gatoartstudios.munecraft.core.interfaces.ICrud;
import com.github.gatoartstudios.munecraft.helpers.CoordinatesHelper;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.helpers.PlayerHelper;
import com.github.gatoartstudios.munecraft.models.FurnaceModel;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class FurnaceRepository extends EventListener implements ICrud<Integer, FurnaceModel> {
    private final Munecraft plugin;
    private final File configDirectory;
    private Location originWorld;
    private List<FurnaceModel> furnaces = new ArrayList<>();
    private YamlConfiguration config;

    public FurnaceRepository(Munecraft plugin) {
        this.plugin = plugin;

        this.configDirectory = new File(plugin.getDataFolder(), "furnaces");
        if (!configDirectory.exists()) {
            configDirectory.mkdirs();
        }
    }

    @Override
    public void onLoaded() {
        loadConfig();
    }

    @Override
    public void onDisable() {
        saveFurnaces();
    }

    private void loadConfig() {
        File configFile = getConfigFile();
        this.config = new YamlConfiguration();
        this.furnaces = new ArrayList<>();


        if (!configFile.exists()) {
            this.originWorld = getDefaultLocation();
            this.config.set("originWorld", PlayerHelper.serializeLocation(this.originWorld));

            try {
                this.config.save(configFile);
            } catch (Exception e) {
                LoggerCustom.error("Error while saving config repositories: " + e.getMessage());
            }
            return;
        }

        this.config = YamlConfiguration.loadConfiguration(configFile);
        if (this.config.contains("originWorld")) {
            this.originWorld = PlayerHelper.deserializeLocation(Objects.requireNonNull(config.getString("originWorld")));
        } else {
            this.originWorld = getDefaultLocation();
        }

        loadFurnaces();
    }

    private void loadFurnaces() {
        if (this.config.contains("furnaces")) {

            List<Map<?, ?>> list_temp = config.getMapList("furnaces");

            for (Map<?, ?> furnace : list_temp) {
                String name = (String) furnace.get("name");
                UUID uuid = UUID.fromString((String) furnace.get("uuid"));
                Location location = PlayerHelper.deserializeLocation((String) furnace.get("location"));
                int index = ((Number) furnace.get("index")).intValue();

                FurnaceModel furnaceModel = new FurnaceModel(name, uuid, location, index);
                furnaces.add(furnaceModel);
            }

        }
    }

    private void saveFurnaces() {
        config.set("originWorld", PlayerHelper.serializeLocation(this.originWorld));

        if (furnaces == null || furnaces.isEmpty()) {
            config.set("furnaces", null);
        } else {
            List<Map<?, ?>> list_furnaces_formatted = new ArrayList<>();

            for (FurnaceModel furnace : furnaces) {
                Map<String, Object> furnaceMap = new HashMap<>();
                furnaceMap.put("name", furnace.getName());
                furnaceMap.put("uuid", furnace.getUuid().toString());
                furnaceMap.put("location", PlayerHelper.serializeLocation(furnace.getLocation()));
                furnaceMap.put("index", furnace.getIndex());

                list_furnaces_formatted.add(furnaceMap);
            }
            config.set("furnaces", list_furnaces_formatted);
        }

        try {
            this.config.save(getConfigFile());
        } catch (Exception e) {
            LoggerCustom.error("Error while saving config repositories: " + e.getMessage());
        }
    }

    private File getConfigFile() {
        return new File(configDirectory, "config.yml");
    }

    @Override
    public void create(FurnaceModel entity) {
        if (entity == null) return;

        furnaces.add(entity);
        saveFurnaces();
        LoggerCustom.debug("Furnace created: index=" + entity.getIndex() + ", name=" + entity.getName());
    }

    @Override
    public FurnaceModel read(Integer index) {
        if (index == null) return null;

        Optional<FurnaceModel> found = furnaces.stream()
                .filter(furnaceModel -> furnaceModel.getIndex() == index)
                .findFirst();

        return found.orElse(null);
    }

    public FurnaceModel readByUUID(UUID uuid) {
        if (uuid == null) return null;

        Optional<FurnaceModel> found = furnaces.stream()
                .filter(furnaceModel -> furnaceModel.getUuid().equals(uuid))
                .findFirst();

        return found.orElse(null);
    }

    @Override
    public void update(FurnaceModel entity) {
        if (entity == null) return;

        int idx = entity.getIndex();
        for (int i = 0; i < furnaces.size(); i++) {
            if (furnaces.get(i).getIndex() == idx) {
                furnaces.set(i, entity);
                saveFurnaces();
                LoggerCustom.debug("Furnace updated: index=" + idx + ", name=" + entity.getName());
                return;
            }
        }

        LoggerCustom.warning("Furnace was not found with index=" + idx + "to update.");
    }

    @Override
    public void delete(Integer index) {
        if (index == null) return;

        boolean removed = furnaces.removeIf(furnaceModel -> furnaceModel.getIndex() == index);
        if (removed) {
            saveFurnaces();
            LoggerCustom.debug("Furnace removed: index=" + index);
        } else {
            LoggerCustom.warning("Furnace was not found with index=" + index + "to eliminate.");
        }
    }

    @Override
    public List<FurnaceModel> getAll() {
        return furnaces;
    }

    public Location getOriginWorld() {
        return originWorld;
    }

    public void setOriginWorld(Location originWorld) {
        this.originWorld = originWorld;
        saveFurnaces();
    }

    public Location getDefaultLocation() {
        Location locationSpawn = plugin.getServer().getWorlds().getFirst().getSpawnLocation();
        int[] coordinates = CoordinatesHelper.CoordinatesMinInChunk(locationSpawn.getBlockX(), locationSpawn.getBlockZ());
        int x = coordinates[0];
        int z = coordinates[1];
        return new Location(plugin.getServer().getWorlds().getFirst(), x, -60, z);
    }
}
