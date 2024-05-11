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

import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedcore.JadedCorePlugin;
import net.jadedmc.jadedutils.chat.ChatUtils;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Represents a group of players playing together.
 */
public class Party {
    private final JadedCorePlugin plugin;
    private final UUID uuid;
    private final Collection<PartyPlayer> players = new HashSet<>();
    private final Collection<UUID> invites = new HashSet<>();

    /**
     * Creates the party using a Bson Document.
     * @param plugin Instance of the plugin.
     * @param document Bson document.
     */
    public Party(final JadedCorePlugin plugin, final Document document) {
        this.plugin = plugin;
        this.uuid = UUID.fromString(document.getString("uuid"));

        // Load the players from the document.
        Document playersDocument = document.get("players", Document.class);
        for(String player : playersDocument.keySet()) {
            players.add(new PartyPlayer(playersDocument.get(player, Document.class)));
        }

        // Load the pending invites of the party.
        List<String> inviteUUIDs = document.getList("invites", String.class);
        for(String uuid : inviteUUIDs) {
            this.invites.add(UUID.fromString(uuid));
        }
    }

    /**
     * Creates an empty party with a given leader.
     * @param plugin Instance of the plugin.
     * @param leader Leader of the party.
     */
    public Party(final JadedCorePlugin plugin, Player leader) {
        this.plugin = plugin;
        this.uuid = UUID.randomUUID();
        addPlayer(leader, PartyRole.LEADER);
    }

    /**
     * Adds an invite to the party.
     * @param playerUUID UUID of the player being invited.
     */
    public void addInvite(final UUID playerUUID) {
        this.invites.add(playerUUID);
    }

    /**
     * Adds a player to the party.
     * @param player Player to add to the party.
     * @param role Role the player has.
     */
    public void addPlayer(Player player, PartyRole role) {
        players.add(new PartyPlayer(player, role));

        // Removes any potential pending invites for the player.
        this.invites.remove(player.getUniqueId());
    }

    /**
     * Disbands the party.
     */
    public void disband() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            JadedAPI.getRedis().publish("party", "disband " + this.uuid.toString());
            JadedAPI.getRedis().del("parties:" + this.uuid.toString());
        });
    }

    /**
     * Gets a Collection of all current invites.
     * @return All active invites.
     */
    public Collection<UUID> getInvites() {
        return this.invites;
    }

    /**
     * Get the PartyPlayer of a player, from their uuid.
     * @param playerUUID UUID of the player.
     * @return Corresponding PartyPlayer object.
     */
    public PartyPlayer getPlayer(UUID playerUUID) {
        for(PartyPlayer partyPlayer : players) {
            if(partyPlayer.getUniqueID().equals(playerUUID)) {
                return partyPlayer;
            }
        }

        return null;
    }

    /**
     * Get the PartyPlayer of a given Player.
     * @param player Player to get PartyPlayer of.
     * @return Corresponding PartyPlayer object.
     */
    @Nullable
    public PartyPlayer getPlayer(final Player player) {
        return getPlayer(player.getUniqueId());
    }

    /**
     * Gets all party members who are on the current instance.
     * @return All online players.
     */
    public Collection<PartyPlayer> getOnlinePlayers() {
        final Collection<PartyPlayer> onlinePlayers = new HashSet<>();

        for(final PartyPlayer player : players) {
            if(player.isOnline()) {
                onlinePlayers.add(player);
            }
        }

        return onlinePlayers;
    }

    /**
     * Gets all players currently in the party.
     * @return All current players.
     */
    public Collection<PartyPlayer> getPlayers() {
        return players;
    }

    /**
     * Gets the UUID of the party.
     * @return Party UUID.
     */
    public UUID getUniqueID() {
        return uuid;
    }

    /**
     * Check if the party has a given player in it.
     * @param playerUUID UUID of the player.
     * @return Whether they are in the party.
     */
    public boolean hasPlayer(UUID playerUUID) {
        for(PartyPlayer partyPlayer : players) {
            if(partyPlayer.getUniqueID().equals(playerUUID)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasUsername(final String username) {
        for(PartyPlayer partyPlayer : players) {
            if(partyPlayer.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Removes an invite from the party.
     * @param playerUUID UUID of the player who was invited.
     */
    public void removeInvite(final UUID playerUUID) {
        this.invites.remove(playerUUID);
    }

    /**
     * Removes a player from the party.
     * @param playerUUID UUID of the player to remove.
     */
    public void removePlayer(UUID playerUUID) {
        for(PartyPlayer partyPlayer : players) {
            if(partyPlayer.getUniqueID().equals(playerUUID)) {
                players.remove(partyPlayer);
                return;
            }
        }
    }

    /**
     * Sends a message to all members of the party.
     * @param message Message to be sent.
     */
    public void sendMessage(final String message) {
        final StringBuilder builder = new StringBuilder();
        players.forEach(partyPlayer -> {
            builder.append(partyPlayer.getUniqueID());
            builder.append(",");
        });

        final String targets = builder.substring(0, builder.length() - 1);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            JadedAPI.getRedis().publish("proxy", "message " + targets + " " + message);
        });
    }

    /**
     * Updates the party in Redis without announcing the update.
     */
    public void silentUpdate() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.redis().set("parties:" + uuid.toString(), toDocument().toJson());
        });
    }

    /**
     * Converts the cached party into a Bson Document.
     * @return Bson document of the party.
     */
    public Document toDocument() {
        final Document document = new Document();
        document.append("uuid", uuid.toString());

        final Document playersDocument = new Document();
        for(PartyPlayer player : players) {
            playersDocument.append(player.getUniqueID().toString(), player.toDocument());
        }
        document.append("players", playersDocument);

        final List<String> invites = new ArrayList<>();
        this.invites.forEach(invite -> invites.add(invite.toString()));
        document.append("invites", invites);

        return document;
    }

    /**
     * Updates the party in Redis.
     */
    public void update() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.redis().set("parties:" + uuid.toString(), toDocument().toJson());
            plugin.redis().publish("party", "update " + this.uuid.toString());
        });
    }

    /**
     * Updates the cached party with a given Bson document.
     * @param document Bson document to use.
     */
    public void update(@NotNull final Document document) {
        // Empty cached players.
        players.clear();

        // Loads the party players.
        final Document playersDocument = document.get("players", Document.class);
        for(String player : playersDocument.keySet()) {
            players.add(new PartyPlayer(playersDocument.get(player, Document.class)));
        }

        // Empty cached invites.
        this.invites.clear();

        // Loads new invites.
        final List<String> inviteUUIDs = document.getList("invites", String.class);
        for(String uuid : inviteUUIDs) {
            this.invites.add(UUID.fromString(uuid));
        }
    }
}