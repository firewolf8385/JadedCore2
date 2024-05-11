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

import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedcore.JadedCorePlugin;
import net.jadedmc.jadedcore.networking.player.NetworkPlayer;
import net.jadedmc.jadedutils.gui.CustomGUI;
import net.jadedmc.jadedutils.items.ItemBuilder;
import net.jadedmc.jadedutils.items.SkullBuilder;
import net.jadedmc.jadedutils.player.CustomPlayerSet;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayersCMD extends AbstractCommand {
    private final JadedCorePlugin plugin;

    public PlayersCMD(final JadedCorePlugin plugin) {
        super("players", "jadedcore.players", false);
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull CommandSender sender, String[] args) {
        Player player = (Player) sender;
        new PlayersGUI(plugin).open(player);
    }

    private static class PlayersGUI extends CustomGUI {

        public PlayersGUI(JadedCorePlugin plugin) {
            super(54, "Online Players");

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                final CustomPlayerSet<NetworkPlayer> players = JadedAPI.getPlayers();

                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    int slot = 0;

                    for(NetworkPlayer player : JadedAPI.getPlayers()) {
                        if(slot > 53) {
                            continue;
                        }

                        ItemBuilder builder = new SkullBuilder(player.getSkin())
                                .asItemBuilder()
                                .setDisplayName("<green>" + player.getName())
                                .addLore("<gray>Server: <green>" + player.getServerName());
                        setItem(slot, builder.build());

                        slot++;
                    }
                });
            });
        }
    }
}