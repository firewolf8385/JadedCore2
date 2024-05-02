package net.jadedmc.jadedcore.commands;

import net.jadedmc.jadedcore.JadedCorePlugin;
import net.jadedmc.jadedcore.party.Party;
import net.jadedmc.jadedutils.chat.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyCMD extends AbstractCommand {
    private final JadedCorePlugin plugin;

    public PartyCMD(final JadedCorePlugin plugin) {
        super("party", "", false);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
    }

    public void createCMD(final Player player) {
        // Makes sure the player is not already in a party.
        if(plugin.partyManager().getParty(player) != null) {
            ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>You are already in a party.");
            return;
        }

        Party party = plugin.partyManager().createParty(player);
        party.update();

        ChatUtils.chat(player, "<green><bold>Party</bold> <dark_gray>» <green>Party has been created.");
    }
}