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

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.jadedmc.jadedcore.databases.MongoDB;
import net.jadedmc.jadedcore.databases.MySQL;
import net.jadedmc.jadedcore.databases.Redis;
import net.jadedmc.jadedcore.minigames.Minigame;
import net.jadedmc.jadedcore.networking.CurrentInstance;
import net.jadedmc.jadedcore.networking.Instance;
import net.jadedmc.jadedcore.networking.InstanceMonitor;
import net.jadedmc.jadedcore.networking.InstanceType;
import net.jadedmc.jadedcore.networking.player.NetworkPlayer;
import net.jadedmc.jadedcore.party.Party;
import net.jadedmc.jadedcore.player.JadedPlayer;
import net.jadedmc.jadedutils.player.CustomPlayerSet;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.util.*;

public class JadedAPI {
    private static JadedCorePlugin plugin = null;

    public JadedAPI(JadedCorePlugin pl) {
        plugin = pl;
    }

    @Deprecated
    public static Connection getDatabase() {
        return plugin.mySQL().getConnection();
    }

    public static MongoDB getMongoDB() {
        return plugin.mongoDB();
    }

    public static MySQL getMySQL() {
        return plugin.mySQL();
    }

    public static Redis getRedis() {
        return plugin.redis();
    }

    public static void sendToLobby(Player player, Minigame minigame) {
        plugin.instanceMonitor().getInstancesAsync().thenAccept(instances -> {
            UUID gameUUID = UUID.randomUUID();

            String serverName = "";
            {
                int count = 999;

                // Loop through all online servers looking for a server to send the player to
                for (Instance instance : instances) {
                    // Make sure the server is the right mode
                    if (instance.getMinigame() != minigame) {
                        continue;
                    }

                    // Make sure the server isn't a game server.
                    if (instance.getType() != InstanceType.LOBBY && instance.getType() != InstanceType.OTHER) {
                        continue;
                    }

                    // Make sure there is room for another game.
                    if (instance.getOnline() >= instance.getCapacity()) {
                        System.out.println("Not enough room!");
                        continue;
                    }

                    //
                    if (instance.getOnline() < count) {
                        count = instance.getOnline();
                        serverName = instance.getName();
                    }
                }

                // If no server is found, give up ¯\_(ツ)_/¯
                if (count == 999) {
                    System.out.println("No Server Found!");
                    return;
                }

                String finalServerName = serverName;
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    JadedAPI.sendBungeecordMessage(player, "BungeeCord", "Connect", finalServerName);
                });
            }
        }).whenComplete((results, error) -> error.printStackTrace());
    }

    public static CurrentInstance getCurrentInstance() {
        return plugin.instanceMonitor().getCurrentInstance();
    }

    public static JadedCorePlugin getPlugin() {
        return plugin;
    }

    public boolean isLobbyServer() {
        return plugin.lobbyManager().isEnabled();
    }

    public static void sendBungeecordMessage(String channel, String subChannel, String message) {

        // Makes sure there is a player online.
        if(Bukkit.getOnlinePlayers().size() == 0) {
            return;
        }

        // Picks the first player online to send the message to.
        sendBungeecordMessage(Iterables.getFirst(Bukkit.getOnlinePlayers(), null), channel, subChannel, message);
    }

    public static void sendBungeecordMessage(Player player, String channel, String subChannel, String message) {

        // Creates the message
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subChannel);
        out.writeUTF(message);

        // Sends the message using the first player online.
        player.sendPluginMessage(plugin, channel, out.toByteArray());
    }

    public static JadedPlayer getJadedPlayer(Player player) {
        return plugin.jadedPlayerManager().getPlayer(player);
    }

    public static void sendToServer(UUID uuid, String server) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> getRedis().publish("jadedmc", "connect " + uuid.toString() + " " + server));
    }

    public static Instance getServer(String serverName) {
        return plugin.instanceMonitor().getInstance(serverName);
    }

    public static CustomPlayerSet<NetworkPlayer> getPlayers() {
        final CustomPlayerSet<NetworkPlayer> players = new CustomPlayerSet<>();

        try(Jedis jedis = plugin.redis().jedisPool().getResource()) {
            final Set<String> names = jedis.keys("jadedplayers:*");

            for(final String key : names) {
                final String json = jedis.get(key);
                final Document document = Document.parse(json);

                players.add(new NetworkPlayer(document));
            }
        }

        return players;
    }

    public static CustomPlayerSet<NetworkPlayer> getPlayers(Minigame... games) {
        final CustomPlayerSet<NetworkPlayer> players = new CustomPlayerSet<>();

        try(Jedis jedis = plugin.redis().jedisPool().getResource()) {
            final Set<String> names = jedis.keys("jadedplayers:*");

            for(String key : names) {
                String json = jedis.get(key);
                Document document = Document.parse(json);

                if(!Arrays.asList(games).contains(Minigame.valueOf(document.getString("game")))) {
                    continue;
                }

                players.add(new NetworkPlayer(document));
            }
        }

        return players;
    }

    public static InstanceMonitor getInstanceMonitor() {
        return plugin.instanceMonitor();
    }

    public static boolean isOnline(final UUID playerUUID) {
        try(Jedis jedis = plugin.redis().jedisPool().getResource()) {
            return jedis.exists("jadedplayers:" + playerUUID);
        }
    }

    public static boolean isOnline(final String username) {
        return getPlayers().hasPlayer(username);
    }

    public static void sendMessage(final UUID playerUUID, final String message) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.redis().publish("proxy", "message " + playerUUID.toString() + " " + message);
        });
    }

    public static void sendPlayer(final UUID playerUUID, final String server) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.redis().publish("proxy", "connect " + playerUUID.toString() + " " + server);
        });
    }

    public static Party getParty(@NotNull final UUID playerUUID) {
        return plugin.partyManager().getLocalPartyFromUUID(playerUUID);
    }
}