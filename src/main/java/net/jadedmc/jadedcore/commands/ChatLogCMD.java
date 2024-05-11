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
import net.jadedmc.jadedutils.gui.CustomGUI;
import net.jadedmc.jadedutils.items.ItemBuilder;
import net.jadedmc.jadedutils.items.SkullBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatLogCMD extends AbstractCommand {
    private final JadedCorePlugin plugin;
    public ChatLogCMD(@NotNull final JadedCorePlugin plugin) {
        super("chatlog", "jadedcore.chatlog", false);
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull final CommandSender sender, final String[] args) {
        // Make sure they're using the command properly.
        if(args.length < 1) {
            ChatUtils.chat(sender, "&c&lUsage &8» &c/chatlog [player]");
            return;
        }

        // Runs MySQL async.
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                final PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM player_info WHERE username = ?");
                statement.setString(1, args[0]);
                final ResultSet results = statement.executeQuery();

                if(results.next()) {
                    final String target = results.getString(2);
                    final String uuid = results.getString(1);

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        new ChatLogGUI(target, uuid, 1).open((Player) sender);
                    });
                }
                else {
                    ChatUtils.chat(sender, "&cError &8» &cThat player has not played.");
                }
            }
            catch (final SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    private class ChatLogGUI extends CustomGUI {
        public ChatLogGUI(String target, String uuid, int page) {
            super(54, "Chat Log - " + target);

            addFiller(0,1,2,3,4,5,6,7,8,45,46,47,49,51,52,53);

            if(page == 1) {
                final ItemStack previous = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=")
                        .asItemBuilder()
                        .setDisplayName("&cPrevious Page")
                        .build();
                setItem(48, previous);
            }
            else {
                final ItemStack previous = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzU0YWFhYjM5NzY0NjQxZmY4NjI3OTMyZDJmZTFhNGNjY2VkOTY1Mzc1MDhkNGFiYzZjZDVmYmJiODc5MTMifX19")
                        .asItemBuilder()
                        .setDisplayName("&aPrevious Page")
                        .build();
                setItem(48, previous, (p,a) -> new ChatLogGUI(target, uuid, page-1).open(p));
            }

            final ItemStack next = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTYzMzlmZjJlNTM0MmJhMThiZGM0OGE5OWNjYTY1ZDEyM2NlNzgxZDg3ODI3MmY5ZDk2NGVhZDNiOGFkMzcwIn19fQ==")
                    .asItemBuilder()
                    .setDisplayName("&aNext Page")
                    .build();
            setItem(50, next, (p,a) -> new ChatLogGUI(target, uuid, page+1).open(p));

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    final PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM chat_logs WHERE uuid = ? ORDER BY time DESC LIMIT ?, 36");
                    statement.setString(1, uuid);
                    statement.setInt(2, (page-1) * 36);
                    final ResultSet results = statement.executeQuery();

                    int slot = 9;
                    while(results.next()) {
                        final int id = results.getInt(1);
                        final String server = results.getString(2);
                        final String channel = results.getString(3);
                        final String message = results.getString(6);
                        final String time = results.getString(7);

                        addMessage(slot, id, time, server, channel, message);
                        slot++;
                    }
                }
                catch (final SQLException exception) {
                    exception.printStackTrace();
                }
            });
        }

        public void addMessage(int slot, int id, String time, String server, String channel, String message) {
            final ItemBuilder builder = new ItemBuilder(Material.PAPER)
                    .setDisplayName("&a#" + id)
                    .addLore("&aServer: &f" + server)
                    .addLore("&aChannel: &f" + channel)
                    .addLore("")
                    .addLore("&f" + message)
                    .addLore("")
                    .addLore("&aTime: &f" + time);

            setItem(slot, builder.build());
        }
    }
}