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
package net.jadedmc.jadedcore.worlds.generators;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public abstract class JadedChunkGenerator extends ChunkGenerator {
    private final String id;

    public JadedChunkGenerator(final String id) {
        this.id = id;
    }

    /**
     * Generates a chunk.
     * @param world World to generate chunk in.
     * @param random Random number generator (unused).
     * @param x Chunk x location (unused)
     * @param z Chunk y location (unused)
     * @param biome Chunk biomes.
     * @return Generated Chunk Data.
     */
    @Override
    public abstract ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome);

    /**
     * Sets the world spawn location.
     * @param world World to set spawn location of.
     * @param random Random number generator (unused).
     * @return Spawn location.
     */
    @Override
    public Location getFixedSpawnLocation(@NotNull World world, @NotNull Random random) {
        return new Location(world, 0, 60, 0);
    }

    /**
     * Gets the ID of the generator.
     * @return Generator id.
     */
    public String getId() {
        return id;
    }

    /**
     * Disables noise generation.
     * @return false.
     */
    @Override
    public boolean shouldGenerateNoise() {
        return false;
    }

    /**
     * Disable surface generation.
     * @return false
     */
    @Override
    public boolean shouldGenerateSurface() {
        return false;
    }

    /**
     * Disables bedrock generation.
     * @return false
     */
    @Override
    public boolean shouldGenerateBedrock() {
        return false;
    }

    /**
     * Disables cave generation.
     * @return false
     */
    @Override
    public boolean shouldGenerateCaves() {
        return false;
    }

    /**
     * Disables decoration generation.
     * @return false
     */
    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }

    /**
     * Disables generating mobs like villagers.
     * @return false
     */
    @Override
    public boolean shouldGenerateMobs() {
        return false;
    }

    /**
     * Disables structure generation.
     * @return false
     */
    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }
}
