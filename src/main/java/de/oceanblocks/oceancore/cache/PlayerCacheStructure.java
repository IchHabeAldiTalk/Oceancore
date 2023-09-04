package de.oceanblocks.oceancore.cache;

import de.oceanblocks.oceancore.uplayer.uPlayer;
import org.bukkit.entity.Player;

public interface PlayerCacheStructure {

    void loadCache();

    void clearCache();

    void insertCache(uPlayer player);

    void insertCache(Player player);

    void removeFromCache(uPlayer player);

    void insertIntoDatabase(Player player);

}
