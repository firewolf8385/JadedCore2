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

import java.time.LocalDate;

/**
 * A collection of utilities that make dealing with Dates easier.
 */
public class DateUtils {

    /**
     * Gets a String of the current date in MM/DD/YY form.
     * @return Formatted date in MM/DD/YY form.
     */
    public static String currentDateToString() {
        String dateString = "";

        // Gets the current date.
        LocalDate currentDate = LocalDate.now();

        // Adds the month to the formatted string.
        if(currentDate.getMonthValue() < 10) {
            dateString += "0";
        }
        dateString += currentDate.getMonthValue();
        dateString += "/";

        // Adds the day to the formatted string.
        if(currentDate.getDayOfMonth() < 10) {
            dateString += "0";
        }
        dateString += currentDate.getDayOfMonth();
        dateString += "/";

        // Adds the year to the formatted string.
        String year = currentDate.getYear() + "";
        dateString += year.substring(Math.max(year.length() - 2, 0));

        // Returns the new string.
        return dateString;
    }
}