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
import net.jadedmc.jadedutils.chat.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This class runs the vanish command, which hides a player from all others.
 */
public class VanishCMD extends AbstractCommand {
    private final JadedCorePlugin plugin;

    /**
     * Creates the /vanish command with the permission "elytracore.vanish".
     * @param plugin Instance of the plugin.
     */
    public VanishCMD(JadedCorePlugin plugin) {
        super("vanish", "jadedcore.vanish", false);
        this.plugin = plugin;
    }

    /**
     * This is the code that runs when the command is sent.
     * @param sender The player (or console) that sent the command.
     * @param args The arguments of the command.
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        JadedPlayer jadedPlayer = plugin.jadedPlayerManager().getPlayer(player);

        if(jadedPlayer.isVanished()) {
            jadedPlayer.setVanished(false);
            ChatUtils.chat(player, "&aYou are no longer vanished.");
        }
        else {
            jadedPlayer.setVanished(true);
            ChatUtils.chat(player, "&aYou are now vanished.");
        }
    }
}