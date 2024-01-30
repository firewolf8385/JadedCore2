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
package net.jadedmc.jadedcore.achievements;

import net.jadedmc.jadedcore.JadedCorePlugin;
import net.jadedmc.jadedcore.games.Game;
import net.jadedmc.jadedcore.player.JadedPlayer;
import net.jadedmc.jadedutils.chat.ChatUtils;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a custom achievement made by the server.
 */
public class Achievement {
    private final JadedCorePlugin plugin;
    private final Game game;
    private final String id;
    private final String name;
    private final String description;
    private final int points;
    private final List<String> rewards = new ArrayList<>();

    /**
     * Creates the Achievement.
     * @param plugin Instance of the plugin.
     * @param game Game the achievement is for.
     * @param id ID of the achievement.
     * @param name Name of the achievement.
     * @param description Description of the achievement.
     * @param points Number of Achievement Points that should be awarded.
     * @param rewardsString Extra rewards.
     */
    public Achievement(final JadedCorePlugin plugin, final Game game, final String id, final String name, final String description, final int points, final String rewardsString) {
        this.plugin = plugin;
        this.game = game;
        this.id = id;
        this.name = name;
        this.description = description;
        this.points = points;

        // Add the rewards.
        rewards.add("<yellow>" + points + " Achievement Points</yellow>");
        rewards.addAll(Arrays.asList(rewardsString.split(";")));
    }

    /**
     * Get the Game the achievement is for.
     * @return Achievement game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Get the name of the achievement.
     * @return Achievement name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the description of the achievement.
     * @return Achievement description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the points of the achievement.
     * @return Achievement points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Get the ID of the achievement.
     * @return Achievement id.
     */
    public String getID() {
        return id;
    }

    /**
     * Get the rewards of the achievement.
     * @return Achievement rewards.
     */
    public List<String> getRewards() {
        return rewards;
    }

    /**
     * Unlocks the achievement for a player.
     * @param player Player to unlock achievement for.
     * @return Whether the achievement was unlocked.
     */
    public boolean unlock(Player player) {
        JadedPlayer jadedPlayer = plugin.jadedPlayerManager().getPlayer(player);

        // Exit if they already have the achievement.
        if(jadedPlayer.getAchievements().contains(this)) {
            return false;
        }

        jadedPlayer.getAchievements().add(this);

        StringBuilder rewardsString = new StringBuilder();

        for(String reward : rewards) {
            rewardsString.append("<newline>  ").append(reward);
        }

        // Send the unlock message.
        String message = "<yellow><obf>#</obf><green>>> Achievement Unlocked: <hover:show_text:'<green>" + name + "</green><newline><gray>" + description + "</gray><newline><newline><gray>Rewards:</gray>" + rewardsString + "'><gold>" + name  + "</gold></hover> <green><<<yellow><obf>#";
        ChatUtils.chat(player, message);

        // Update MySQL.
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("INSERT INTO player_achievements (uuid,achievementID) VALUES (?,?)");
                statement.setString(1, player.getUniqueId().toString());
                statement.setString(2, id);
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });

        return true;
    }
}