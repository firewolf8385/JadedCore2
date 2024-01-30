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
package net.jadedmc.jadedcore.databases;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import net.jadedmc.jadedcore.JadedCorePlugin;

import java.util.Objects;

/**
 * Manages the connection process to MongoDB.
 */
public class MongoDB {
    private final MongoClient client;
    private final MongoDatabase database;

    /**
     * Connects to MongoDB.
     * @param plugin Instance of the plugin.
     */
    public MongoDB(final JadedCorePlugin plugin) {
        ConnectionString connectionString = new ConnectionString(Objects.requireNonNull(plugin.settingsManager().getConfig().getString("MongoDB.connection")));
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).build();

        client = MongoClients.create(settings);
        database = client.getDatabase("maps");
    }

    /**
     * Gets the current MongoDB client.
     * @return MongoDB client.
     */
    public MongoClient client() {
        return client;
    }

    /**
     * Gets the current MongoDB Database
     * @return MongoDB database.
     */
    public MongoDatabase database() {
        return database;
    }
}