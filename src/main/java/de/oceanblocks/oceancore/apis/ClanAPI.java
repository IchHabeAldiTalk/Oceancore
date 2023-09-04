package de.oceanblocks.oceancore.apis;

import de.oceanblocks.oceancore.Oceancore;
import de.oceanblocks.oceancore.driver.DriverAPI;
import de.oceanblocks.oceancore.driver.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ClanAPI {

    private final MySQL mySQL = Oceancore.getCloudSQL();
    private final DriverAPI driverAPI = Oceancore.getDriverAPI();

    public boolean isPlayerInClan(UUID uuid) {
        return driverAPI.dataInRowExists("UUID", "ClanSystem", uuid.toString(), mySQL);
    }

    public boolean isPlayerInClan(String playerName) {
        return driverAPI.dataInRowExists("NAME", "ClanSystem", playerName, mySQL);
    }

    public boolean isClanNameExists(String clanName) {
        return driverAPI.dataInRowExists("CLANNAME", "ClanSystem", clanName, mySQL);
    }

    public boolean isClanTagExists(String clanTag) {
        return driverAPI.dataInRowExists("CLANTAG", "ClanSystem", clanTag, mySQL);
    }

    public void addPlayer(int playerID, int clanID, UUID uuid, String playerName, String clanName, String clanTag, String rank) {
        mySQL.update("INSERT INTO ClanSystem(PLAYERID, CLANID, UUID, NAME, CLANNAME, CLANTAG, RANK) VALUES (" + playerID + "," + clanID + ",'"+ uuid + "','" + playerName + "','" + clanName + "','" + clanTag + "','" + rank + "')");
    }

    public void removePlayer(String playerName) {
        mySQL.update("DELETE FROM ClanSystem WHERE NAME = '" + playerName + "'");
    }

    public void removePlayer(UUID uuid) {
        mySQL.update("DELETE FROM ClanSystem WHERE UUID = '" + uuid + "'");
    }

    public int getClanID(String playerName) {
        return driverAPI.getInteger("CLANID", "ClanSystem", "NAME", playerName, mySQL);
    }

    public int getClanID(UUID uuid) {
        return driverAPI.getInteger("CLANID", "ClanSystem", "UUID", uuid.toString(), mySQL);
    }

    public String getClanName(int clanID) {
        return driverAPI.getString("CLANNAME", "ClanSystem", "CLANID", String.valueOf(clanID),  mySQL);
    }

    public String getClanTag(int clanID) {
        return driverAPI.getString("CLANTAG", "ClanSystem", "CLANID", String.valueOf(clanID),  mySQL);
    }

    public String isClanLeader(UUID uuid) {
        return driverAPI.getString("RANK", "ClanSystem", "UUID", uuid.toString(),  mySQL);
    }

    public String isClanLeader(String playerName) {
        return driverAPI.getString("RANK", "ClanSystem", "NAME", playerName,  mySQL);
    }

    public Integer getClanIDs() {
        int clanID = 0;
        try {
            PreparedStatement ps = mySQL.getConnection().prepareStatement("SELECT * FROM ClanSystem ORDER BY CLANID DESC");
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return Integer.valueOf(rs.getInt("CLANID"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Integer.valueOf(clanID);
    }

    public String getPlayerID(int playerID) {
        return driverAPI.getString("NAME", "ClanSystem", "PLAYERID", String.valueOf(playerID), mySQL);
    }

    public int getPlayerIDs() {
        int clanID = 0;
        try {
            PreparedStatement ps = mySQL.getConnection().prepareStatement("SELECT * FROM ClanSystem ORDER BY PLAYERID DESC");
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt("PLAYERID");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clanID;
    }

    public void updateClanRank(UUID uuid, String rank) {
        mySQL.update("UPDATE ClanSystem SET RANK = ' " + rank + "' WHERE UUID = '" + uuid + "'");
    }

    public void addPlayerName(String playerName, UUID uuid) {
        mySQL.update("UPDATE ClanSystem SET NAME = '" + playerName + "' WHERE UUID = '" + uuid + "'");
    }
}
