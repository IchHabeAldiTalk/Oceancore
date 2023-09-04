package de.oceanblocks.oceancore.gui;

import de.oceanblocks.oceancore.Oceancore;
import de.oceanblocks.oceancore.item.ItemStackBuilder;
import de.oceanblocks.oceancore.item.NBTModifier;
import de.oceanblocks.oceancore.util.Callback;
import de.oceanblocks.oceancore.util.EmptyCallback;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
public class InteractableItem implements Listener {

    private ItemStack itemStack;
    private Callback<InventoryClickEvent> clickCallback = new EmptyCallback<>();
    private Callback<PlayerInteractEvent> interactCallback = new EmptyCallback<>();
    private Callback<PlayerDropItemEvent> dropCallback = new EmptyCallback<>();
    private Callback<PlayerPickupItemEvent> pickUpCallback = new EmptyCallback<>();

    public InteractableItem(ItemStack itemStack) {
        this.itemStack = NBTModifier.setNBTTag(itemStack, UUID.randomUUID().toString(), UUID.randomUUID().toString());
        registerListener();
    }

    public InteractableItem(ItemStackBuilder itemBuilder) {
        this.itemStack = NBTModifier.setNBTTag(itemBuilder.build(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
        registerListener();
    }

    /***
     *
     * CALLS ON CONSTRUCTOR
     *
     * enables every function in this class
     * just use this method when you're sure
     * this listener is unregisterd
     *
     */
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
    }

    /***
     * will be triggered when user clicks on item
     * @param callback what should happen when you click on the item
     * @return the current instance
     */
    public InteractableItem click(Callback<InventoryClickEvent> callback) {
        this.clickCallback = callback;
        return this;
    }

    /***
     * will be triggered when a player interacts with the itemstack
     * @param callback what should happen when you interact with the item
     * @return the current instance
     */
    public InteractableItem interact(Callback<PlayerInteractEvent> callback) {
        this.interactCallback = callback;
        return this;
    }

    /***
     * will be triggered when a player drops this itemstack
     * @param callback what should happen when you drop the item
     * @return the current instance
     */
    public InteractableItem drop(Callback<PlayerDropItemEvent> callback) {
        this.dropCallback = callback;
        return this;
    }

    /***
     * will be triggered when a player picks up this itemstack
     * @param callback what should happen when you pick up this item
     * @return the current instance
     */
    public InteractableItem pickUp(Callback<PlayerPickupItemEvent> callback) {
        this.pickUpCallback = callback;
        return this;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
        if (event.getCurrentItem().equals(itemStack)) {
            event.setCancelled(true);
            clickCallback.accept(event);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null || item.getType().equals(Material.AIR)) return;
        if (item.equals(itemStack)) {
            event.setCancelled(true);
            interactCallback.accept(event);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Item itemDrop = event.getItemDrop();
        Player player = event.getPlayer();
        ItemStack itemStack = itemDrop.getItemStack();
        if (itemStack.equals(this.itemStack)) {
            event.setCancelled(true);
            dropCallback.accept(event);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Item itemDrop = event.getItem();
        Player player = event.getPlayer();
        ItemStack itemStack = itemDrop.getItemStack();
        if (itemStack.equals(this.itemStack)) {
            event.setCancelled(true);
            pickUpCallback.accept(event);
        }
    }
}
