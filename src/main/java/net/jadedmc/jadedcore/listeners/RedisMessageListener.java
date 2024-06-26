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
package net.jadedmc.jadedcore.listeners;

import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedcore.JadedCorePlugin;
import net.jadedmc.jadedcore.events.RedisMessageEvent;
import net.jadedmc.jadedcore.networking.InstanceStatus;
import net.jadedmc.jadedcore.party.Party;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import redis.clients.jedis.Jedis;

import java.util.UUID;

public class RedisMessageListener implements Listener {
    private final JadedCorePlugin plugin;

    public RedisMessageListener(final JadedCorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMessage(RedisMessageEvent event) {

        if(event.getChannel().equalsIgnoreCase("party")) {
            partyChannel(event);
            return;
        }

        if(!event.getChannel().equalsIgnoreCase("jadedmc")) {
            return;
        }

        String[] args = event.getMessage().split(" ");

        if(args.length < 2) {
            return;
        }

        switch (args[1]) {
            case "summon" -> {
                UUID playerUUID = UUID.fromString(args[2]);
                String server = args[3];

                Player player = plugin.getServer().getPlayer(playerUUID);

                if(player == null || !player.isOnline()) {
                    return;
                }

                JadedAPI.sendBungeecordMessage(player, "BungeeCord", "connect", server);
            }

            case "close" -> {
                String server = args[2];

                if(server.equalsIgnoreCase(JadedAPI.getCurrentInstance().getName())) {
                    JadedAPI.getCurrentInstance().setStatus(InstanceStatus.CLOSED);
                }
            }
        }
    }

    private void partyChannel(RedisMessageEvent event) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            final String[] args = event.getMessage().split(" ");

            switch(args[0].toLowerCase()) {
                case "disband" -> {
                    final UUID partyUUID = UUID.fromString(args[1]);
                    final Party party = plugin.partyManager().getLocalPartyFromUUID(partyUUID);
                    plugin.partyManager().deleteLocalParty(party);
                }

                case "join" -> {
                    final UUID partyUUID = UUID.fromString(args[1]);
                    final UUID playerUUID = UUID.fromString(args[2]);
                    final Party party = plugin.partyManager().getLocalPartyFromUUID(partyUUID);

                    final Player player = plugin.getServer().getPlayer(playerUUID);
                    if(player == null || !player.isOnline()) {
                        return;
                    }

                    try(Jedis jedis = plugin.redis().jedisPool().getResource()) {
                        final Document document = Document.parse(jedis.get("parties:" + partyUUID.toString()));

                        if(party == null) {
                            plugin.partyManager().loadPartyFromDocument(document);
                        }
                        else {
                            party.update(document);
                        }
                    }
                }

                case "leave" -> {
                    final UUID partyUUID = UUID.fromString(args[1]);
                    final UUID playerUUID = UUID.fromString(args[2]);
                    final Party party = plugin.partyManager().getLocalPartyFromUUID(partyUUID);

                    if(party == null) {
                        return;
                    }

                    party.removePlayer(playerUUID);
                }

                case "update" -> {
                    final UUID partyUUID = UUID.fromString(args[1]);
                    final Party party = plugin.partyManager().getLocalPartyFromUUID(partyUUID);

                    if(party == null) {
                        return;
                    }

                    try(Jedis jedis = plugin.redis().jedisPool().getResource()) {
                        final Document partyDocument = Document.parse(jedis.get("parties:" + partyUUID.toString()));
                        party.update(partyDocument);
                    }
                }
            }
        });
    }
}