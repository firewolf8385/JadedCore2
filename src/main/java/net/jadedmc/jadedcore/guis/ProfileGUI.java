/*
 * This file is part of JadedCoreLegacy, licensed under the MIT License.
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

import net.jadedmc.jadedcore.JadedCorePlugin;
import net.jadedmc.jadedcore.achievements.Achievement;
import net.jadedmc.jadedcore.player.JadedPlayer;
import net.jadedmc.jadedutils.gui.CustomGUI;
import net.jadedmc.jadedutils.items.ItemBuilder;
import net.jadedmc.jadedutils.items.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;

/**
 * Runs the profile GUI, which displays player data.
 */
public class ProfileGUI extends CustomGUI {

    /**
     * Creates the GUI
     * @param plugin Instance of the plugin.
     * @param player Player the GUI is for.
     */
    public ProfileGUI(JadedCorePlugin plugin, Player player) {
        super(54, "Profile");
        JadedPlayer jadedPlayer = plugin.jadedPlayerManager().getPlayer(player);

        addFiller(0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String firstJoined = dateFormat.format(jadedPlayer.getFirstJoined());

        ItemStack characterInfo = new SkullBuilder(player).asItemBuilder()
                .setDisplayName("<green><bold>Character Info")
                .addLore("<gray>Player: <green>" + player.getName())
                .addLore("<gray>Rank: " + jadedPlayer.getRank().getDisplayName())
                .addLore("")
                .addLore("<gray>Level: <green>" + jadedPlayer.getLevel())
                .addLore("<gray>Experience: <green>" + jadedPlayer.getExperience())
                .addLore("")
                .addLore("<gray>First Joined: <green>" + firstJoined)
                .build();
        setItem(22, characterInfo);

        int achievementPoints = 0;
        for(Achievement achievement : jadedPlayer.getAchievements()) {
            achievementPoints += achievement.getPoints();
        }

        ItemStack achievements = new ItemBuilder(Material.DIAMOND)
                .setDisplayName("<green><bold>Achievements")
                .addLore("<gray>Unlocked: <green>" + jadedPlayer.getAchievements().size())
                .addLore("<gray>Achievement Points: <yellow>" + achievementPoints)
                .build();
        setItem(31, achievements, (p, a) -> new AchievementsGUI(plugin, p).open(p));
    }
}