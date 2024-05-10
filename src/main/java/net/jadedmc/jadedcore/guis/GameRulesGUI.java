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

import net.jadedmc.jadedutils.gui.CustomGUI;
import net.jadedmc.jadedutils.items.ItemBuilder;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;

public class GameRulesGUI extends CustomGUI {

    public GameRulesGUI(final World world) {
        super(54, "Game Rules");

        addFiller(0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);

        ItemBuilder dayLightCycle = new ItemBuilder(Material.DAYLIGHT_DETECTOR).setDisplayName("<green><bold>doDayLightCycle");
        setItem(21, dayLightCycle.build());

        // Daylight Cycle toggle
        if(Boolean.TRUE.equals(world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE))) {
            ItemBuilder toggle = new ItemBuilder(Material.LIME_DYE).setDisplayName("<green>Enabled");
            setItem(30, toggle.build(), (p, a) -> {
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                new GameRulesGUI(world).open(p);
            });
        }
        else {
            ItemBuilder toggle = new ItemBuilder(Material.GRAY_DYE).setDisplayName("<red>Disabled");
            setItem(30, toggle.build(), (p, a) -> {
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
                new GameRulesGUI(world).open(p);
            });
        }

        ItemBuilder weatherCycle = new ItemBuilder(Material.WATER_BUCKET).setDisplayName("<green><bold>doWeatherCycle");
        setItem(23, weatherCycle.build());

        // Weather Cycle toggle
        if(Boolean.TRUE.equals(world.getGameRuleValue(GameRule.DO_WEATHER_CYCLE))) {
            ItemBuilder toggle = new ItemBuilder(Material.LIME_DYE).setDisplayName("<green>Enabled");
            setItem(32, toggle.build(), (p, a) -> {
                world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                new GameRulesGUI(world).open(p);
            });
        }
        else {
            ItemBuilder toggle = new ItemBuilder(Material.GRAY_DYE).setDisplayName("<red>Disabled");
            setItem(32, toggle.build(), (p, a) -> {
                world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);
                new GameRulesGUI(world).open(p);
            });
        }
    }

}
