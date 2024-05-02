package net.jadedmc.jadedcore.party;

import net.jadedmc.jadedcore.JadedCorePlugin;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;

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
}