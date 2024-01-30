/*
 * This file is part of JadedCore, licensed under the MIT License.
 *
 *  Copyright (c) JadedMC
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package net.jadedmc.jadedcore.databases;

import net.jadedmc.jadedcore.JadedCorePlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Manages the connection process to MySQL.
 */
public class MySQL {
    private final JadedCorePlugin plugin;
    private Connection connection;
    private final String host;
    private final String database;
    private final String username;
    private final String password;
    private final int port;

    /**
     * Loads the MySQL database connection info.
     * @param plugin Instance of the plugin.
     */
    public MySQL(JadedCorePlugin plugin) {
        this.plugin = plugin;
        host = plugin.settingsManager().getConfig().getString("MySQL.host");
        database = plugin.settingsManager().getConfig().getString("MySQL.database");
        username = plugin.settingsManager().getConfig().getString("MySQL.username");
        password = plugin.settingsManager().getConfig().getString("MySQL.password");
        port = plugin.settingsManager().getConfig().getInt("MySQL.port");
    }

    /**
     * Close a connection.
     */
    public void closeConnection() {
        if(isConnected()) {
            try {
                connection.close();
            }
            catch(SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Get the connection.
     * @return Connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Get if plugin is connected to the database.
     * @return Connected
     */
    private boolean isConnected() {
        return (connection != null);
    }

    /**
     * Open a MySQL Connection
     */
    public void openConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }

            synchronized(JadedCorePlugin.class) {
                if (connection != null && !connection.isClosed()) {
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false&characterEncoding=utf8", username, password);
            }

            // Prevents losing connection to MySQL.
            plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, ()-> {
                try {
                    connection.isValid(0);
                }
                catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }, 504000, 504000);

            {
                PreparedStatement api_keys = connection.prepareStatement("CREATE TABLE IF NOT EXISTS api_keys (" +
                        "uuid VARCHAR(36)," +
                        "apiKey VARCHAR(36)," +
                        "PRIMARY KEY(uuid, apiKey)" +
                        ");");
                api_keys.execute();
            }

            {
                PreparedStatement staff_settings = connection.prepareStatement("CREATE TABLE IF NOT EXISTS staff_settings (" +
                        "uuid VARCHAR(36)," +
                        "vanish BOOLEAN DEFAULT FALSE," +
                        "commandSpy BOOLEAN DEFAULT FALSE," +
                        "PRIMARY KEY(uuid)" +
                        ");");
                staff_settings.execute();
            }

            {
                PreparedStatement achievements_list = connection.prepareStatement("CREATE TABLE IF NOT EXISTS achievements_list (" +
                        "id VARCHAR(36)," +
                        "mode VARCHAR(36)," +
                        "name VARCHAR(36)," +
                        "description VARCHAR(128)," +
                        "achievementPoints INT DEFAULT 0, " +
                        "rewards VARCHAR(256)," +
                        "PRIMARY KEY(id)" +
                        ");");
                achievements_list.execute();
            }

            {
                PreparedStatement player_achievements = connection.prepareStatement("CREATE TABLE IF NOT EXISTS player_achievements (" +
                        "uuid VARCHAR(36)," +
                        "achievementID VARCHAR(36), " +
                        "time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "PRIMARY KEY(uuid,achievementID)" +
                        ");");
                player_achievements.execute();
            }
        }
        catch(SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }
}