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
package net.jadedmc.jadedutils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * A group of math-related utilities.
 */
public class MathUtils {

    /**
     * A map of various suffixes when converting numbers to abbreviated forms.
     */
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    /**
     * Calculate the distance between two Locations.
     * @param a First location.
     * @param b Second location.
     * @return Distance (in blocks) between them.
     */
    public static double distance(final Location a, final Location b) {
        return Math.sqrt(square(a.getX() - b.getX()) + square(a.getY() - b.getY()) + square(a.getZ() - b.getZ()));
    }

    /**
     * Turn a number into it's abbreviated spring form.
     * @param value Number to be converted.
     * @return Abbreviated number.
     */
    public static String format(long value) {
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value);

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10);
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    /**
     * Get a trajectory vector between two locations.
     * @param from From Location
     * @param to To Location
     * @return Trajectory Vector
     */
    public static Vector getTrajectory2d(Location from, Location to) {
        return getTrajectory2d(from.toVector(), to.toVector());
    }

    /**
     * Get a trajectory vector between two vectors.
     * @param from From Vector
     * @param to To Vector
     * @return Trajectory Vector
     */
    public static Vector getTrajectory2d(Vector from, Vector to) {
        return to.clone().subtract(from).setY(0).normalize();
    }

    /**
     * Calculate the percentage obtained compared to a total.
     * @param currentValue Obtained amount.
     * @param maxValue Total amount.
     * @return Percentage of total.
     */
    public static int percent(final double currentValue, final double maxValue) {
        double percent = (currentValue/maxValue) * 100;
        return (int) percent;
    }

    /**
     * Rotates a vector a given angle.
     * @param vector Vector to rotate.
     * @param whatAngle Angle (in radians).
     * @return Rotated vector.
     */
    public static Vector rotateVector(final Vector vector, final double whatAngle) {
        double sin = Math.sin(whatAngle);
        double cos = Math.cos(whatAngle);
        double x = vector.getX() * cos + vector.getZ() * sin;
        double z = vector.getX() * -sin + vector.getZ() * cos;

        return vector.setX(x).setZ(z);
    }

    /**
     * Rounds a double to a set number of decimal places.
     * @param value The double to be rounded.
     * @param places Number of decimal places.
     * @return Rounded double.
     */
    public static double round(final double value, final int places) {
        if(places > 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bigDecimal = new BigDecimal(Double.toString(value));
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    /**
     * Square a given double.
     * @param x Double to square.
     * @return Squared double.
     */
    public static double square(final double x) {
        return x * x;
    }
}