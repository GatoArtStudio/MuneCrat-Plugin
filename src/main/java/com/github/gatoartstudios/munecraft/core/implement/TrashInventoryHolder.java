package com.github.gatoartstudios.munecraft.core.implement;

import com.github.gatoartstudios.munecraft.Munecraft;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class TrashInventoryHolder implements InventoryHolder {
    private final Inventory inventory;

    public static final Component TITLE = Component.text("Trash").color(TextColor.color(255, 0, 0));

    public TrashInventoryHolder(Munecraft plugin) {
        this.inventory = Bukkit.createInventory(this, 27, TITLE);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
