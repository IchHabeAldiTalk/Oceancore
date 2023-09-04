package de.oceanblocks.oceancore.entity;

import de.oceanblocks.oceancore.Oceancore;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Hologram {

    private final List<ArmorStand> entityList = new ArrayList<>();
    private final Location location;
    private String[] text;
    private final double distance = 0.25;
    private int count;

    public Hologram( String[] text, Location location) {
        this.location = location.add(0, 0.5, 0);
        this.text = text;
        this.create();
    }

    public void create() {
        String[] arresting = this.text;
        int n = arresting.length;
        int n2 = 0;
        while (n2 < n) {
            String Text = arresting[n2];
            ArmorStand entity = this.location.getWorld().spawn(this.location, ArmorStand.class);
            entity.setInvisible(true);
            entity.setGravity(false);
            entity.setBasePlate(false);
            entity.setCustomNameVisible(true);
            entity.setInvulnerable(true);
            entity.setCustomName(Text);
            this.entityList.add(entity);
            this.location.subtract(0.0, this.distance, 0.0);
            ++this.count;
            ++n2;
        }
        int i = 0;
        while (i < this.count) {
            this.location.add(0.0, this.distance, 0.0);
            ++i;
        }
        this.count = 0;
    }

    public void reCreate(String[] text, long delay) {
        this.location.getChunk().load();
        for (ArmorStand armorStand : getEntityList()) {
            armorStand.remove();
        }
        for (Entity entity : Bukkit.getWorld("lobby").getEntities()) {
            if (entity instanceof ArmorStand) {
                if (!entity.getName().endsWith("§7in klein")) {
                    entity.remove();
                }
            }
        }
        this.text = text;
        Bukkit.getScheduler().scheduleSyncDelayedTask(Oceancore.getInstance(), this::create, delay);
    }

    public void setText(String[] text) {
        this.text = text;
    }

}
