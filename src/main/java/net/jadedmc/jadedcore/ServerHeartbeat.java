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
package net.jadedmc.jadedcore;

import org.bson.Document;
import org.bukkit.scheduler.BukkitRunnable;

public class ServerHeartbeat extends BukkitRunnable {
    private final JadedCorePlugin plugin;

    public ServerHeartbeat(final JadedCorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Document document = new Document()
                .append("status", "ONLINE")
                .append("online", plugin.getServer().getOnlinePlayers().size())
                .append("capacity", plugin.getServer().getMaxPlayers())
                .append("mode", plugin.settingsManager().getConfig().getString("serverGame"))
                .append("heartbeat", System.currentTimeMillis());

        if(plugin.lobbyManager().isEnabled()) {
            document.append("type", "LOBBY");
        }
        else {
            document.append("type", "GAME");
        }

        plugin.redis().set("servers:" + plugin.settingsManager().getConfig().getString("serverName"), document.toJson());
    }
}