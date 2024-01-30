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
package net.jadedmc.jadedcore.player;

import net.jadedmc.jadedcore.JadedCorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * This class manages the Jaded Player object, which stores general data for players, such as their current rank.
 */
public class JadedPlayerManager {
    private final JadedCorePlugin plugin;
    private final Map<Player, JadedPlayer> jadedPlayers = new HashMap<>();

    /**
     * Initializes the Jaded Player Manager.
     * @param plugin Instance of the plugin.
     */
    public JadedPlayerManager(final JadedCorePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Add a player to the player list.
     * @param player Player to add.
     * @return JadedPlayer completable future.
     */
    public CompletableFuture<JadedPlayer> addPlayer(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            JadedPlayer jadedPlayer = new JadedPlayer(plugin, player);
            jadedPlayers.put(player, jadedPlayer);
            return jadedPlayer;
        });
    }

    /**
     * Get the JadedPlayer of a player
     * @param player Player to get JadedPlayer of.
     * @return JadedPlayer of the player.
     */
    public JadedPlayer getPlayer(Player player) {
        return jadedPlayers.get(player);
    }

    /**
     * Get a JadedPlayer from a UUID.
     * @param uuid UUID of the player.
     * @return JadedPlayer of the player.
     */
    public JadedPlayer getPlayer(UUID uuid) {
        return this.getPlayer(Bukkit.getPlayer(uuid));
    }

    /**
     * Get all currently saved JadedPlayers
     * @return Collection of JadedPlayers
     */
    public Collection<JadedPlayer> getJadedPlayers() {
        return jadedPlayers.values();
    }

    /**
     * Remove a player from the player list.
     * @param player Player to remove.
     */
    public void removePlayer(Player player) {
        jadedPlayers.remove(player);
    }
}