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
package net.jadedmc.jadedutils;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Creates a timer that counts up.
 * Used in queue and in match.
 */
public class Timer {
    private final Plugin plugin;
    private int seconds;
    private int minutes;
    private final BukkitRunnable task;

    /**
     * Create a timer.
     */
    public Timer(final Plugin plugin) {
        this.plugin = plugin;
        seconds = 0;
        minutes = 0;

        task = new BukkitRunnable() {
            @Override
            public void run() {
                seconds++;

                if(seconds == 60) {
                    seconds = 0;
                    minutes ++;
                }
            }
        };
    }

    /**
     * Create a timer from seconds.
     * @param plugin Plugin instance.
     * @param seconds Amount of seconds.
     */
    public Timer(final Plugin plugin, final int seconds) {
        this.plugin = plugin;
        task = null;

        this.minutes = seconds / 60;
        this.seconds = seconds % 60;
    }

    /**
     * Reset the timer.
     */
    public void reset() {
        seconds = 0;
        minutes = 0;
    }

    /**
     * Start the timer.
     */
    public void start() {
        task.runTaskTimer(plugin, 0, 20);
    }

    /**
     * Stop the timer.
     */
    public void stop() {
        task.cancel();
    }

    public double toMinutes() {
        return minutes + ((double) seconds / (double) 60);
    }

    /**
     * Convert the timer to seconds.
     * @return Seconds.
     */
    public int toSeconds() {
        return (60 * minutes) + seconds;
    }

    /**
     * Converts the timer into a String
     * @return String version of timer.
     */
    public String toString() {
        String minute = "";
        if(minutes < 10) {
            minute += "0";
        }
        minute += "" + minutes;

        String second = "";

        if(seconds < 10) {
            second += "0";
        }
        second += "" + seconds;

        return minute + ":" + second;
    }
}