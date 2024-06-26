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
import net.jadedmc.jadedcore.commands.AbstractCommand;
import net.jadedmc.jadedcore.databases.MongoDB;
import net.jadedmc.jadedcore.databases.MySQL;
import net.jadedmc.jadedcore.databases.Redis;
import net.jadedmc.jadedcore.hooks.HookManager;
import net.jadedmc.jadedcore.leaderboards.LeaderboardManager;
import net.jadedmc.jadedcore.listeners.*;
import net.jadedmc.jadedcore.lobby.LobbyManager;
import net.jadedmc.jadedcore.networking.InstanceMonitor;
import net.jadedmc.jadedcore.party.PartyManager;
import net.jadedmc.jadedcore.player.JadedPlayerManager;
import net.jadedmc.jadedcore.settings.SettingsManager;
import net.jadedmc.jadedcore.worlds.WorldManager;
import net.jadedmc.jadedutils.FileUtils;
import net.jadedmc.jadedutils.gui.GUIListeners;
import net.jadedmc.jadedutils.scoreboard.ScoreboardUpdate;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class JadedCorePlugin extends JavaPlugin {
    private AchievementManager achievementManager;
    private HookManager hookManager;
    private JadedPlayerManager jadedPlayerManager;
    private LeaderboardManager leaderboardManager;
    private LobbyManager lobbyManager;
    private MongoDB mongoDB;
    private MySQL mySQL;
    private PartyManager partyManager;
    private Redis redis;
    private SettingsManager settingsManager;
    private WorldManager worldManager;
    private InstanceMonitor instanceMonitor;

    @Override
    public void onEnable() {
        // Initialize API
        new JadedAPI(this);

        // Load settings.
        settingsManager = new SettingsManager(this);
        hookManager = new HookManager(this);

        // Load Databases
        mongoDB = new MongoDB(this);
        mySQL = new MySQL(this);
        mySQL.openConnection();
        redis = new Redis(this);

        // Load other managers.
        achievementManager = new AchievementManager(this);
        jadedPlayerManager = new JadedPlayerManager(this);
        leaderboardManager = new LeaderboardManager(this);
        worldManager = new WorldManager(this);
        lobbyManager = new LobbyManager(this);
        partyManager = new PartyManager(this);

        // Register commands and listeners.
        registerListeners();
        AbstractCommand.registerCommands(this);

        // Allow the plugin to send messages to the proxy.
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        // Register placeholders.
        new Placeholders(this).register();

        // Updates scoreboards every second
        new ScoreboardUpdate().runTaskTimer(this, 20L, 20L);

        // Downloads configured worlds.
        updateWorlds();

        instanceMonitor = new InstanceMonitor(this);
    }

    @Override
    public void onDisable() {
        // Deletes the search from Redis
        redis.del("servers:" + settingsManager.getConfig().getString("serverName"));
    }

    /**
     * Registers plugin listeners.
     */
    private void registerListeners() {
        // Plugin listeners.
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        getServer().getPluginManager().registerEvents(new ChannelMessageSendListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChangeListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerChangeWorldListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerCommandPreprocessListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new RedisMessageListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldInitListener(this), this);
        new UserDataRecalculateListener(this, LuckPermsProvider.get());

        // Utility listeners.
        getServer().getPluginManager().registerEvents(new GUIListeners(), this);
    }

    /**
     * Updates worlds listed in the config file.
     * Called when the server is first started.
     */
    private void updateWorlds() {
        ConfigurationSection worldsSection = settingsManager.getConfig().getConfigurationSection("Worlds");

        // Exit if there is no section.
        if(worldsSection == null) {
            return;
        }

        // Loops through each world configured.
        for(String world : worldsSection.getKeys(false)) {
            String fileName = settingsManager.getConfig().getString("Worlds." + world);

            // Deletes the world folder if it currently already exists.
            File worldFolder = new File(getServer().getPluginsFolder().getParent(), world);
            if(worldFolder.exists()) {
                FileUtils.deleteDirectory(worldFolder);
            }

            // Downloads the world from MongoDB.
            File newWorld = worldManager.downloadWorldSync(fileName);
            newWorld.renameTo(worldFolder);
        }
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

    public InstanceMonitor instanceMonitor() {
        return instanceMonitor;
    }

    /**
     * Get the Jaded Player manager, which manages player data.
     * @return Jaded Player manager.
     */
    public JadedPlayerManager jadedPlayerManager() {
        return jadedPlayerManager;
    }

    /**
     * Get the Leaderboard Manager, which controls leaderboards.
     * @return Leaderboard Manager.
     */
    public LeaderboardManager leaderboardManager() {
        return leaderboardManager;
    }

    /**
     * Get the Lobby Manager, which controls everything related to lobbies.
     * @return Lobby Manager.
     */
    public LobbyManager lobbyManager() {
        return lobbyManager;
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
     * Get the party manager, which controls everything related to parties.
     * @return Party Manager.
     */
    public PartyManager partyManager() {
        return partyManager;
    }

    /**
     * Gets the Redis connection.
     * @return Redis.
     */
    public Redis redis() {
        return redis;
    }

    /**
     * Gets the server's configuration files.
     * @return Settings Manager.
     */
    public SettingsManager settingsManager() {
        return settingsManager;
    }

    /**
     * Get the world manager, which manages savings and storing worlds.
     * @return World Manager.
     */
    public WorldManager worldManager() {
        return worldManager;
    }
}