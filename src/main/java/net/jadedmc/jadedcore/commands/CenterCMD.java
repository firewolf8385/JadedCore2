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

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CenterCMD extends AbstractCommand {

    public CenterCMD() {
        super("center", "jadedcore.center", false);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        double x = player.getLocation().getBlockX() + 0.5;
        double y = player.getLocation().getY();
        double z = player.getLocation().getBlockZ() + 0.5;
        float pitch = 0;

        float yaw;
        if(player.getLocation().getYaw() > -45 && player.getLocation().getYaw() < 45) {
            yaw = 0;
        }
        else if(player.getLocation().getYaw() > 45 && player.getLocation().getYaw() < 135) {
            yaw = 90;
        }
        else if(player.getLocation().getYaw() > 135 || player.getLocation().getYaw() < -135) {
            yaw = 180;
        }
        else {
            yaw = -90;
        }

        Location location = new Location(player.getLocation().getWorld(), x, y, z, yaw, pitch);
        player.teleport(location);
    }
}
