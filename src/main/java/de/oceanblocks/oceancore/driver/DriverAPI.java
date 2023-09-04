package de.oceanblocks.oceancore.driver;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DriverAPI {

    /**
     * Gets an boolean neither or not a player exists in a database.
     *
     * @param row
     * Must be an string, defines the SELECT.
     * @param table
     * Table of your mysql.
     * @param mySQL
     * MySQL object.
     * @return
     * An boolean neither or not the player exists.
     */

    public boolean dataInRowExists(String row, String table, String input, MySQL mySQL) {
        ResultSet resultSet = mySQL.getResult("SELECT " + row + " FROM " + table + " WHERE " + row + "='" + input + "'");

        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    return true;
                }
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        }
        return false;
    }


    /**
     * Create data into mysql
     *
     * @param insert
     * mysql query.
     * @param mySQL
     * MySQL object.
     */

    public void createData(String insert, MySQL mySQL) {
        mySQL.update(insert);
    }

    public List<UUID> getAllUUIDs(MySQL mySQL) {
        List<UUID> uuids = new ArrayList<>();
        try {
            ResultSet rs = mySQL.getResult("SELECT UUID FROM Players");
            while (rs.next()) {
                String uuidString = rs.getString("UUID");
                UUID uuid = UUID.fromString(uuidString);
                uuids.add(uuid);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return uuids;
    }

    /**
     * Get a string out of any mysql backend you have. (Requires mysql object )
     *
     * @param row
     * string for row in mysql database.
     * @param table
     * table in your mysql database.
     * @param get
     * WHERE getter.
     * @param mySQL
     * MySQL object.
     * @return
     * returns a string.
     */

    public String getString(String row, String table, String get, String input, MySQL mySQL) {
        ResultSet rs = mySQL.getResult("SELECT " + row + " FROM " + table + " WHERE " + get + "='" + input + "'");
        String result = "null";

        try {
            if (rs != null) {
                while (rs.next()) {
                    result = rs.getString(row);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return result;
    }

    public ArrayList<String> getStringList(String get, String table, MySQL mySQL) {
        ResultSet rs = mySQL.getResult("SELECT " + get + " FROM " + table);
        ArrayList<String> list = new ArrayList<>();
        try {
            while (rs.next()) {
                list.add(rs.getString(get));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return list;
    }

    /**
     * Set a string into any mysql database.
     *
     * @param row
     * string for row in mysql database.
     * @param table
     * table in your mysql database.
     * @param get
     * WHERE getter.
     * @param mySQL
     * MySQL object.
     */

    public void setString(String row, String table, String get, String set, String input, MySQL mySQL) {
        if (dataInRowExists(row, table, input, mySQL)) {
            mySQL.update("UPDATE " + table + " SET " + row + "='" + set + "' WHERE " + get + "='" + input + "'");
        }
    }

    /**
     * Get a int out of any mysql backend you have. (Requires mysql object )
     *
     * @param row
     * string for row in mysql database.
     * @param table
     * table in your mysql database.
     * @param get
     * WHERE getter.
     * @param mySQL
     * MySQL object.
     * @return
     * returns a int.
     */

    public int getInteger(String row, String table, String get, String input, MySQL mySQL) {
        ResultSet rs = mySQL.getResult("SELECT " + row + " FROM " + table + " WHERE " + get + "='" + input + "'");
        int i = 0;

        try {
            if (rs != null) {
                while (rs.next()) {
                    i = rs.getInt(row);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return i;
    }

    /**
     * Set a int into any mysql database.
     *
     * @param row
     * string for row in mysql database.
     * @param table
     * table in your mysql database.
     * @param get
     * WHERE getter.
     * @param mySQL
     * MySQL object.
     */

    public void setInteger(String row, String table, String get, int set, String input, MySQL mySQL) {
        if (dataInRowExists(row, table, input, mySQL)) {
            mySQL.update("UPDATE " + table + " SET " + row + "='" + set + "' WHERE " + get + "='" + input + "'");
        }
    }


    /**
     * Get a long out of any mysql backend you have. (Requires mysql object )
     *
     * @param row
     * string for row in mysql database.
     * @param table
     * table in your mysql database.
     * @param get
     * WHERE getter.
     * @param mySQL
     * MySQL object.
     * @return
     * returns a long.
     */

    public long getLong(String row, String table, String get, String input, MySQL mySQL) {
        ResultSet rs = mySQL.getResult("SELECT " + row + " FROM " + table + " WHERE " + get + "='" + input + "'");
        long i = 0;

        try {
            if (rs != null) {
                while (rs.next()) {
                    i = rs.getLong(row);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return i;
    }

    /**
     * Set a long into any mysql database.
     *
     * @param row
     * string for row in mysql database.
     * @param table
     * table in your mysql database.
     * @param get
     * WHERE getter.
     * @param mySQL
     * MySQL object.
     */

    public void setLong(String row, String table, String get, long set, String input, MySQL mySQL) {
        if (dataInRowExists(row, table, input, mySQL)) {
            mySQL.update("UPDATE " + table + " SET " + row + "='" + set + "' WHERE " + get + "='" + input + "'");
        }
    }


    /**
     * Get a uuid out of any mysql backend you have. (Requires mysql object )
     *
     * @param row
     * string for row in mysql database.
     * @param table
     * table in your mysql database.
     * @param get
     * WHERE getter.
     * @param mySQL
     * MySQL object.
     * @return
     * returns a uuid.
     */


    public UUID getUUID(String row, String table, String get, String input, MySQL mySQL) {
        ResultSet rs = mySQL.getResult("SELECT " + row + " FROM " + table + " WHERE " + get + "='" + input + "'");
        UUID uuid = null;

        try {
            if (rs != null) {
                while (rs.next()) {
                    uuid = UUID.fromString(rs.getString(row));
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return uuid;
    }

    /**
     * Set a uuid into any mysql database.
     *
     * @param row
     * string for row in mysql database.
     * @param table
     * table in your mysql database.
     * @param get
     * WHERE getter.
     * @param mySQL
     * MySQL object.
     */

    public void setUUID(String row, String table, String get, UUID set, String input, MySQL mySQL) {
        if (dataInRowExists(row, table, input, mySQL)) {
            mySQL.update("UPDATE " + table + " SET " + row + "='" + set + "' WHERE " + get + "='" + input + "'");
        }
    }

    /**
     * Get a boolean out of any mysql backend you have. (Requires mysql object )
     *
     * @param row
     * string for row in mysql database.
     * @param table
     * table in your mysql database.
     * @param get
     * WHERE getter.
     * @param mySQL
     * MySQL object.
     * @return
     * returns a boolean.
     */

    public boolean getBoolean(String row, String table, String get, String input, MySQL mySQL) {
        ResultSet rs = mySQL.getResult("SELECT " + row + " FROM " + table + " WHERE " + get + "='" + input + "'");
        boolean b = false;

        try {
            if (rs != null) {
                while (rs.next()) {
                    b = rs.getBoolean(row);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return b;
    }

    /**
     * Set a boolean into any mysql database.
     *
     * @param row
     * string for row in mysql database.
     * @param table
     * table in your mysql database.
     * @param get
     * WHERE getter.
     * @param mySQL
     * MySQL object.
     */

    public void setBoolean(String row, String table, String get, boolean set, String input, MySQL mySQL) {
        if (dataInRowExists(row, table, input, mySQL)) {
            mySQL.update("UPDATE " + table + " SET " + row + "='" + set + "' WHERE " + get + "='" + input + "'");
        }
    }

    /**
     * Get a double out of any mysql backend you have. (Requires mysql object )
     *
     * @param row
     * string for row in mysql database.
     * @param table
     * table in your mysql database.
     * @param get
     * WHERE getter.
     * @param mySQL
     * MySQL object.
     * @return
     * returns a double.
     */

    public double getDouble(String row, String table, String get, String input, MySQL mySQL) {
        ResultSet rs = mySQL.getResult("SELECT " + row + " FROM " + table + " WHERE " + get + "='" + input + "'");
        double d = 0.0;

        try {
            if (rs != null) {
                while (rs.next()) {
                    d = rs.getDouble(row);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return d;
    }

    /**
     * Set a double into any mysql database.
     *
     * @param row
     * string for row in mysql database.
     * @param table
     * table in your mysql database.
     * @param get
     * WHERE getter.
     * @param mySQL
     * MySQL object.
     */

    public void setDouble(String row, String table, String get, double set, String input, MySQL mySQL) {
        if (dataInRowExists(row, table, input, mySQL)) {
            mySQL.update("UPDATE " + table + " SET " + row + "='" + set + "' WHERE " + get + "='" + input + "'");
        }
    }

    /**
     * Get a float out of any mysql backend you have. (Requires mysql object )
     *
     * @param row
     * string for row in mysql database.
     * @param table
     * table in your mysql database.
     * @param get
     * WHERE getter.
     * @param mySQL
     * MySQL object.
     * @return
     * returns a double.
     */

    public float getFloat(String row, String table, String get, String input, MySQL mySQL) {
        ResultSet rs = mySQL.getResult("SELECT " + row + " FROM " + table + " WHERE " + get + "='" + input + "'");
        float f = 0;

        try {
            if (rs != null) {
                while (rs.next()) {
                    f = rs.getFloat(row);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return f;
    }

    /**
     * Set a float into any mysql database.
     *
     * @param row
     * string for row in mysql database.
     * @param table
     * table in your mysql database.
     * @param get
     * WHERE getter.
     * @param mySQL
     * MySQL object.
     */

    public void setFloat(String row, String table, String get, float set, String input, MySQL mySQL) {
        if (dataInRowExists(row, table, input, mySQL)) {
            mySQL.update("UPDATE " + table + " SET " + row + "='" + set + "' WHERE " + get + "='" + input + "'");
        }
    }

    /**
     * Get a object out of any mysql backend you have. (Requires mysql object )
     *
     * @param row
     * string for row in mysql database.
     * @param table
     * table in your mysql database.
     * @param get
     * WHERE getter.
     * @param mySQL
     * MySQL object.
     * @return
     * returns a object.
     */

    public Object getObject(String row, String table, String get, String input, MySQL mySQL) {
        ResultSet rs = mySQL.getResult("SELECT " + row + " FROM " + table + " WHERE " + get + "='" + input + "'");
        Object result = null;

        try {
            if (rs != null) {
                while (rs.next()) {
                    result = rs.getObject(row);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return result;
    }

    /**
     * Set a object into any mysql database.
     *
     * @param row
     * string for row in mysql database.
     * @param table
     * table in your mysql database.
     * @param get
     * WHERE getter.
     * @param mySQL
     * MySQL object.
     */

    public void setObject(String row, String table, String get, String set, String input, MySQL mySQL) {
        if (dataInRowExists(row, table, input, mySQL)) {
            mySQL.update("UPDATE " + table + " SET " + row + "='" + set + "' WHERE " + get + "='" + input + "'");
        }
    }

    /**
     * Get the date out of any mysql backend you have. (Requires mysql object )
     *
     * @param row
     * string for row in mysql database.
     * @param table
     * table in your mysql database.
     * @param get
     * WHERE getter.
     * @param mySQL
     * MySQL object.
     * @return
     * returns an object.
     */

    public java.sql.Date getDate(String row, String table, String get, String input, MySQL mySQL) {
        ResultSet rs = mySQL.getResult("SELECT " + row + " FROM " + table + " WHERE " + get + "='" + input + "'");
        java.sql.Date result = null;

        try {
            if (rs != null) {
                while (rs.next()) {
                    result = rs.getDate(row);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return result;
    }

    /**
     * Set an object into any mysql database.
     *
     * @param row
     * string for row in mysql database.
     * @param table
     * table in your mysql database.
     * @param get
     * WHERE getter.
     * @param mySQL
     * MySQL object.
     */

    public void setDate(String row, String table, Date set, String get, String input, MySQL mySQL) {
        if (dataInRowExists(row, table, input, mySQL)) {
            mySQL.update("UPDATE " + table + " SET " + row + "='" + set + "' WHERE " + get + "='" + input + "'");
        }
    }

}
