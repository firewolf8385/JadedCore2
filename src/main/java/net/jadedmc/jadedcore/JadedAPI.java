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
import net.jadedmc.jadedcore.games.Game;
import net.jadedmc.jadedcore.games.GameType;
import net.jadedmc.jadedcore.player.JadedPlayer;
import net.jadedmc.jadedcore.servers.Server;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import javax.print.Doc;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.CompletableFuture;

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

    public static CompletableFuture<Collection<Server>> getServers() {
        return CompletableFuture.supplyAsync(() -> {
            Collection<Server> servers = new HashSet<>();


            try(Jedis jedis = plugin.redis().jedisPool().getResource()) {
                Set<String> names = jedis.keys("servers:*");

                for(String key : names) {
                    servers.add(new Server(jedis.get(key)));
                }
            }

            return servers;
        });
    }

    public static void sendToLobby(Player player, Game game) {
        JadedAPI.getServers().thenAccept(servers -> {
            UUID gameUUID = UUID.randomUUID();

            String serverName = "";
            {
                int count = 999;

                // Loop through all online servers looking for a server to send the player to
                for (Server server : servers) {
                    // Make sure the server is the right mode
                    if (!server.mode().equalsIgnoreCase(game.toString())) {
                        continue;
                    }

                    // Make sure the server isn't a game server.
                    if (!server.type().equalsIgnoreCase("LOBBY")) {
                        continue;
                    }

                    // Make sure there is room for another game.
                    if (server.online() + 2 >= server.capacity()) {
                        System.out.println("Not enough room!");
                        continue;
                    }

                    //
                    if (server.online() < count) {
                        count = server.online();
                        serverName = server.name();
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

    public static Game getServerGame() {
        return Game.valueOf(plugin.settingsManager().getConfig().getString("serverGame"));
    }

    public static String getServerName() {
        return plugin.settingsManager().getConfig().getString("serverName");
    }

    public static int getServerVersion() {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        int subVersion = Integer.parseInt(version.replace("1_", "").replaceAll("_R\\d", "").replace("v", ""));

        return subVersion;
    }

    public static JadedPlayer getJadedPlayer(Player player) {
        return plugin.jadedPlayerManager().getPlayer(player);
    }

    public static NetworkPlayerSet getPlayers(Game... games) {
        NetworkPlayerSet players = new NetworkPlayerSet();

        try(Jedis jedis = plugin.redis().jedisPool().getResource()) {
            Set<String> names = jedis.keys("jadedplayers:*");

            for(String key : names) {
                String json = jedis.get(key);
                Document document = Document.parse(json);

                if(!Arrays.asList(games).contains(Game.valueOf(document.getString("game")))) {
                    continue;
                }

                players.addPlayer(document);
            }
        }

        return players;
    }

    public static class NetworkPlayerSet {
        private final List<NetworkPlayer> players = new ArrayList<>();

        public void addPlayer(Document document) {
            players.add(new NetworkPlayer(document));
        }

        public List<NetworkPlayer> getPlayers() {
            return players;
        }

        public boolean hasPlayer(String uuid) {
            for(NetworkPlayer player : players) {
                if(player.getUniqueID().toString().equalsIgnoreCase(uuid) || player.getName().equalsIgnoreCase(uuid)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static class NetworkPlayer {
        private final Document document;

        public NetworkPlayer(Document document) {
            this.document = document;
        }

        public String getName() {
            return document.getString("displayName");
        }

        public UUID getUniqueID() {
            return UUID.fromString(document.getString("uuid"));
        }
    }
}