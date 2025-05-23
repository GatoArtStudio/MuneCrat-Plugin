package com.github.gatoartstudios.munecraft.gui;

import com.github.gatoartstudios.munecraft.Munecraft;
import com.github.gatoartstudios.munecraft.core.implement.TrashInventoryHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class TrashMenu implements Listener {

    private final Munecraft plugin;

    public TrashMenu(Munecraft plugin) {
        this.plugin = plugin;
    }

    public void openTrashMenu(Player player) {
        TrashInventoryHolder holder = new TrashInventoryHolder(plugin);
        Inventory inventory = holder.getInventory();

        player.openInventory(inventory);
    }

    // --------------- EVENTS ---------------

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        Inventory closeInv = event.getInventory();

        InventoryHolder inventoryHolder = null;

        try {
            InventoryHolder holder = closeInv.getHolder();
            if (holder instanceof TrashInventoryHolder) {
                inventoryHolder = holder;
            }
        } catch (IllegalStateException e) {
            return;
        }

        if (inventoryHolder == null) return;

        ItemStack[] contents = closeInv.getContents();
        int totalItemsRemoved = 0;
        for (ItemStack item : contents) {
            if (item != null && item.getType() != Material.AIR) {
                totalItemsRemoved += item.getAmount();
            }
        }

        player.sendMessage(
                Component.text("Has borrado " + totalItemsRemoved + " items del inventario.")
                        .color(TextColor.color(0, 255, 0))
        );

        // Remove the items from inventory
        closeInv.clear();
    }
}
