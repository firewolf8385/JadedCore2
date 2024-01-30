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
package net.jadedmc.jadedcore;

import net.jadedmc.jadedcore.achievements.AchievementManager;
import net.jadedmc.jadedcore.databases.MongoDB;
import net.jadedmc.jadedcore.databases.MySQL;
import net.jadedmc.jadedcore.hooks.HookManager;
import net.jadedmc.jadedcore.player.JadedPlayerManager;
import net.jadedmc.jadedcore.settings.SettingsManager;
import net.jadedmc.jadedutils.gui.GUIListeners;
import org.bukkit.plugin.java.JavaPlugin;

public final class JadedCorePlugin extends JavaPlugin {
    private AchievementManager achievementManager;
    private HookManager hookManager;
    private JadedPlayerManager jadedPlayerManager;
    private SettingsManager settingsManager;
    private MongoDB mongoDB;
    private MySQL mySQL;

    @Override
    public void onEnable() {
        // Load settings.
        settingsManager = new SettingsManager(this);
        hookManager = new HookManager(this);

        // Load Databases
        mongoDB = new MongoDB(this);
        mySQL = new MySQL(this);

        // Load other managers.
        achievementManager = new AchievementManager(this);
        jadedPlayerManager = new JadedPlayerManager(this);

        // Register commands and listeners.
        registerListeners();
    }

    /**
     * Registers plugin listeners.
     */
    private void registerListeners() {
        // Utility listeners.
        getServer().getPluginManager().registerEvents(new GUIListeners(), this);
    }

    /**
     * Get the Achievement Manager, which controls Achievements.
     * @return AchievementManager.
     */
    public AchievementManager achievementManager() {
        return achievementManager;
    }

    /**
     * Get the Hook Manager, which controls what third-party plugins we hook into.
     * @return HookManager.
     */
    public HookManager hookManager() {
        return hookManager;
    }

    /**
     * Get the Jaded Player manager, which manages player data.
     * @return Jaded Player manager.
     */
    public JadedPlayerManager jadedPlayerManager() {
        return jadedPlayerManager;
    }

    /**
     * Gets the MongoDB connection.
     * @return MongoDB.
     */
    public MongoDB mongoDB() {
        return mongoDB;
    }

    /**
     * Be able to connect to MySQL.
     * @return MySQL.
     */
    public MySQL mySQL() {
        return mySQL;
    }

    /**
     * Gets the server's configuration files.
     * @return Settings Manager
     */
    public SettingsManager settingsManager() {
        return settingsManager;
    }
}