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
package net.jadedmc.jadedcore.commands;

import net.jadedmc.jadedchat.utils.StringUtils;
import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedcore.JadedCorePlugin;
import net.jadedmc.jadedcore.networking.player.NetworkPlayerSet;
import net.jadedmc.jadedcore.party.Party;
import net.jadedmc.jadedcore.party.PartyPlayer;
import net.jadedmc.jadedcore.party.PartyRole;
import net.jadedmc.jadedcore.party.PartySet;
import net.jadedmc.jadedcore.player.JadedPlayer;
import net.jadedmc.jadedutils.chat.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class PartyCMD extends AbstractCommand {
    private final JadedCorePlugin plugin;

    public PartyCMD(final JadedCorePlugin plugin) {
        super("party", "", false);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length == 0) {
            return;
        }

        switch(args[0].toLowerCase()) {
            case "accept" -> acceptCMD(player, args);
            case "create" -> createCMD(player);
            case "disband" -> disbandCMD(player);
            case "invite" -> inviteCMD(player, args);
            case "list" -> listCMD(player);
        }
    }

    private void acceptCMD(final Player player, String[] args) {
        // Makes sure the player is using the command correctly.
        if(args.length == 1) {
            ChatUtils.chat(player, "<red><bold>Usage</bold> <dark_gray>» <red>/party accept [player]");
            return;
        }

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            NetworkPlayerSet onlinePlayers = JadedAPI.getPlayers();
            String username = args[1];

            if(!onlinePlayers.hasPlayer(username)) {
                ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>That player is not online");
                return;
            }

            UUID uuid = onlinePlayers.getPlayer(username).getUniqueUID();

            Collection<Party> remoteParties = plugin.partyManager().getRemoteParties();
            boolean inParty = false;
            Party party = null;

            for(Party remoteParty : remoteParties) {
                if(remoteParty.hasPlayer(uuid)) {
                    party = remoteParty;
                    inParty = true;
                    break;
                }
            }

            if(!inParty) {
                ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>That player is not in a party");
                return;
            }

            if(!party.getInvites().contains(player.getUniqueId())) {
                ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>You do not have an invite to that party.");
                System.out.println("Invites Found: " + party.getInvites().size());

                for(UUID inviteUUID : party.getInvites()) {
                    System.out.println(inviteUUID.toString());
                }
                return;
            }

            party.addPlayer(player, PartyRole.MEMBER);
            party.update();
            party.sendMessage("<green><bold>Party</bold> <dark_gray>» <white>" + player.getName() + " &ahas joined the party.");
        });
    }

    private void createCMD(final Player player) {
        // Makes sure the player is not already in a party.
        if(plugin.partyManager().getLocalParties().containsPlayer(player)) {
            ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>You are already in a party.");
            return;
        }

        Party party = plugin.partyManager().createLocalParty(player);
        party.update();

        ChatUtils.chat(player, "<green><bold>Party</bold> <dark_gray>» <green>Party has been created.");
    }

    private void disbandCMD(@NotNull final Player player) {
        final Party party = plugin.partyManager().getLocalPartyFromPlayer(player);

        if(party == null) {
            ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>You are not in a party!");
            return;
        }

        final PartyPlayer partyPlayer = party.getPlayer(player.getUniqueId());
        if(partyPlayer.getRole() != PartyRole.LEADER) {
            ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>You do not have permission to disband the party!");
            return;
        }

        party.sendMessage("<green><bold>Party</bold> <dark_gray>» <green>The party has been disbanded.");
        party.disband();
    }

    private void inviteCMD(final Player player, String[] args) {
        Party party = plugin.partyManager().getLocalPartyFromPlayer(player);
        if(party == null) {
            ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>You are not in a party! Create one with /p create.");
            return;
        }

        PartyPlayer partyPlayer = party.getPlayer(player.getUniqueId());
        if(partyPlayer.getRole() == PartyRole.MEMBER) {
            ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>You do not have permission to invite other players!");
            return;
        }

        if(args[1].equalsIgnoreCase(player.getName())) {
            ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>You cannot invite yourself!");
            return;
        }

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            NetworkPlayerSet onlinePlayers = JadedAPI.getPlayers();
            String username = args[1];

            if(!onlinePlayers.hasPlayer(username)) {
                ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>That player is not online");
                return;
            }

            UUID targetUUID = onlinePlayers.getPlayer(username).getUniqueUID();

            PartySet remoteParties = plugin.partyManager().getRemoteParties();
            if(remoteParties.containsPlayer(targetUUID)) {
                ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>That player is already in a party!");
                return;
            }

            // Makes sure the player wasn't already invited.
            if(party.getInvites().contains(targetUUID)) {
                ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>You already have a pending invite to that person.");
                return;
            }

            JadedPlayer jadedPlayer = plugin.jadedPlayerManager().getPlayer(player);

            party.addInvite(targetUUID);
            party.sendMessage("<green><bold>Party</bold> <dark_gray>» <white>" + username + " &ahas been invited to the party.");
            party.update();

            String inviteMessage = "<green>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬</green><newline>" +
                    ChatUtils.centerText("<green><bold>Party Invite</bold></green>") + "<newline>" +
                    "<newline>" +
                    ChatUtils.centerText(jadedPlayer.getRank().getChatPrefix() + "<gray>" + player.getName() + " <green>has invited you to join their party!") + "<newline>" +
                    "<newline>" +
                    "  <dark_gray>» <click:run_command:'/party accept " + player.getName() + "'><hover:show_text:'<green>Click to accept'><green>/party accept " + player.getName() + "</hover></click><newline>" +
                    "<newline>" +
                    "<green>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬</green><newline>";

            JadedAPI.sendMessage(targetUUID, inviteMessage);
        });
    }

    private void listCMD(final Player player) {
        // Makes sure the player is in a party.
        Party party = plugin.partyManager().getLocalPartyFromPlayer(player);
        if(party == null) {
            ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>You are not in a party! Create one with /p create.");
            return;
        }

        // Gets all the party members and their roles.
        String leader = "";
        List<String> moderators = new ArrayList<>();
        List<String> members = new ArrayList<>();
        for(PartyPlayer member : party.getPlayers()) {
            switch (member.getRole()) {
                case LEADER -> leader = member.getRank().getChatPrefix() + "<gray>" + member.getUsername();
                case MODERATOR -> moderators.add(member.getRank().getChatPrefix() + "<gray>" + member.getUsername());
                case MEMBER -> members.add(member.getRank().getChatPrefix() + "<gray>" + member.getUsername());
            }
        }

        // Displays the list
        ChatUtils.chat(player, "&8&m+-----------------------***-----------------------+");
        ChatUtils.chat(player, ChatUtils.centerText("&a&lParty Members"));
        ChatUtils.chat(player, "");
        ChatUtils.chat(player, "&aLeader &7» &f" + leader);

        // Only show moderators if there are any.
        if(moderators.size() > 0) {
            ChatUtils.chat(player, "&aModerators &7[" + moderators.size() + "] » &f" + StringUtils.join(moderators, ", "));
        }

        ChatUtils.chat(player, "&aMembers &7[" + members.size() + "] » &f" + StringUtils.join(members, ", "));
        ChatUtils.chat(player, "");
        ChatUtils.chat(player, "&8&m+-----------------------***-----------------------+");
    }

    private void summonCMD(final Player player) {
        // Makes sure the player is in a party.
        Party party = plugin.partyManager().getLocalPartyFromPlayer(player);
        if(party == null) {
            ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>You are not in a party! Create one with /p create.");
            return;
        }
    }
}