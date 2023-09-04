package de.oceanblocks.oceancore.listener;

import de.oceanblocks.oceancore.Oceancore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        Oceancore.getInstance().getUPlayerCache().insertCache(player);
    }

}
