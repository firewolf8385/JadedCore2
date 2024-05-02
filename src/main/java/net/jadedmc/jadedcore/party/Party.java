package net.jadedmc.jadedcore.party;

import net.jadedmc.jadedcore.JadedCorePlugin;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

/**
 * Represents a group of players playing together.
 */
public class Party {
    private final JadedCorePlugin plugin;
    private final UUID uuid;
    private final Collection<PartyPlayer> players = new HashSet<>();

    public Party(final JadedCorePlugin plugin, final Document document) {
        this.plugin = plugin;
        this.uuid = UUID.fromString(document.getString("uuid"));

        Document playersDocument = document.get("players", Document.class);
        for(String player : playersDocument.keySet()) {
            players.add(new PartyPlayer(playersDocument.get(player, Document.class)));
        }
    }

    public Party(final JadedCorePlugin plugin, Player leader) {
        this.plugin = plugin;
        this.uuid = UUID.randomUUID();
        addPlayer(leader, PartyRole.LEADER);
    }

    public void addPlayer(Player player, PartyRole role) {
        players.add(new PartyPlayer(player, role));
    }

    public boolean hasPlayer(UUID playerUUID) {
        for(PartyPlayer partyPlayer : players) {
            if(partyPlayer.getUniqueID().equals(playerUUID)) {
                return true;
            }
        }

        return false;
    }

    public void removePlayer(Player player) {
        for(PartyPlayer partyPlayer : players) {
            if(partyPlayer.getUniqueID().equals(player.getUniqueId())) {
                players.remove(partyPlayer);
                return;
            }
        }
    }

    public Document toDocument() {
        Document document = new Document();
        document.append("uuid", uuid.toString());

        Document playersDocument = new Document();
        for(PartyPlayer player : players) {
            playersDocument.append(player.getUniqueID().toString(), player.toDocument());
        }
        document.append("players", playersDocument);

        return document;
    }
}