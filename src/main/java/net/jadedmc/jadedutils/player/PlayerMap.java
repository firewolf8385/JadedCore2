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
 *  FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package net.jadedmc.jadedutils.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerMap<V> extends HashMap<UUID, V> {

    public void add(@NotNull final Player player) {
        this.put(player.getUniqueId(), (V) player);
    }

    public void add(@NotNull final PluginPlayer pluginPlayer) {
        this.put(pluginPlayer.getUniqueId(), (V) pluginPlayer);
    }

    public boolean contains(@NotNull final Player player) {
        return this.containsKey(player.getUniqueId());
    }

    public boolean contains(@NotNull final PluginPlayer pluginPlayer) {
        return this.containsKey(pluginPlayer.getUniqueId());
    }

    /**
     * Gets all PluginPlayers that are on this instance.
     * @return Collection of online CustomPlayers.
     */
    public Set<V> getOnlinePlayers() {
        final Set<V> onlinePlayers = new HashSet<>();

        // Loop through all objects.
        for(final V object : this.values()) {
            // If it's not a CustomPlayer, skip it.
            if(!(object instanceof final PluginPlayer pluginPlayer)) {
                continue;
            }

            // Gets the Bukkit player being represented.
            final Player player = pluginPlayer.getBukkitPlayer();

            // Skip them if they are not online.
            if(player == null || !player.isOnline()) {
                continue;
            }

            // Otherwise, add them to the Set.
            onlinePlayers.add((V) pluginPlayer);
        }

        return onlinePlayers;
    }

    public void put(@NotNull final Player player) {
        add(player);
    }

    public void put(@NotNull final PluginPlayer pluginPlayer) {
        add(pluginPlayer);
    }

    public void remove(@NotNull final Player player) {
        this.remove(player.getUniqueId());
    }

    public void remove(@NotNull final PluginPlayer pluginPlayer) {
        this.remove(pluginPlayer.getUniqueId());
    }
}