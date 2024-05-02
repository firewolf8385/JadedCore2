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

import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedcore.player.Rank;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PartyPlayer {
    private final UUID uuid;
    private final String username;
    private PartyRole role;
    private final Rank rank;

    public PartyPlayer(final Document document) {
        this.uuid = UUID.fromString(document.getString("uuid"));
        this.username = document.getString("username");
        this.role = PartyRole.valueOf(document.getString("role"));
        this.rank = Rank.valueOf(document.getString("rank"));
    }

    public PartyPlayer(final Player player, final PartyRole role) {
        this.uuid = player.getUniqueId();
        this.username = player.getName();
        this.rank = JadedAPI.getJadedPlayer(player).getRank();
        this.role = role;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public Rank getRank() {
        return rank;
    }

    public PartyRole getRole() {
        return role;
    }

    public UUID getUniqueID() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public Document toDocument() {
        return new Document()
                .append("uuid", uuid.toString())
                .append("username", username)
                .append("role", role.toString())
                .append("rank", rank.toString());
    }
}