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

public class PartyManager {
    private final JadedCorePlugin plugin;
    private final PartySet localParties = new PartySet();

    public PartyManager(final JadedCorePlugin plugin) {
        this.plugin = plugin;
    }

    @Deprecated
    public Party createParty(Player leader) {
        Party party = new Party(plugin, leader);
        localParties.add(party);
        return party;
    }

    @Deprecated
    public Party createParty(final Document document) {
        return new Party(plugin, document);
    }

    @Deprecated
    public void deleteParty(Party party) {
        localParties.remove(party);
    }

    @Deprecated
    public Party getParty(Player player) {
        for(Party party : localParties) {
            if(party.hasPlayer(player.getUniqueId())) {
                return party;
            }
        }

        return null;
    }

    @Deprecated
    public Party getPartyFromUUID(final UUID partyUUID) {
        for(Party party : localParties) {
            if(party.getUniqueID().equals(partyUUID)) {
                return party;
            }
        }

        return null;
    }

    @Deprecated
    public void cacheParty(final Party party) {
        localParties.add(party);
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
     * Retrieves a locally-cached party from its UUID.
     * Returns null if non are found.
     * @param player Player to get the Party of.
     * @return Corresponding Party object.
     */
    @Nullable
    public Party getLocalPartyFromPlayer(final Player player) {
        return localParties.getFromPlayer(player);
    }

    /**
     * Retrieves a locally-cached party from its UUID.
     * Returns null if non are found.
     * @param partyUUID UUID of target Party.
     * @return Corresponding Party object.
     */
    @Nullable
    public Party getLocalPartyFromUUID(final UUID partyUUID) {
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