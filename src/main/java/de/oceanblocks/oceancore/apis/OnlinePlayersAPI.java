package de.oceanblocks.oceancore.apis;

import de.oceanblocks.oceancore.Oceancore;
import de.oceanblocks.oceancore.driver.DriverAPI;
import de.oceanblocks.oceancore.driver.MySQL;

public class OnlinePlayersAPI {

    private final MySQL mySQL = Oceancore.getCloudSQL();
    private final DriverAPI driverAPI = Oceancore.getDriverAPI();

    public boolean playerExists(String UUID) {
        return driverAPI.dataInRowExists("UUID", "OnlinePlayers", UUID, mySQL);
    }

    public void createPlayer(String UUID) {
        if(!playerExists(UUID))mySQL.update("INSERT INTO OnlinePlayers(UUID, Online) VALUES ('" + UUID + "', 'true')");
    }

    public void setPlayerOnlineState(String UUID, boolean online) {
        if(playerExists(UUID)) {
            mySQL.update("UPDATE OnlinePlayers SET Online='" + online + "' WHERE UUID='" + UUID + "'");
        }else {
            createPlayer(UUID);
        }
    }

    public boolean isPlayerOnline(String UUID) {
        return driverAPI.getBoolean("Online", "OnlinePlayers", "UUID", UUID, mySQL);
    }

}
