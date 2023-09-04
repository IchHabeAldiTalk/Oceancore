package de.oceanblocks.oceancore.listener;

import de.oceanblocks.oceancore.Oceancore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        Oceancore.getInstance().getUPlayerCache().insertIntoDatabase(player);
    }

}
