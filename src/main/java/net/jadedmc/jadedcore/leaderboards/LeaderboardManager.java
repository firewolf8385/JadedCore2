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
package net.jadedmc.jadedcore.leaderboards;

import net.jadedmc.jadedcore.JadedCorePlugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manages leaderboards for various statistics.
 */
public class LeaderboardManager {
    private final JadedCorePlugin plugin;
    private final Map<String, Integer> achievementPoints = new LinkedHashMap<>();

    /**
     * Creates the manager.
     * @param plugin Instance of the plugin.
     */
    public LeaderboardManager(JadedCorePlugin plugin) {
        this.plugin = plugin;

        // Creates a task that updates the leaderboards every 20 minutes
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::update, 20*4, 20*60*20);
    }

    /**
     * Updates the leaderboards.
     */
    public void update() {
        updateAchievementPoints();
    }

    public void updateAchievementPoints() {
        try {
            achievementPoints.clear();
            PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM player_info ORDER BY achievementPoints DESC LIMIT 10");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                String player = resultSet.getString("username");
                int points = resultSet.getInt("achievementPoints");
                achievementPoints.put(player, points);
            }
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Map<String, Integer> getAchievementPointsLeaderboard() {
        return achievementPoints;
    }
}