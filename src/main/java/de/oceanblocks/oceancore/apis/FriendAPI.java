package de.oceanblocks.oceancore.apis;

import de.oceanblocks.oceancore.Oceancore;
import de.oceanblocks.oceancore.driver.DriverAPI;
import de.oceanblocks.oceancore.driver.MySQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FriendAPI {

    private final MySQL mySQL = Oceancore.getCloudSQL();
    private final DriverAPI driverAPI = Oceancore.getDriverAPI();

    public boolean playerExists(String UUID) {
        return driverAPI.dataInRowExists("UUID", "FriendSystem", UUID, mySQL);
    }

    public void createPlayer(String UUID, String playerName) {
        if(!playerExists(UUID))mySQL.update("INSERT INTO FriendSystem(UUID, USERNAME, FRIENDS, REQUESTS, SETTINGS, LASTLOGIN, FIRSTLOGIN) VALUES ('" + UUID + "', '" + playerName + "', '', '', '', '" +
                System.currentTimeMillis() + "', '" + System.currentTimeMillis() + "')");
    }

    public boolean nameExists(String playerName) {
        return driverAPI.dataInRowExists("USERNAME", "FriendSystem", playerName, mySQL);
    }

    public String getUUID(String playerName) {
        return driverAPI.getString("UUID", "FriendSystem", "USERNAME", playerName, mySQL);
    }

    public String getName(String UUID) {
        return driverAPI.getString("USERNAME", "FriendSystem", "UUID", UUID, mySQL);
    }

    public void updatePlayer(String UUID, String newPlayerName) {
        if(playerExists(UUID)) {
            mySQL.update("UPDATE FriendSystem SET USERNAME='" + newPlayerName + "' WHERE UUID='" + UUID + "'");
            mySQL.update("UPDATE FriendSystem SET LASTLOGIN='" + System.currentTimeMillis() + "' WHERE UUID='" + UUID + "'");
        }
    }

    public void updateFriendString(String UUID, String newString) {
        if (playerExists(UUID))
            mySQL.update("UPDATE FriendSystem SET FRIENDS='" + newString + "' WHERE UUID='" + UUID + "'");
    }

    public String getRAWFriends(String UUID) {
        if (playerExists(UUID)) return driverAPI.getString("FRIENDS", "FriendSystem", "UUID", UUID, mySQL);
        return null;
    }

    public void addFriend(String UUID, String FriendUUID) {
        if (getRAWFriends(UUID) != null) {
            String newString = getRAWFriends(UUID) + FriendUUID + ";";
            updateFriendString(UUID, newString);
        } else {
            updateFriendString(UUID, FriendUUID + ";");
        }
    }

    public void deleteFriend(String UUID, String FriendUUID) {
        String newString = getRAWFriends(UUID).replace(FriendUUID + ";", "");
        updateFriendString(UUID, newString);
    }

    public void updateRequestString(String UUID, String newString) {
        if (playerExists(UUID)) mySQL.update("UPDATE FriendSystem SET REQUESTS='" + newString + "' WHERE UUID='" + UUID + "'");
    }

    public String getRAWRequests(String UUID) {
        if (playerExists(UUID)) return driverAPI.getString("REQUESTS", "FriendSystem", "UUID", UUID, mySQL);
        return null;
    }

    public void addRequest(String UUID, String FriendUUID) {
        if (getRAWRequests(UUID) != null) {
            String newString = getRAWRequests(UUID) + FriendUUID + ";";
            updateRequestString(UUID, newString);
        } else {
            updateRequestString(UUID, FriendUUID + ";");
        }
    }

    public void delRequest(String UUID, String FriendUUID) {
        String newString = getRAWRequests(UUID).replace(FriendUUID + ";", "");
        updateRequestString(UUID, newString);
    }

    public boolean hasRequest(String UUID, String FriendUUID) {
        if (getRAWRequests(UUID) != null) {
            if (getRAWRequests(UUID).contains(FriendUUID))
                return true;
            return false;
        }
        return false;
    }

    public boolean isFriend(String UUID, String FriendUUID) {
        if (getRAWFriends(UUID) != null) {
            if (getRAWFriends(UUID).contains(FriendUUID))
                return true;
            return false;
        }
        return false;
    }

    public void updateSettingsString(String UUID, String newString) {
        if (playerExists(UUID))
            mySQL.update("UPDATE FriendSystem SET SETTINGS='" + newString + "' WHERE UUID='" + UUID + "'");
    }

    public String getRAWSettings(String UUID) {
        if (playerExists(UUID)) return driverAPI.getString("SETTINGS", "FriendSystem", "UUID", UUID, mySQL);
        return null;
    }

    public void addSetting(String UUID, String Setting) {
        if (getRAWSettings(UUID) != null) {
            String newString = getRAWSettings(UUID) + Setting + ";";
            updateSettingsString(UUID, newString);
        } else {
            updateSettingsString(UUID, Setting + ";");
        }
    }

    public void delSetting(String UUID, String Setting) {
        String newString = getRAWSettings(UUID).replace(Setting + ";", "");
        updateSettingsString(UUID, newString);
    }

    public boolean hasSetting(String UUID, String Setting) {
        if (getRAWSettings(UUID) != null) {
            return getRAWSettings(UUID).contains(Setting);
        }
        return false;
    }

    public List<String> getFriendsList(String UUID) {
        List<String> friendlist = new ArrayList<>();
        if (getRAWFriends(UUID).isEmpty())
            return friendlist;
        String[] friends = getRAWFriends(UUID).split(";");
        friendlist.addAll(Arrays.asList(friends));
        return friendlist;
    }

    public List<String> getRequestsList(String UUID) {
        List<String> reqlist = new ArrayList<>();
        if (getRAWRequests(UUID).isEmpty())
            return reqlist;
        String[] reqs = getRAWRequests(UUID).split(";");
        reqlist.addAll(Arrays.asList(reqs));
        return reqlist;
    }

    public Integer countFriends(String UUID) {
        return getFriendsList(UUID).size();
    }
    public ArrayList<String> getNames() {
        return driverAPI.getStringList("USERNAME", "FriendSystem", mySQL);
    }


    public Integer countRequests(String UUID) {
        return getRequestsList(UUID).size();
    }



}
