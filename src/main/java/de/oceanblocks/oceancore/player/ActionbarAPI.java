package de.oceanblocks.oceancore.player;

import eu.thesimplecloud.api.CloudAPI;
import eu.thesimplecloud.api.player.ICloudPlayer;
import eu.thesimplecloud.api.player.SimpleCloudPlayer;
import eu.thesimplecloud.clientserverapi.lib.promise.ICommunicationPromise;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ActionbarAPI {

    private Player player;
    private final String message;

    private ICloudPlayer iCloudPlayer;
    List<SimpleCloudPlayer> list = new ArrayList<>();
    List<ICloudPlayer> players = new ArrayList<>();

    public ActionbarAPI(Player player, String message) {
        this.player = player;
        this.message = message;
        ICommunicationPromise<ICloudPlayer> promise = CloudAPI.getInstance().getCloudPlayerManager().getCloudPlayer(player.getUniqueId());
        promise.then((iCloudPlayer) -> {
            return this.iCloudPlayer = iCloudPlayer;
        });
    }

    public ActionbarAPI(String message) {
        this.message = message;
    }

    public void sendActionBar() {
        if (iCloudPlayer != null) {
            iCloudPlayer.sendActionBar(this.message);
        }
    }

    public void sendActionBarToAllCloudPlayers() {
        ICommunicationPromise<List<SimpleCloudPlayer>> promise = CloudAPI.getInstance().getCloudPlayerManager().getAllOnlinePlayers();
        promise.then(simpleCloudPlayers -> {
            return list = simpleCloudPlayers;
        });
        list.forEach(simpleCloudPlayer -> {
            simpleCloudPlayer.getCloudPlayer().then(players::add);
        });
        players.forEach(cloudPlayer -> {
            if (cloudPlayer != null) {
                cloudPlayer.sendActionBar(this.message);
            }
        });
    }

    public void sendActionBarToAllOnlinePlayers() {
        Collection<? extends Player> bukkitPlayers = Bukkit.getOnlinePlayers();
        bukkitPlayers.forEach(all -> {
            ICommunicationPromise<ICloudPlayer> promise = CloudAPI.getInstance().getCloudPlayerManager().getCloudPlayer(all.getUniqueId());
            promise.then(cloudPlayer -> {
                return players.add(cloudPlayer);
            });
        });
        players.forEach(cloudPlayer -> {
            if (cloudPlayer != null) {
                cloudPlayer.sendActionBar(this.message);
            }
        });
    }

}
