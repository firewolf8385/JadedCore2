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
package net.jadedmc.jadedcore.networking.player;

import java.util.HashSet;
import java.util.UUID;

/**
 * This class represents a Set of NetworkPlayer objects.
 * Contains extra methods for comparing cached information inside the NetworkPlayer objects.
 */
public class NetworkPlayerSet extends HashSet<NetworkPlayer> {

    /**
     * Check if the set contains a player with a given UUID.
     * @param uuid UUID of the player to check.
     * @return Whether the set has the player.
     */
    public boolean hasPlayer(final UUID uuid) {
        for(NetworkPlayer networkPlayer : this) {
            if(networkPlayer.getUniqueUID().equals(uuid)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if the set contains a player with a given username.
     * @param username Username of the player to check.
     * @return Whether the set has the player.
     */
    public boolean hasPlayer(final String username) {
        for(NetworkPlayer networkPlayer : this) {
            if(networkPlayer.getName().equals(username)) {
                return true;
            }
        }

        return false;
    }
}