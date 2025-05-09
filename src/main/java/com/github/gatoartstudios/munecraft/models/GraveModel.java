package com.github.gatoartstudios.munecraft.models;

import java.util.UUID;

public class GraveModel {
    private int idGrave;
    private UUID uuid;
    private boolean respawnHere = false;
    private boolean gravePublic = true;
    private int graveDuration;
    private String graveLocation;
    private String graveInventory;

    public GraveModel(int idGrave, UUID uuid, boolean respawnHere, boolean gravePublic, int graveDuration, String graveLocation, String graveInventory) {
        this.idGrave = idGrave;
        this.uuid = uuid;
        this.respawnHere = respawnHere;
        this.gravePublic = gravePublic;
        this.graveDuration = graveDuration;
        this.graveLocation = graveLocation;
        this.graveInventory = graveInventory;
    }

    public GraveModel() {
    }

    public int getIdGrave() {
        return idGrave;
    }

    public void setIdGrave(int idGrave) {
        this.idGrave = idGrave;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isRespawnHere() {
        return respawnHere;
    }

    public void setRespawnHere(boolean respawnHere) {
        this.respawnHere = respawnHere;
    }

    public boolean isGravePublic() {
        return gravePublic;
    }

    public void setGravePublic(boolean gravePublic) {
        this.gravePublic = gravePublic;
    }

    public int getGraveDuration() {
        return graveDuration;
    }

    public void setGraveDuration(int graveDuration) {
        this.graveDuration = graveDuration;
    }

    public String getGraveLocation() {
        return graveLocation;
    }

    public void setGraveLocation(String graveLocation) {
        this.graveLocation = graveLocation;
    }

    public String getGraveInventory() {
        return graveInventory;
    }

    public void setGraveInventory(String graveInventory) {
        this.graveInventory = graveInventory;
    }
}
