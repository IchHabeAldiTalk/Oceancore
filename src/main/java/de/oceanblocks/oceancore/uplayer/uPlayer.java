package de.oceanblocks.oceancore.uplayer;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class uPlayer {

    private final Player player;
    private final UUID uuid;
    private final String name;

    public uPlayer(Player player, UUID uuid, String name) {
        this.player = player;
        this.uuid = uuid;
        this.name = name;
    }

}
