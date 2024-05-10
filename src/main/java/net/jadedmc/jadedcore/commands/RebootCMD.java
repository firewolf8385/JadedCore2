package net.jadedmc.jadedcore.commands;

import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedcore.networking.InstanceStatus;
import net.jadedmc.jadedutils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RebootCMD extends AbstractCommand {

    public RebootCMD() {
        super("reboot", "jadedcore.reboot", false);
    }

    @Override
    public void execute(final CommandSender sender, String[] args) {
        JadedAPI.getCurrentInstance().setStatus(InstanceStatus.CLOSED);

        for(final Player player : Bukkit.getOnlinePlayers()) {
            ChatUtils.chat(player, "<red><bold>The server you were on is being rebooted. You have been moved to the lobby.");
            player.performCommand("lobby");
        }
    }
}
