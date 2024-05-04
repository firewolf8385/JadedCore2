/*
 * This file is part of JadedCoreLegacy, licensed under the MIT License.
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

import net.jadedmc.jadedchat.JadedChat;
import net.jadedmc.jadedchat.features.channels.events.ChannelMessageSendEvent;
import net.jadedmc.jadedcore.JadedCorePlugin;
import net.jadedmc.jadedcore.party.Party;
import net.jadedmc.jadedutils.chat.ChatUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collections;

/**
 * Listens to the ChannelMessageSendEvent, which is called every time a player sends a message in a chat channel.
 */
public class ChannelMessageSendListener implements Listener {
    private final JadedCorePlugin plugin;

    /**
     * Creates the Listener.
     * @param plugin Instance of the plugin.
     */
    public ChannelMessageSendListener(final JadedCorePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the ChannelMessageSendEvent is called.
     * @param event ChannelMessageSendEvent.
     */
    @EventHandler
    public void onMessageSend(ChannelMessageSendEvent event) {
        plugin.achievementManager().getAchievement("general_2").unlock(event.getPlayer());

        if(event.getChannel().equals(JadedChat.getChannel("PARTY"))) {
            partyChannel(event);
            return;
        }
    }

    private void partyChannel(ChannelMessageSendEvent event) {
        Player player = event.getPlayer();
        Party party = plugin.partyManager().getLocalPartyFromPlayer(player);

        if(party == null) {
            ChatUtils.chat(player, "&cYou are not in a party! You can go back to global with /chat global");
            event.setCancelled(true);
            return;
        }

        // Sends the chat message to the party.
        String message = MiniMessage.miniMessage().serialize(event.getFormattedMessage());
        party.sendMessage(message);

        // Remove all server-side viewers.
        event.setViewers(Collections.emptyList());
    }
}