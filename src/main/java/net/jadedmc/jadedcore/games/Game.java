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
package net.jadedmc.jadedcore.games;

import com.cryptomorin.xseries.XMaterial;

/**
 * Represents a game on the network.
 */
public enum Game {
    ELYTRAPVP("ElytraPvP", XMaterial.FEATHER, GameType.PERSISTENT, "Action-Packed pvp in the air using bows!", "elytrapvp"),
    TURFWARS("Turf Wars", XMaterial.BOW, GameType.COMPETITIVE, "Advance your team's territory by killing other players.", "turfwars"),
    CACTUS_RUSH("Cactus Rush", XMaterial.CACTUS, GameType.COMPETITIVE, "Team-Based Cactus Fighting Minigame.", "cactusrush"),
    HOUSING("Housing", XMaterial.DARK_OAK_DOOR, GameType.PERSISTENT, "Create in your own mini-world, or visit someone else's!", "housing"),
    LOBBY("Main Lobby", XMaterial.CRAFTING_TABLE, GameType.NONE, "", "lobby"),
    GENERAL("General", XMaterial.BOOK, GameType.NONE, "", ""),
    DUELS("Duels", XMaterial.IRON_SWORD, GameType.COMPETITIVE, "Arena-Based PvP duels and tournaments!", ""),
    MODERN_DUELS("1.20 Duels", XMaterial.DIAMOND_SWORD, GameType.COMPETITIVE, "Arena-Based pvp duels and tournaments for modern minecraft.", "modernduels"),
    LEGACY_DUELS("1.8 Duels", XMaterial.GOLDEN_SWORD, GameType.COMPETITIVE, "", "legacyduels"),
    TOURNAMENTS("1.8 Tournaments", XMaterial.GOLD_INGOT, GameType.COMPETITIVE, "Automated 1.8 PvP Tournaments, hosted by staff!", "tournament");

    private final String name;
    private final XMaterial iconMaterial;
    private final GameType type;
    private final String description;
    private final String server;

    Game(String name, XMaterial iconMaterial, GameType type, String description, String server) {
        this.name = name;
        this.iconMaterial = iconMaterial;
        this.type = type;
        this.description = description;
        this.server = server;
    }

    /**
     * Get the description of the game.
     * @return The description of the game.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the material the game icon is made of.
     * @return Game icon material.
     */
    public XMaterial getIconMaterial() {
        return iconMaterial;
    }

    /**
     * Get the name of the game.
     * @return Game name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the server the game is hosted on.
     * @return Server to send players to.
     */
    public String getServer() {
        return server;
    }

    /**
     * Gets the GameType of the game.
     * @return GameType.
     */
    public GameType getType() {
        return type;
    }
}