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
package net.jadedmc.jadedcore;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import net.jadedmc.jadedcore.party.Party;
import net.jadedmc.jadedcore.player.JadedPlayer;
import net.jadedmc.jadedutils.chat.ChatUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * This class will be registered through the register-method in the
 * plugins onEnable-method.
 */
class Placeholders extends PlaceholderExpansion implements Relational {
    private final JadedCorePlugin plugin;

    /**
     * Since we register the expansion inside our own plugin, we
     * can simply use this method here to get an instance of our
     * plugin.
     *
     * @param plugin
     *        The instance of our plugin.
     */
    public Placeholders(final JadedCorePlugin plugin){
        this.plugin = plugin;
    }

    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }

    /**
     * Because this is a internal class, this check is not needed
     * and we can simply return {@code true}
     *
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     * <br>For convienience do we return the author from the plugin.yml
     *
     * @return The name of the author as a String.
     */
    @Override
    public @NotNull String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public @NotNull String getIdentifier(){
        return "jadedcore";
    }

    /**
     * This is the version of the expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * For convenience do we return the version from the plugin.yml
     *
     * @return The version as a String.
     */
    @Override
    public @NotNull String getVersion(){
        return plugin.getDescription().getVersion();
    }


    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        // Overall
        if(identifier.contains("top_ap_name")) {
            int place = Integer.parseInt(identifier.replaceAll("\\D+","")) - 1;
            ArrayList<String> temp = new ArrayList<>(plugin.leaderboardManager().getAchievementPointsLeaderboard().keySet());
            if(temp.size() < place + 1) {
                return "---";
            }

            return temp.get(place);
        }

        if(identifier.contains("top_ap_value")) {
            int place = Integer.parseInt(identifier.replaceAll("\\D+","")) - 1;
            ArrayList<Integer> temp = new ArrayList<>(plugin.leaderboardManager().getAchievementPointsLeaderboard().values());

            if(temp.size() < place + 1) {
                return "---";
            }

            return temp.get(place) + "";
        }

        JadedPlayer jadedPlayer = plugin.jadedPlayerManager().getPlayer(player);

        if(jadedPlayer == null) {
            return "";
        }

        if(identifier.contains("player_name")) {
            return jadedPlayer.getName();
        }

        if(identifier.contains("player_real_name")) {
            return jadedPlayer.getRealName();
        }

        if(identifier.contains("rank_displayname_legacy")) {
            return ChatUtils.toLegacy(jadedPlayer.getRank().getDisplayName());
        }

        if(identifier.contains("rank_chat_prefix_legacy")) {
            return ChatUtils.toLegacy(jadedPlayer.getRank().getChatPrefix());
        }

        if(identifier.contains("rank_chat_color_legacy")) {
            return ChatUtils.toLegacy(jadedPlayer.getRank().getChatColor());
        }

        if(identifier.contains("rank_displayname")) {
            return jadedPlayer.getRank().getDisplayName();
        }

        if(identifier.contains("rank_chat_prefix")) {
            return jadedPlayer.getRank().getChatPrefix();
        }

        if(identifier.contains("rank_chat_color")) {
            return jadedPlayer.getRank().getChatColor();
        }

        if(identifier.contains("rank_color")) {
            return jadedPlayer.getRank().getChatColor();
        }

        if(identifier.contains("server_name")) {
            return JadedAPI.getCurrentInstance().getName();
        }

        if(identifier.contains("online")) {
            return JadedAPI.getInstanceMonitor().getPlayerCount() + "";
        }

        return null;
    }

    @Override
    public String onPlaceholderRequest(Player one, Player two, String identifier) {
        if (one == null || two == null) {
            return null; //
        }

        Party partyOne = plugin.partyManager().getParty(one);
        Party partyTwo = plugin.partyManager().getParty(two);

        if(partyOne == null || partyTwo == null) {
            return "";
        }

        if(identifier.contains("party_sign")) {
            if(partyOne.equals(partyTwo)) {
                return "&e⭐ ";
            }

            return "";
        }

        return null;
    }
}