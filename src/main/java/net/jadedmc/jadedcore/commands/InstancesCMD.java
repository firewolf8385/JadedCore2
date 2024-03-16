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

import com.cryptomorin.xseries.XMaterial;
import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedcore.JadedCorePlugin;
import net.jadedmc.jadedcore.servers.Server;
import net.jadedmc.jadedutils.gui.CustomGUI;
import net.jadedmc.jadedutils.items.ItemBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InstancesCMD extends AbstractCommand {
    private final JadedCorePlugin plugin;

    public InstancesCMD(final JadedCorePlugin plugin) {
        super("instances", "jadedcore.instances", false);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        new InstancesGUI(plugin).open(player);
    }

    private class InstancesGUI extends CustomGUI {

        public InstancesGUI(JadedCorePlugin plugin) {
            super(54, "Instances");

            JadedAPI.getServers().thenAccept(servers -> {
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    int slot = 0;

                    for(Server server : servers) {
                        String color;
                        XMaterial xMaterial;

                        switch (server.status()) {
                            case UNRESPONSIVE -> {
                                color = "<dark_gray>";
                                xMaterial = XMaterial.BLACK_TERRACOTTA;
                            }

                            case FULL -> {
                                color = "<red>";
                                xMaterial = XMaterial.RED_TERRACOTTA;
                            }

                            default -> {
                                color = "<green>";
                                xMaterial = XMaterial.GREEN_TERRACOTTA;
                            }
                        }

                        ItemBuilder builder = new ItemBuilder(xMaterial)
                                .setDisplayName("<green>" + server.name())
                                .addLore("<gray>Mode: " + color + server.mode())
                                .addLore("<gray>Type: " + color + server.type())
                                .addLore("<gray>Online: " + color + server.online() + "<gray>/" + color + server.capacity());
                        setItem(slot, builder.build());

                        slot++;
                    }
                });
            });
        }
    }
}
