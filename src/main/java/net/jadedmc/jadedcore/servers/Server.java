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
package net.jadedmc.jadedcore.servers;

import org.bson.Document;

public class Server {
    private final long lastHeartbeat;
    private final int online;
    private final int capacity;
    private final String name;
    private final String mode;
    private final String type;
    private final ServerStatus status;

    public Server(String json) {
        Document document = Document.parse(json);

        lastHeartbeat = document.getLong("heartbeat");
        online = document.getInteger("online");
        capacity = document.getInteger("capacity");
        name = document.getString("serverName");
        mode = document.getString("mode");
        type = document.getString("type");

        if((System.currentTimeMillis() - lastHeartbeat) > 90000) {
            status = ServerStatus.UNRESPONSIVE;
        }
        else if(online == capacity) {
            status = ServerStatus.FULL;
        }
        else {
            status = ServerStatus.valueOf(document.getString("status"));
        }
    }

    public int capacity() {
        return capacity;
    }

    public long lastHeartbeat() {
        return lastHeartbeat;
    }

    public String mode() {
        return mode;
    }

    public String name() {
        return name;
    }

    public int online() {
        return online;
    }

    public String type() {
        return type;
    }

    public ServerStatus status() {
        return status;
    }
}
