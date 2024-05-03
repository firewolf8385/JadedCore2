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
package net.jadedmc.jadedcore.party;

import net.jadedmc.jadedchat.utils.StringUtils;
import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedcore.JadedCorePlugin;
import net.jadedmc.jadedutils.chat.ChatUtils;
import org.bson.Document;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Represents a group of players playing together.
 */
public class Party {
    private final JadedCorePlugin plugin;
    private final UUID uuid;
    private final Collection<PartyPlayer> players = new HashSet<>();
    private final Collection<UUID> invites = new HashSet<>();

    public Party(final JadedCorePlugin plugin, final Document document) {
        this.plugin = plugin;
        this.uuid = UUID.fromString(document.getString("uuid"));

        Document playersDocument = document.get("players", Document.class);
        for(String player : playersDocument.keySet()) {
            players.add(new PartyPlayer(playersDocument.get(player, Document.class)));
        }
    }

    public Party(final JadedCorePlugin plugin, Player leader) {
        this.plugin = plugin;
        this.uuid = UUID.randomUUID();
        addPlayer(leader, PartyRole.LEADER);
    }

    public void addPlayer(Player player, PartyRole role) {
        players.add(new PartyPlayer(player, role));
        this.invites.remove(player.getUniqueId());
    }

    public PartyPlayer getPlayer(UUID playerUUID) {
        for(PartyPlayer partyPlayer : players) {
            if(partyPlayer.getUniqueID().equals(playerUUID)) {
                return partyPlayer;
            }
        }

        return null;
    }

    public Collection<PartyPlayer> getPlayers() {
        return players;
    }

    public UUID getUniqueID() {
        return uuid;
    }

    public boolean hasPlayer(UUID playerUUID) {
        for(PartyPlayer partyPlayer : players) {
            if(partyPlayer.getUniqueID().equals(playerUUID)) {
                return true;
            }
        }

        return false;
    }

    public void removePlayer(UUID player) {
        for(PartyPlayer partyPlayer : players) {
            if(partyPlayer.getUniqueID().equals(player)) {
                players.remove(partyPlayer);
                return;
            }
        }
    }

    public Document toDocument() {
        Document document = new Document();
        document.append("uuid", uuid.toString());

        Document playersDocument = new Document();
        for(PartyPlayer player : players) {
            playersDocument.append(player.getUniqueID().toString(), player.toDocument());
        }
        document.append("players", playersDocument);

        List<String> invites = new ArrayList<>();
        this.invites.forEach(invite -> invites.add(invite.toString()));
        document.append("invites", invites);

        return document;
    }

    public void update() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.redis().set("parties:" + uuid.toString(), toDocument().toJson());
        });
    }

    public void addInvite(final UUID playerUUID) {
        this.invites.add(playerUUID);
    }

    public Collection<UUID> getInvites() {
        return this.invites;
    }

    public void removeInvite(final UUID playerUUID) {
        this.invites.remove(playerUUID);
    }

    public void broadcast(final String message) {
        // TODO: Create a "publishAsync" method.
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            JadedAPI.getRedis().publish("party", "message " + message);
            JadedAPI.getRedis().publish("party", "update " + this.uuid.toString());
        });
    }

    public void broadcastLocal(final String message) {
        for(PartyPlayer partyPlayer : this.players) {
            Player player = partyPlayer.getPlayer();

            if(player == null) {
                continue;
            }

            ChatUtils.chat(player, message);
        }
    }

    public void update(final Document document) {
        Document playersDocument = document.get("players", Document.class);
        for(String player : playersDocument.keySet()) {
            players.add(new PartyPlayer(playersDocument.get(player, Document.class)));
        }
    }

    public void disband() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            JadedAPI.getRedis().publish("party", "disband " + this.uuid.toString());
            JadedAPI.getRedis().del("parties:" + this.uuid.toString());
        });
    }
}