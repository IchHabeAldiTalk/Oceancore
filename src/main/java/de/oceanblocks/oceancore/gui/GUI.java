package de.oceanblocks.oceancore.gui;

import de.oceanblocks.oceancore.Oceancore;
import de.oceanblocks.oceancore.util.Callback;
import de.oceanblocks.oceancore.util.EmptyCallback;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GUI implements Listener {

    private Inventory inventory;
    private Callback<Player> closeCallback = new EmptyCallback<>();
    private List<InteractableItem> items = new ArrayList<>();

    public GUI(int rows, String title) {
        inventory = Bukkit.createInventory(null, rows * 9, title);
        registerListener();
    }

    public GUI(InventoryType inventoryType, String title) {
        inventory = Bukkit.createInventory(null, inventoryType, title);
        registerListener();
    }

    public GUI(int rows, Component title) {
        inventory = Bukkit.createInventory(null, rows * 9, title);
        registerListener();
    }

    public GUI(InventoryType inventoryType, Component title) {
        inventory = Bukkit.createInventory(null, inventoryType, title);
        registerListener();
    }

    public GUI addItem(InteractableItem item) {
        items.add(item);
        inventory.addItem(item.getItemStack());
        return this;
    }

    public GUI setItem(int slot, InteractableItem item) {
        items.add(item);
        inventory.setItem(slot, item.getItemStack());
        return this;
    }

    public GUI close(Callback<Player> callback) {
        this.closeCallback = callback;
        return this;
    }

    public void registerListener() {
        Bukkit.getPluginManager().registerEvents(this, Oceancore.getInstance());
    }

    /***
     * destroys every function from this class
     * just use it when you want to disable
     * this item.
     */
    public void unregisterListener() {
        HandlerList.unregisterAll(this);
        for (InteractableItem item : items) {
            item.unregisterListener();
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        HumanEntity player = event.getPlayer();
        Inventory inventory = event.getInventory();
        if (inventory == this.inventory) {
            closeCallback.accept((Player) player);
        }
    }
}
