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
import net.jadedmc.jadedutils.FileUtils;
import net.jadedmc.jadedutils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CancelWorldCMD extends AbstractCommand {
    private final JadedCorePlugin plugin;

    public CancelWorldCMD(final JadedCorePlugin plugin) {
        super("cancelworld", "jadedcore.cancelworld", false);
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull final CommandSender sender, final String[] args) {
        final Player player = (Player) sender;

        // Make sure the world isn't loaded as a lobby.
        if(plugin.lobbyManager().isLobbyWorld(player.getWorld())) {
            ChatUtils.chat(player, "&c&lError &8» &cYou cannot cancel this world!");
            return;
        }

        final World world = player.getWorld();

        // Kick all the players out before killing.
        for(final Player inhabitant : world.getPlayers()) {
            plugin.lobbyManager().sendToLobby(inhabitant);
        }

        // Gets the world folder before stabbing the world.
        final File worldFolder = world.getWorldFolder();

        // Kill the world.
        Bukkit.unloadWorld(world, false);

        // Hides the body.
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            FileUtils.deleteDirectory(worldFolder);
        });
    }
}