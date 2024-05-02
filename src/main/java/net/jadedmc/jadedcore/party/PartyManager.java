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

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class PartyManager {
    private final JadedCorePlugin plugin;
    private final Collection<Party> parties = new HashSet<>();

    public PartyManager(final JadedCorePlugin plugin) {
        this.plugin = plugin;
    }

    public Party createParty(Player leader) {
        Party party = new Party(plugin, leader);
        parties.add(party);
        return party;
    }

    public Party createParty(final Document document) {
        return new Party(plugin, document);
    }

    public void deleteParty(Party party) {
        parties.remove(party);
    }

    public Collection<Party> getParties() {
        return parties;
    }

    public Party getParty(Player player) {
        for(Party party : parties) {
            if(party.hasPlayer(player.getUniqueId())) {
                return party;
            }
        }

        return null;
    }

    public Party getPartyFromUUID(final UUID partyUUID) {
        for(Party party : parties) {
            if(party.getUniqueID().equals(partyUUID)) {
                return party;
            }
        }

        return null;
    }

    public void cacheParty(final Party party) {
        parties.add(party);
    }
}