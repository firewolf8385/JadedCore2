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
 *  FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package net.jadedmc.jadedutils.player;

import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedutils.chat.ChatUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.UUID;

/**
 * The base wrapper for Bukkit Players to be used by plugins.
 * Contains many methods for making players easier to work with.
 */
public abstract class PluginPlayer {
    private final UUID playerUUID;
    private final String playerName;

    /**
     * Creates the PluginPlayer.
     * @param playerUUID UUID of the player being represented.
     * @param playerName Username of the player being represented.
     */
    public PluginPlayer(@NotNull final UUID playerUUID, @NotNull final String playerName) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
    }

    /**
     * Gets the Bukkit Player object being represented.
     * Returns null if offline.
     * @return Bukkit Player object.
     */
    @Nullable
    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(this.playerUUID);
    }


    /**
     * Get the player's username.
     * @return Username of the player.
     */
    @NotNull
    public String getName() {
        return this.playerName;
    }

    /**
     * Get the player's UUID.
     * @return UUID of the player.
     */
    @NotNull
    public UUID getUniqueId() {
        return this.playerUUID;
    }

    /**
     * Plays a given sound for the player with a given volume and pitch if they are online.
     * Skips them if they are not online.
     * @param sound Sound to play.
     * @param volume Volume to play sound at.
     * @param pitch Pitch to play sound with.
     */
    public void playSound(final Sound sound, final float volume, final float pitch) {
        final Player player = this.getBukkitPlayer();

        // If the player is not online, ignore them.
        if(player == null) {
            return;
        }

        // Plays the sound.
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    /**
     * Plays a given sound for the player with default settings of 1.0f volume and 1.0f pitch.
     * @param sound Sound to be played to the player.
     */
    public void playSound(final Sound sound) {
        this.playSound(sound, 1.0f, 1.0f);
    }

    /**
     * Sends a chat message to the player. Also works with legacy color codes and MiniMessage.
     * Skips the player if they are not online.
     * @param message Message to send to the player.
     */
    public void sendMessage(@NotNull final String message) {
        final Player player = this.getBukkitPlayer();

        // Exit if the player isn't online.
        if(player == null) {
            return;
        }

        // Sends the message using ChatUtils.
        ChatUtils.chat(player, message);
    }

    /**
     * Sends a chat message to the player. Also works with legacy color codes and MiniMessage.
     * Done through the proxy, so can reach the player even if they are on a different server in the network.
     * @param message Message to send to the player.
     */
    public void sendProxyMessage(@NotNull final String message) {
        JadedAPI.getRedis().publishAsync("proxy", "message " + playerUUID.toString() + " " + message);
    }

    /**
     * Sends a title message to the player using Adventure.
     * Supports legacy color codes and (in theory) MiniMessage.
     * @param titleString The title (top) message to send.
     * @param subTitleString The subtitle (bottom) message to send.
     * @param fadeIn How long, in milliseconds, the title should fade in for.
     * @param stay How long, in milliseconds, the title should stay on the screen for.
     * @param fadeOut How long, in milliseconds, the title should fade out for.
     */
    public void sendTitle(@NotNull final String titleString, @NotNull final String subTitleString, final int fadeIn, final int stay, final int fadeOut) {
        final Player player = this.getBukkitPlayer();

        // Exit if the player isn't online.
        if(player == null) {
            return;
        }

        // Translate the Title and Subtitle to components.
        final Component firstLine = ChatUtils.translate(titleString);
        final Component secondLine = ChatUtils.translate(subTitleString);

        // Create the Title object.
        final Title.Times times = Title.Times.times(Duration.ofMillis(fadeIn), Duration.ofMillis(stay), Duration.ofMillis(fadeOut));
        final Title title = Title.title(firstLine, secondLine, times);

        // Display the title.
        Audience.audience(player).showTitle(title);
    }
}