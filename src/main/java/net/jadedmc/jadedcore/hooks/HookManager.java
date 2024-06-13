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
package net.jadedmc.jadedcore.hooks;

import net.jadedmc.jadedcore.JadedCorePlugin;

/**
 * Manages hooks into various third-party plugins.
 */
public class HookManager {
    private final JadedCorePlugin plugin;

    /**
     * Creates the HookManager.
     * @param plugin Instance of the plugin.
     */
    public HookManager(final JadedCorePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Get if the plugin should interact with HyNick.
     * @return Whether HyNick is enabled.
     */
    public boolean useHyNick() {
        return plugin.getServer().getPluginManager().isPluginEnabled("HyNick");
    }

    /**
     * Get if the plugin should interact with ViaVersion.
     * @return Whether ViaVersion is enabled.
     */
    public boolean useViaVersion() {
        return plugin.getServer().getPluginManager().isPluginEnabled("ViaVersion");
    }
}