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

import com.cryptomorin.xseries.XMaterial;
import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedcore.games.Game;
import net.jadedmc.jadedutils.chat.ChatUtils;
import net.jadedmc.jadedutils.gui.CustomGUI;
import net.jadedmc.jadedutils.items.ItemBuilder;
import net.jadedmc.jadedutils.items.SkullBuilder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;

public class DuelsGUI extends CustomGUI {
    public DuelsGUI() {
        super(54, "Duels");

        addFiller(1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);

        ItemStack comingSoon = new ItemBuilder(XMaterial.GRAY_DYE)
                .setDisplayName("<red><bold>Coming Soon")
                .build();

        setItem(22, comingSoon);

        addGame(20, Game.TOURNAMENTS);
        addGame(24, Game.MODERN_DUELS);

        ItemStack back = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=")
                .asItemBuilder()
                .setDisplayName("<red>Back")
                .build();
        setItem(0, back, (p, a) -> new GamesGUI().open(p));
    }

    private void addGame(int slot, Game game) {
        setItem(slot, getGameIcon(game), (p, a) -> JadedAPI.sendBungeecordMessage(p, "BungeeCord", "Connect", game.getServer()));
    }

    private ItemStack getGameIcon(Game game) {
        XMaterial material = game.getIconMaterial();

        if(JadedAPI.getCurrentInstance().getMajorVersion() < 9 && material == XMaterial.ELYTRA) {
            material = XMaterial.FEATHER;
        }

        ItemBuilder builder = new ItemBuilder(material)
                .setDisplayName("<green><bold>" + game.getName())
                .addLore("<dark_gray>" + game.getType())
                .addLore("")
                .addLore(ChatPaginator.wordWrap(game.getDescription(), 25), "<gray>")
                .addLore("")
                .addLore("<green>▸ Click to Connect")
                .addLore(ChatUtils.parsePlaceholders("<gray>Join %bungee_" + game.getServer() + "% others playing!"))
                .addFlag(ItemFlag.HIDE_ATTRIBUTES);
        return builder.build();
    }
}