/*
 * This file is part of JadedLobby, licensed under the MIT License.
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
import net.jadedmc.jadedutils.chat.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Runs the /spawn command, which teleports the player to the server spawn.
 */
public class StuckCMD extends AbstractCommand {
    private final JadedCorePlugin plugin;

    /**
     * Creates the command with no required permissions.
     * @param plugin Instance of the plugin.
     */
    public StuckCMD(@NotNull final JadedCorePlugin plugin) {
        super("stuck", "", false);
        this.plugin = plugin;
    }

    /**
     * Runs when the command is executed.
     * @param sender The Command Sender.
     * @param args Arguments of the command.
     */
    @Override
    public void execute(@NotNull final CommandSender sender, final String[] args) {
        final Player player = (Player) sender;

        // Makes sure the player can go to spawn.
        if(!plugin.lobbyManager().isEnabled() || !plugin.lobbyManager().isLobbyWorld(player.getWorld())) {
            ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>You can't use that command here!");
            return;
        }

        // Make sure spawn has been set.
        if(!plugin.lobbyManager().isSpawnSet()) {
            ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>No spawn location has been set!");
            return;
        }

        player.teleport(plugin.lobbyManager().getSpawn());
    }
}