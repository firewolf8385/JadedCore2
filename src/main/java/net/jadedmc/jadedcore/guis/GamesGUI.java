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
package net.jadedmc.jadedcore.guis;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedcore.minigames.Minigame;
import net.jadedmc.jadedcore.player.JadedPlayer;
import net.jadedmc.jadedutils.gui.CustomGUI;
import net.jadedmc.jadedutils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

public class GamesGUI extends CustomGUI {

    public GamesGUI() {
        super(54, "Games");
        addFiller(0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);

        ItemBuilder cactusRush = new ItemBuilder(Material.CACTUS)
                .setDisplayName("<green><bold>Cactus Rush")
                .addLore("<dark_gray>Competitive")
                .addLore("")
                .addLore("<gray>Team-Based Cactus")
                .addLore("<gray>Fighting Minigame.")
                .addLore("")
                .addLore("<green>▸ Click to Connect")
                .addLore("<gray>Join " + JadedAPI.getInstanceMonitor().getPlayerCount(Minigame.CACTUS_RUSH) + " others playing!")
                .setUnbreakable(true)
                .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                .addFlag(ItemFlag.HIDE_UNBREAKABLE);
        setItem(20, cactusRush.build(), (p,a) -> JadedAPI.sendToLobby(p, Minigame.CACTUS_RUSH));

        ItemBuilder duels = new ItemBuilder(Material.IRON_SWORD)
                .setDisplayName("<green><bold>Duels")
                .addLore("<dark_gray>Competitive")
                .addLore("")
                .addLore("<gray>Arena-based pvp duels")
                .addLore("<gray>with a variety of kits!")
                .addLore("")
                .addLore("<green>▸ Click to Connect")
                .addLore("<gray>Join " + JadedAPI.getInstanceMonitor().getPlayerCount(Minigame.DUELS) + " others playing!")
                .setUnbreakable(true)
                .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                .addFlag(ItemFlag.HIDE_UNBREAKABLE);
        setItem(22, duels.build(), (p, a) -> {
            final JadedPlayer jadedPlayer = JadedAPI.getJadedPlayer(p);
            final int protocol = jadedPlayer.getProtocolVersion();
            final Minigame minigame;


            if(protocol == 0) {
                minigame = Minigame.DUELS_MODERN;
            }
            else if(protocol > ProtocolVersion.v1_9.getVersion()) {
                minigame = Minigame.DUELS_MODERN;
            }
            else {
                minigame = Minigame.DUELS_LEGACY;
            }

            JadedAPI.sendToLobby(p, minigame);
        });

        ItemBuilder elytraPvP = new ItemBuilder(Material.ELYTRA)
                .setDisplayName("<green><bold>ElytraPvP")
                .addLore("<dark_gray>Persistent Game")
                .addLore("")
                .addLore("<gray>Action-Packed pvp in the")
                .addLore("<gray>air using bows!")
                .addLore("")
                .addLore("<green>▸ Click to Connect")
                .addLore("<gray>Join " + JadedAPI.getInstanceMonitor().getPlayerCount(Minigame.ELYTRAPVP) + " others playing!")
                .setUnbreakable(true)
                .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                .addFlag(ItemFlag.HIDE_UNBREAKABLE);
        setItem(24, elytraPvP.build(), (p,a) -> JadedAPI.sendBungeecordMessage(p, "BungeeCord", "Connect", "elytrapvp"));

        ItemBuilder lobby = new ItemBuilder(Material.BOOKSHELF)
                .setDisplayName("<green><bold>Main Lobby")
                .addLore("")
                .addLore("<green>▸ Click to Connect")
                .addLore("<gray>Join " + JadedAPI.getInstanceMonitor().getPlayerCount(Minigame.HUB) + " others playing!")
                .setUnbreakable(true)
                .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                .addFlag(ItemFlag.HIDE_UNBREAKABLE);
        setItem(30, lobby.build(), (p,a) -> JadedAPI.sendToLobby(p, Minigame.HUB));

        ItemBuilder limbo = new ItemBuilder(Material.BEDROCK)
                .setDisplayName("<green><bold>Limbo")
                .addLore("")
                .addLore("<green>▸ Click to Connect")
                .addLore("<gray>Join " + JadedAPI.getInstanceMonitor().getPlayerCount(Minigame.LIMBO) + " others playing!")
                .setUnbreakable(true)
                .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                .addFlag(ItemFlag.HIDE_UNBREAKABLE);
        setItem(31, limbo.build(), (p,a) -> JadedAPI.sendBungeecordMessage(p, "BungeeCord", "Connect", "limbo"));

        ItemBuilder tournaments = new ItemBuilder(Material.GOLD_INGOT)
                .setDisplayName("<green><bold>Tournament Lobby")
                .addLore("")
                .addLore("<green>▸ Click to Connect")
                .addLore("<gray>Join " + JadedAPI.getInstanceMonitor().getPlayerCount(Minigame.TOURNAMENTS) + " others playing!")
                .setUnbreakable(true)
                .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                .addFlag(ItemFlag.HIDE_UNBREAKABLE);
        setItem(32, tournaments.build(), (p,a) -> {
            final JadedPlayer jadedPlayer = JadedAPI.getJadedPlayer(p);
            final int protocol = jadedPlayer.getProtocolVersion();
            final Minigame minigame;


            if(protocol == 0) {
                minigame = Minigame.TOURNAMENTS_MODERN;
            }
            else if(protocol > ProtocolVersion.v1_9.getVersion()) {
                minigame = Minigame.TOURNAMENTS_MODERN;
            }
            else {
                minigame = Minigame.TOURNAMENTS_LEGACY;
            }

            JadedAPI.sendToLobby(p, minigame);
        });
    }
}