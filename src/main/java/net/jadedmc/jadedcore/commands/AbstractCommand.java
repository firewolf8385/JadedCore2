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
package net.jadedmc.jadedcore.commands;

import net.jadedmc.jadedcore.JadedCorePlugin;
import net.jadedmc.jadedutils.chat.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractCommand implements CommandExecutor {
    private final String permission;
    private final boolean canConsoleUse;
    private static JadedCorePlugin plugin;

    /**
     * Creates a new AbstractCommand.
     * @param commandName Name of the command.
     * @param permission Permission required to use the command.
     * @param canConsoleUse Whether console can use the command.
     */
    public AbstractCommand(final String commandName, final String permission, final boolean canConsoleUse) {
        this.permission = permission;
        this.canConsoleUse = canConsoleUse;
        plugin.getCommand(commandName).setExecutor(this);
    }

    /**
     * Registers all commands in the plugin.
     * @param pl Plugin.
     */
    public static void registerCommands(JadedCorePlugin pl) {
        plugin = pl;

        new AnvilCMD();
        new BroadcastCMD();
        new CancelWorldCMD(pl);
        new CenterCMD();
        new ChatLogCMD(pl);
        new CommandSpyCMD(pl);
        new CopyWorldCMD(pl);
        new CreateWorldCMD(pl);
        new DiscordCMD();
        new DuelsCMD();
        new ECSeeCMD();
        new EnderchestCMD();
        new FeedCMD();
        new FlyCMD();
        new GamesCMD();
        new GrindstoneCMD();
        new HealCMD();
        new InstancesCMD(pl);
        new InvSeeCMD();
        new JamisonCMD();
        new ListAllCMD(pl);
        new ListCMD(pl);
        new LoadWorldCMD(pl);
        new LoomCMD();
        new PlayersCMD(pl);
        new ProfileCMD(pl);
        new RankCMD(pl);
        new RenameCMD();
        new RulesCMD();
        new SaveWorldCMD(pl);
        new SetLobbyCMD(pl);
        new SpawnCMD(pl);
        new StoreCMD();
        new UUIDCMD();
        new VanishCMD(pl);
        new VoteCMD();
        new WebsiteCMD();
        new WorkbenchCMD();
    }

    /**
     * Executes the command.
     * @param sender The Command Sender.
     * @param args Arguments of the command.
     */
    public abstract void execute(CommandSender sender, String[] args);

    /**
     * Processes early execution of the plugin.
     * @param sender Command Sender.
     * @param cmd The Command.
     * @param label Command Label.
     * @param args Command Arugments.
     * @return Successful Completion.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!permission.equals("") && !sender.hasPermission(permission)) {
            ChatUtils.chat(sender, "&cError &8» &cYou do not have access to that command.");
            return true;
        }
        if(!canConsoleUse && !(sender instanceof Player)) {
            ChatUtils.chat(sender, "&cError &8» &cOnly players can use that command.");
            return true;
        }
        execute(sender, args);
        return true;
    }
}