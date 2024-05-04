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
package net.jadedmc.jadedcore.party;

import net.jadedmc.jadedcore.JadedCorePlugin;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

/**
 * Manages all existing party, both Local (stored in memory) and Remote (stored in Redis).
 */
public class PartyManager {
    private final JadedCorePlugin plugin;
    private final PartySet localParties = new PartySet();

    /**
     * Creates the party manager.
     * @param plugin Instance of the plugin.
     */
    public PartyManager(@NotNull final JadedCorePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates a Local Party with a given leader.
     * @param leader Leader of the party.
     * @return Created Party.
     */
    @NotNull
    public Party createLocalParty(@NotNull final Player leader) {
        Party party = new Party(plugin, leader);
        localParties.add(party);
        return party;
    }

    /**
     * Caches a given Party by storing it as a Local Party.
     * @param party Party to be cached.
     */
    public void cacheParty(@NotNull final Party party) {
        localParties.add(party);
    }

    /**
     * Loads a party object from a document.
     * Allows for using Party class methods on Remote Parties.
     * @param document Document to load the party object from.
     * @return Party object from the document.
     */
    @NotNull
    public Party loadPartyFromDocument(@NotNull final Document document) {
        return new Party(plugin, document);
    }

    /**
     * Retrieves a Set of all locally cached Parties.
     * @return Set containing parties stored in RAM.
     */
    @NotNull
    public PartySet getLocalParties() {
        return localParties;
    }

    /**
     * Deletes a Party from the local Party cache.
     * @param party Party to be deleted.
     */
    public void deleteLocalParty(@NotNull final Party party) {
        localParties.remove(party);
    }

    /**
     * Retrieves a locally-cached party from its UUID.
     * Returns null if non are found.
     * @param player Player to get the Party of.
     * @return Corresponding Party object.
     */
    @Nullable
    public Party getLocalPartyFromPlayer(@NotNull final Player player) {
        return localParties.getFromPlayer(player);
    }

    /**
     * Retrieves a locally-cached party from its UUID.
     * Returns null if non are found.
     * @param partyUUID UUID of target Party.
     * @return Corresponding Party object.
     */
    @Nullable
    public Party getLocalPartyFromUUID(@NotNull final UUID partyUUID) {
        return localParties.getFromUUID(partyUUID);
    }

    /**
     * Retrieves a Set of all parties stored in Redis.
     * <b>Warning: Database operation. Call asynchronously.</b>
     * @return Set containing Parties grabbed from Redis.
     */
    @NotNull
    public PartySet getRemoteParties() {
        final PartySet remoteParties = new PartySet();

        final Set<String> keys = plugin.redis().keys("parties:*");
        for(String key : keys) {
            final Document partyDocument = Document.parse(plugin.redis().get(key));
            remoteParties.add(new Party(plugin, partyDocument));
        }

        return remoteParties;
    }
}