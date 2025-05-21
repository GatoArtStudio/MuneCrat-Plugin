package com.github.gatoartstudios.munecraft.repository;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.core.interfaces.ICrud;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.models.FurnaceModel;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FurnaceRepository implements ICrud<Integer, FurnaceModel> {
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

        loadConfig();
    }

    private void loadConfig() {
        File configFile = getConfigFile();
        this.config = new YamlConfiguration();


        if (!configFile.exists()) {
            this.originWorld = plugin.getServer().getWorlds().getFirst().getSpawnLocation();
            this.config.set("originWorld", originWorld);

            try {
                this.config.save(configFile);
            } catch (Exception e) {
                LoggerCustom.error("Error while saving config repository: " + e.getMessage());
            }
            return;
        }

        this.config = YamlConfiguration.loadConfiguration(configFile);
        if (this.config.contains("originWorld")) {
            this.originWorld = config.getLocation("originWorld");
        } else {
            this.originWorld = plugin.getServer().getWorlds().getFirst().getSpawnLocation();
        }

        loadFurnaces();
    }

    private void loadFurnaces() {
        if (this.config.contains("furnaces")) {
            Object furnacesObj = this.config.get("furnaces");

            if (furnacesObj instanceof List<?> rawList) {

                List<FurnaceModel> temp = new ArrayList<>();
                for (Object element : rawList) {
                    if (element instanceof FurnaceModel) {
                        temp.add((FurnaceModel) element);
                    } else {
                        LoggerCustom.warning("Se encontró un elemento inesperado en 'furnaces': "
                                + element.getClass().getSimpleName());
                    }
                }

                furnaces = temp;
            }
        }
    }

    private void saveFurnaces() {
        config.set("originWorld", originWorld);

        if (furnaces == null || furnaces.isEmpty()) {
            config.set("furnaces", null);
        } else {
            config.set("furnaces", furnaces);
        }

        try {
            this.config.save(getConfigFile());
        } catch (Exception e) {
            LoggerCustom.error("Error while saving config repository: " + e.getMessage());
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
    }
}
