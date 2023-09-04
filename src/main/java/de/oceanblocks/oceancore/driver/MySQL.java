package de.oceanblocks.oceancore.driver;

import de.oceanblocks.oceancore.Oceancore;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MySQL {

    private final String host;
    private final String user;
    private final String password;
    private final String database;
    private final String port;
    @Getter
    private boolean autoReconnect;
    private Connection con;
    private int scheduler;
    private boolean alreadyConnected = false;

    public MySQL(String host, String user, String password, String database, String port, boolean autoReconnect) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.database = database;
        this.port = port;
        this.autoReconnect = autoReconnect;
    }

    public void connect() {
        if (con == null)
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + user + "&password=" + password + "&autoReconnect=true");
                System.out.println("Connected successfully to database " + user + "@" + host + " via " + getClass().getSimpleName());
                if (autoReconnect) {
                    if (!alreadyConnected) {
                        reconnect();
                        alreadyConnected = true;
                    }
                }
            } catch (SQLException | ClassNotFoundException e) {
                System.out.println("Failed to connect to database:");
                e.printStackTrace();
            }
    }

    public Connection getConnection() {
        return con;
    }

    public void close()
    {
        if (con != null)
            try {
                con.close();
                con = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    public void update(String sql) {

        if(isConnected()) {
            new FutureTask<>(new Runnable() {

                @Override
                public void run() {
                    try {
                        Statement s = con.createStatement();
                        s.executeUpdate(sql);
                        s.close();
                    } catch (SQLException e) {
                        System.out.println("An error occurred while executing mysql update (" + e.getMessage() + ")!");
                    }
                }
            }, 1).run();
        }
    }

    public void createTable(String create) {
        update(create);
    }

    public boolean isConnected() {
        if (con != null) {
            return true;
        } else {
            return false;
        }
    }

    public ResultSet getResult(String qry) {
        if(isConnected()) {
            try {
                final FutureTask<ResultSet> task = new FutureTask<ResultSet>(new Callable<>() {

                    PreparedStatement ps;

                    @Override
                    public ResultSet call() throws Exception {
                        ps = con.prepareStatement(qry);

                        return ps.executeQuery();
                    }
                });

                task.run();

                return task.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            connect();
        }

        return null;
    }

    public void reconnect() {
        if (autoReconnect) {
            Oceancore.getInstance().getLogger().info("Started auto-reconnect for mysql constructor.");
            this.scheduler = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Oceancore.getInstance(), () -> {
                Oceancore.getInstance().getLogger().info("Disconnecting from mysql for " + getClass() + "...");
                close();
                connect();
            }, 20L * 60L * 60L * 6L, 20L * 60L * 60L * 6L);
        }
    }

    public void shutdownReconnect() {
        if (autoReconnect) {
            Bukkit.getScheduler().cancelTask(this.scheduler);
        } else {
            Oceancore.getInstance().getLogger().warning("Auto-reconnect was not enabled for " + getClass());
        }
    }

    public String getDatabase() {
        return database;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public int getScheduler() {
        return scheduler;
    }

    public void setAutoReconnect(boolean reconnect) {
        this.autoReconnect = reconnect;
    }
}
