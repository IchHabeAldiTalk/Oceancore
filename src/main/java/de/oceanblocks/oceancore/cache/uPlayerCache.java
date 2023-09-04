package de.oceanblocks.oceancore.cache;

import de.oceanblocks.oceancore.Oceancore;
import de.oceanblocks.oceancore.uplayer.uPlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

@Getter
public class uPlayerCache implements PlayerCacheStructure {

    private ArrayList<uPlayer> cache;

    @Override
    public void loadCache() {
        this.cache = new ArrayList<>();
        Oceancore.getInstance().getLogger().info("Loaded Cache type uPlayerCache.");
    }

    @Override
    public void clearCache() {
        this.cache.clear();
        Oceancore.getInstance().getLogger().info("uPlayerCache cleared.");
    }

    @Override
    public void insertCache(uPlayer player) {
        this.cache.add(player);
    }

    @Override
    public void insertCache(Player player) {
        uPlayer uPlayer = new uPlayer(player, player.getUniqueId(), player.getName());
        this.cache.add(uPlayer);
    }

    @Override
    public void removeFromCache(uPlayer player) {
        this.cache.remove(player);
    }

    @Override
    public void insertIntoDatabase(Player player) {
        uPlayer uPlayer = getPlayer(player);
    }

    public uPlayer getPlayer(UUID uuid) {
        uPlayer player = null;
        for (uPlayer cachedPlayers : this.cache) {
            if (cachedPlayers.getUuid().equals(uuid)) {
                player = cachedPlayers;
            }
        }
        if (player != null) {
            return player;
        } else {
            Player bukkitPlayer = null;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.getUniqueId().equals(uuid)) {
                    bukkitPlayer = onlinePlayer;
                }
            }
            if (bukkitPlayer != null) {
                player = new uPlayer(bukkitPlayer, uuid, bukkitPlayer.getName());
                return player;
            } else {
                Oceancore.getInstance().getLogger().warning("Failed to fetch uPlayer from cache!");
                return null;
            }
        }
    }

    public uPlayer getPlayer(Player bukkitPlayerInput) {
        uPlayer player = null;
        UUID uuid = bukkitPlayerInput.getUniqueId();
        for (uPlayer cachedPlayers : this.cache) {
            if (cachedPlayers.getUuid().equals(uuid)) {
                player = cachedPlayers;
            }
        }
        if (player != null) {
            return player;
        } else {
            Player bukkitPlayer = null;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.getUniqueId().equals(uuid)) {
                    bukkitPlayer = onlinePlayer;
                }
            }
            if (bukkitPlayer != null) {
                player = new uPlayer(bukkitPlayer, uuid, bukkitPlayer.getName());
                return player;
            } else {
                Oceancore.getInstance().getLogger().warning("Failed to fetch uPlayer from cache!");
                return null;
            }
        }
    }

}
