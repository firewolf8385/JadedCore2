/*
 * This file is part of JadedUtils, licensed under the MIT License.
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
package net.jadedmc.jadedutils.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

/**
 * Makes creating Potion ItemStacks easier.
 */
public class PotionBuilder {
    ItemStack potion;
    PotionMeta potionMeta;

    /**
     * Creates the PotionBuilder.
     * Can use Potion, Splash Potion, Lingering Potion, and Tipped Arrows.
     * @param potionType Material of the potion.
     */
    public PotionBuilder(Material potionType) {
        potion = new ItemStack(potionType);
        potionMeta = (PotionMeta) potion.getItemMeta();
    }

    /**
     * Converts the builder to an ItemBuilder.
     * Gives access to lore/display name/etc.
     * @return
     */
    public ItemBuilder asItemBuilder() {
        return new ItemBuilder(build());
    }

    /**
     * Sets the Potion Data of the potion.
     * @param potionType Potion Type to set.
     * @param extended Whether the potion is extended.
     * @param upgraded Whether the potion is upgraded.
     * @return Potion Builder.
     */
    public PotionBuilder setPotionData(PotionType potionType, boolean extended, boolean upgraded) {
        // TODO: Remove this when update to 1.21 is complete.
        /*
        PotionData potionData = new PotionData(potionType, extended, upgraded);
        potionMeta.setBasePotionData(potionData);

         */
        System.out.println("Tried to set Potion Data in PotionBuilder");
        return this;
    }

    /**
     * Builds the potion without using ItemBuilder.
     * @return ItemStack of the option.
     */
    public ItemStack build() {
        potion.setItemMeta(potionMeta);
        return potion;
    }
}
