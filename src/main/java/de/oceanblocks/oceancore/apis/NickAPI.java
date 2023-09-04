package de.oceanblocks.oceancore.apis;

import de.oceanblocks.oceancore.Oceancore;
import de.oceanblocks.oceancore.driver.DriverAPI;
import de.oceanblocks.oceancore.driver.MySQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NickAPI {

    private final DriverAPI driverAPI = Oceancore.getDriverAPI();
    private final MySQL mySQL = Oceancore.getNickSQL();

    public boolean playerExists(String UUID) {
        return driverAPI.dataInRowExists("UUID", "NickSystem", UUID, mySQL);
    }
    public boolean isPlayerNicked(String UUID) {
        return driverAPI.dataInRowExists("UUID", "NickedPlayers", UUID, mySQL);
    }


    public void createPlayer(String UUID, String playerName) {
        if(!playerExists(UUID))mySQL.update("INSERT INTO NickSystem(UUID, PlayerName, AutoNick, NickType) VALUES ('" + UUID + "', '" + playerName + "', 'false', 'Spieler')");
    }

    public void removePlayer(String UUID) {
        if(playerExists(UUID)) {
            mySQL.update("DELETE FROM NickSystem WHERE UUID='" + UUID + "'");
        }
    }

    public String getRealName(String UUID) {
        return driverAPI.getString("Realname", "NickedPlayers", "UUID", UUID, mySQL);
    }

    public String getNickName(String UUID) {
        return driverAPI.getString("Nickname", "NickedPlayers", "UUID", UUID, mySQL);
    }

    public void addPlayerIsNicked(String UUID, String PlayerName, String NickName) {
        mySQL.update("INSERT INTO NickedPlayers(UUID, Realname, Nickname) VALUES ('" + UUID + "', '" + PlayerName + "', '" + NickName + "')");
    }

    public void removePlayerIsNicked(String UUID) {
        if(playerExists(UUID)) {
            mySQL.update("DELETE FROM NickedPlayers WHERE UUID='" + UUID + "'");
        }
    }

    public void setAutoNick(String UUID, boolean value) {
        mySQL.update("UPDATE NickSystem SET AutoNick='" + value + "' WHERE UUID='" + UUID + "'");
    }

    public void setNickType(String UUID, String value) {
        mySQL.update("UPDATE NickSystem SET NickType='" + value + "' WHERE UUID='" + UUID + "'");
    }

    public boolean getAutoNick(String UUID) {
        return driverAPI.getBoolean("AutoNick", "NickSystem", "UUID", UUID, mySQL);
    }

    public String getNickType(String UUID) {
        return driverAPI.getString("NickType", "NickSystem", "UUID", UUID, mySQL);
    }

    private String getRawNickNames () {
        return driverAPI.getString("NickNames", "NickNames", "Names", "Names", mySQL);
    }

    public List<String> getNickNamesList() {
        List<String> friendlist = new ArrayList<>();
        if (getRawNickNames().isEmpty()) return friendlist;
        String[] friends = getRawNickNames().split(";");
        friendlist.addAll(Arrays.asList(friends));
        return friendlist;
    }

    public void removeNickName(String nickName) {
        String newString = getRawNickNames().replace(nickName + ";", "");
        updateNickNames(newString);
    }

    public void updateNickNames(String newNickNames) {
            mySQL.update("UPDATE NickNames SET NickNames='" + newNickNames + "' WHERE Names='Names'");
    }

    public String getRandomNick() {
        return getNickNamesList().get((new Random()).nextInt(getNickNamesList().size()));
    }

    public void putNamesIn() {
        mySQL.update("INSERT INTO NickNames(Names, NickNames) VALUES ('Names', 'SupperLP;Gamer01;King01')");
    }
}
