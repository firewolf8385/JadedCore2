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

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.UUID;

/**
 * This class represents a Set of CustomPlayer objects.
 * Contains extra methods for comparing cached information inside the CustomPlayer objects.
 */
public class CustomPlayerSet<T> extends HashSet<T> {

    /**
     * Get a Network player file for a player in the set.
     * @param uuid UUID of the player to get Network player of.
     * @return Associated NetworkPlayer.
     */
    public CustomPlayer getPlayer(@NotNull final UUID uuid) {
        for(final Object object : this) {
            if(!(object instanceof final CustomPlayer customPlayer)) {
                return null;
            }

            if(customPlayer.getUniqueId().equals(uuid)) {
                return customPlayer;
            }
        }
        return null;
    }

    /**
     * Get a Network player object for a player in the set.
     * @param username Username of the player to get Network player of.
     * @return Associated NetworkPlayer.
     */
    public CustomPlayer getPlayer(@NotNull final String username) {
        for(final Object object : this) {
            if(!(object instanceof final CustomPlayer customPlayer)) {
                return null;
            }

            if(customPlayer.getName().equalsIgnoreCase(username)) {
                return customPlayer;
            }
        }

        return null;
    }

    /**
     * Check if the set contains a player with a given UUID.
     * @param uuid UUID of the player to check.
     * @return Whether the set has the player.
     */
    public boolean hasPlayer(@NotNull final UUID uuid) {
        for(final Object object : this) {
            if(!(object instanceof final CustomPlayer customPlayer)) {
                return false;
            }

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
            if(!(object instanceof final CustomPlayer customPlayer)) {
                return false;
            }

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
     * Remove a guven player from the set from their UUID.
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
}