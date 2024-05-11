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
package net.jadedmc.jadedutils.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

/**
 * This class represents a Set of CustomPlayer objects.
 * Contains extra methods for comparing cached information inside the CustomPlayer objects.
 */
public class CustomPlayerSet<T> extends HashSet<T> {

    /**
     * Converts all the online CustomPlayers to Bukkit Players.
     * @return Collection of Bukkit Players.
     */
    public Collection<Player> asBukkitPlayers() {
        final Collection<Player> bukkitPlayers = new HashSet<>();

        // Loop through all objects.
        for(final T object : this) {
            // If it's not a CustomPlayer, skip it.
            if(!(object instanceof final CustomPlayer customPlayer)) {
                continue;
            }

            // Gets the Bukkit player being represented.
            final Player player = Bukkit.getPlayer(customPlayer.getUniqueId());

            // Skip them if they are not online.
            if(player == null || !player.isOnline()) {
                continue;
            }

            // Otherwise, add them to the Set.
            bukkitPlayers.add(player);
        }

        return bukkitPlayers;
    }

    /**
     * Get a CustomPlayer object for a player in the set.
     * @param uuid UUID of the player to get CustomPlayer of.
     * @return Associated CustomPlayer.
     */
    public T getPlayer(@NotNull final UUID uuid) {
        for(final Object object : this) {
            // Make sure the object is a custom player.
            if(!(object instanceof final CustomPlayer customPlayer)) {
                return null;
            }

            // If the UUID matches, return that object.
            if(customPlayer.getUniqueId().equals(uuid)) {
                return (T) customPlayer;
            }
        }
        return null;
    }

    /**
     * Get a CustomPlayer object for a player in the set from their username.
     * @param username Username of the player to get CustomPlayer of.
     * @return Associated CustomPlayer.
     */
    public T getPlayer(@NotNull final String username) {
        for(final Object object : this) {
            // Make sure the object is a custom player.
            if(!(object instanceof final CustomPlayer customPlayer)) {
                return null;
            }

            // If the username matches, return that object.
            if(customPlayer.getName().equalsIgnoreCase(username)) {
                return (T) customPlayer;
            }
        }

        return null;
    }

    /**
     * Get a CustomPlayer object of a player in the set.
     * @param player Player to get CustomPlayer of.
     * @return Associated CustomPlayer.
     */
    public T getPlayer(@NotNull final Player player) {
        return getPlayer(player.getUniqueId());
    }

    /**
     * Get a CustomPlayer object of a player in the set.
     * @param customPlayer Player to get CustomPlayer of.
     * @return Associated CustomPlayer.
     */
    public T getPlayer(@NotNull final CustomPlayer customPlayer) {
        return getPlayer(customPlayer.getUniqueId());
    }

    /**
     * Gets all CustomPlayers that are on this instance.
     * @return Collection of online CustomPlayers.
     */
    public CustomPlayerSet<T> getOnlinePlayers() {
        final CustomPlayerSet<T> onlinePlayers = new CustomPlayerSet<>();

        // Loop through all objects.
        for(final T object : this) {
            // If it's not a CustomPlayer, skip it.
            if(!(object instanceof final CustomPlayer customPlayer)) {
                continue;
            }

            // Gets the Bukkit player being represented.
            final Player player = Bukkit.getPlayer(customPlayer.getUniqueId());

            // Skip them if they are not online.
            if(player == null || !player.isOnline()) {
                continue;
            }

            // Otherwise, add them to the Set.
            onlinePlayers.add((T) customPlayer);
        }

        return onlinePlayers;
    }

    /**
     * Check if the set contains a player with a given UUID.
     * @param uuid UUID of the player to check.
     * @return Whether the set has the player.
     */
    public boolean hasPlayer(@NotNull final UUID uuid) {
        for(final Object object : this) {
            // Make sure the object is a custom player.
            if(!(object instanceof final CustomPlayer customPlayer)) {
                return false;
            }

            // If the UUID matches, return that object.
            if(customPlayer.getUniqueId().equals(uuid)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if the set contains a player with a given username.
     * @param username Username of the player to check.
     * @return Whether the set has the player.
     */
    public boolean hasPlayer(@NotNull final String username) {
        for(final Object object : this) {
            // Make sure the object is a custom player.
            if(!(object instanceof final CustomPlayer customPlayer)) {
                return false;
            }

            // If the username matches, return that object.
            if(customPlayer.getName().equalsIgnoreCase(username)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if the set contains a given player.
     * @param player Player to check.
     * @return Whether the set has that player.
     */
    public boolean hasPlayer(@NotNull final Player player) {
        return hasPlayer(player.getUniqueId());
    }

    /**
     * Check if the set contains a given player, via one of their CustomPlayer objects.
     * @param customPlayer Player to check.
     * @return Whether the set has that player.
     */
    public boolean hasPlayer(@NotNull final CustomPlayer customPlayer) {
        return hasPlayer(customPlayer.getUniqueId());
    }

    /**
     * Remove a given player from the set from their UUID.
     * @param playerUUID UUID of the player you want to remove.
     */
    public void removePlayer(@NotNull final UUID playerUUID) {
        this.remove(getPlayer(playerUUID));
    }

    /**
     * Remove a given player from the set.
     * @param player Player to remove.
     */
    public void removePlayer(@NotNull final Player player) {
        removePlayer(player.getUniqueId());
    }

    /**
     * Remove a given player from the set, from a representative CustomPlayer object.
     * @param customPlayer Player to remove.
     */
    public void removePlayer(@NotNull final CustomPlayer customPlayer) {
        removePlayer(customPlayer.getUniqueId());
    }
}