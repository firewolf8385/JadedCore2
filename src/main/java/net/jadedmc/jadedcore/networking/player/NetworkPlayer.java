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

import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedcore.networking.Instance;
import org.bson.Document;

import java.util.UUID;

public class NetworkPlayer {
    private final String name;
    private final UUID uuid;
    private final String skin;
    private final String serverName;

    public NetworkPlayer(Document document) {
        this.name = document.getString("displayName");
        this.uuid = UUID.fromString(document.getString("uuid"));
        this.skin = document.getString("skin");
        this.serverName = document.getString("server");
    }

    public String getName() {
        return name;
    }

    public UUID getUniqueUID() {
        return uuid;
    }

    public String getSkin() {
        return skin;
    }

    public Instance getServer() {
        return JadedAPI.getServer(serverName);
    }

    public String getServerName() {
        return serverName;
    }
}