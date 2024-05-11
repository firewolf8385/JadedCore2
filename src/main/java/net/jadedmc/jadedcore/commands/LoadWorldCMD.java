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
import net.jadedmc.jadedutils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LoadWorldCMD extends AbstractCommand {
    private final JadedCorePlugin plugin;

    public LoadWorldCMD(@NotNull final JadedCorePlugin plugin) {
        super("loadworld", "jadedcore.loadworld", false);
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull final CommandSender sender, final String[] args) {
        final Player player = (Player) sender;

        // Make sure they are using the command properly.
        if(args.length != 1) {
            ChatUtils.chat(player, "<red>><bold>>Usage</bold> <dark_gray>» <red>/loadworld <name>");
            return;
        }

        final String worldName = args[0];

        // Make sure the world isn't already being loaded.
        for(final World world : Bukkit.getWorlds()) {
            if(world.getName().equals(worldName)) {
                player.teleport(world.getSpawnLocation());
                return;
            }
        }

        // If not, downloads the world and teleports the player.
        plugin.worldManager().loadWorld(args[0]).thenAccept(world -> {
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                player.teleport(world.getSpawnLocation());
            });
        });
    }
}