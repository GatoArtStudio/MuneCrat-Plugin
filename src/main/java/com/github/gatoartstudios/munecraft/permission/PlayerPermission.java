package com.github.gatoartstudios.munecraft.permission;

import com.github.gatoartstudios.munecraft.core.interfaces.Permission;

public enum PlayerPermission implements Permission {
    TRASH("munecraft.trash"),
    TRASH_HAND("munecraft.trash.hand"),
    TRASH_MENU("munecraft.trash.menu"),
    FURNACE("munecraft.furnace"),
    GRAVE_SAVEINVENTORY("munecraft.grave.saveinventory"),
    GRAVE_PUBLIC("munecraft.grave.public"),
    GRAVE_DURATION("munecraft.grave.duration"),
    GRAVE_DURATION_1("munecraft.grave.duration.1"),
    GRAVE_DURATION_2("munecraft.grave.duration.2"),
    GRAVE_DURATION_3("munecraft.grave.duration.3"),
    GRAVE_DURATION_4("munecraft.grave.duration.4"),
    GRAVE_DURATION_5("munecraft.grave.duration.5");

    private final String permission;

    PlayerPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String getPermission() {
        return permission;
    }
}
