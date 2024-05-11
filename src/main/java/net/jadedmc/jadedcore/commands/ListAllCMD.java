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

import net.jadedmc.jadedcore.JadedCorePlugin;
import net.jadedmc.jadedcore.player.JadedPlayer;
import net.jadedmc.jadedcore.player.Rank;
import net.jadedmc.jadedutils.chat.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * This class runs the listall command, which lists all online players in the server.
 */
public class ListAllCMD extends AbstractCommand {
    private final JadedCorePlugin plugin;

    /**
     * Creates the /listall command with the permission "jadedcore.listall".
     */
    public ListAllCMD(final JadedCorePlugin plugin) {
        super("listall", "jadedcore.listall", false);
        this.plugin = plugin;
    }

    /**
     * This is the code that runs when the command is sent.
     *
     * @param sender The player (or console) that sent the command.
     * @param args   The arguments of the command.
     */
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        // Create a  map to store all the groups.
        LinkedHashMap<Rank, Set<Player>> groups = new LinkedHashMap<>();
        for(Rank rank : Rank.values()) {
            groups.put(rank, new HashSet<>());
        }

        // Add players to their group's player list.
        for(Player worldPlayer : plugin.getServer().getOnlinePlayers()) {
            JadedPlayer jadedPlayer = plugin.jadedPlayerManager().getPlayer(player);
            groups.get(jadedPlayer.getRank()).add(worldPlayer);
        }

        // Creates a string containing all players.
        StringBuilder list = new StringBuilder();
        for(Rank rank : groups.keySet()) {
            for(Player worldPlayer : groups.get(rank)) {
                list.append(rank.getChatColor()).append(worldPlayer.getName()).append("<white>,");
            }
        }

        // Prints the list.
        ChatUtils.chat(player, "");
        ChatUtils.chat(player, "<center><green><bold>All Players</bold> <gray>(" + plugin.getServer().getOnlinePlayers().size() + ")");
        ChatUtils.chat(player, list.substring(0, list.length() - 1));
        ChatUtils.chat(player, "");
    }
}