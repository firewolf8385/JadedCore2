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
package net.jadedmc.jadedcore.lobby;

import me.clip.placeholderapi.PlaceholderAPI;
import net.jadedmc.jadedcore.JadedCorePlugin;
import net.jadedmc.jadedutils.scoreboard.CustomScoreboard;
import net.jadedmc.jadedutils.scoreboard.ScoreHelper;
import org.bukkit.entity.Player;

import java.util.List;

public class LobbyScoreboard extends CustomScoreboard {
    private final JadedCorePlugin plugin;

    /**
     * Links the player with the scoreboard.
     * @param plugin Instance of the plugin.
     * @param player Player to create scoreboard for.
     */
    public LobbyScoreboard(final JadedCorePlugin plugin, Player player) {
        super(player);
        this.plugin = plugin;

        CustomScoreboard.getPlayers().put(player.getUniqueId(), this);
        update(player);
    }

    /**
     * Updates the scoreboard for a specific player.
     * @param player Player to update scoreboard for.
     */
    public void update(Player player) {
        ScoreHelper helper;

        if(ScoreHelper.hasScore(player)) {
            helper = ScoreHelper.getByPlayer(player);
        }
        else {
            helper = ScoreHelper.createScore(player);
        }

        // Sets up the scoreboard.
        helper.setTitle(plugin.settingsManager().getConfig().getString("Lobby.Scoreboard.Title"));

        List<String> lines = plugin.settingsManager().getConfig().getStringList("Lobby.Scoreboard.Lines");

        int lineNumber = 15;
        for(String line : lines) {
            helper.setSlot(lineNumber, PlaceholderAPI.setPlaceholders(player, line));

            lineNumber--;
        }

        for(int i = 0; i <= lineNumber; i++) {
            helper.removeSlot(i);
        }
    }
}