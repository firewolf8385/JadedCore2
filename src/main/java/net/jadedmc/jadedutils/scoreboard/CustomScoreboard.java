/*
 * This file is part of JadedUtils, licensed under the MIT License.
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
package net.jadedmc.jadedutils.scoreboard;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Represents a scoreborad that is easy
 * to create and customize.
 */
public abstract class CustomScoreboard {
    private static final HashMap<UUID, CustomScoreboard> players = new HashMap<>();

    public CustomScoreboard(Player p) {
        ScoreHelper.removeScore(p);
        players.put(p.getUniqueId(), this);
    }

    public static HashMap<UUID, CustomScoreboard> getPlayers() {
        return players;
    }

    public void addPlayer(Player p) {
        getPlayers().put(p.getUniqueId(), this);
    }

    public abstract void update(Player p);
}