package com.sollace.romanizer.impl.parser;

import java.util.Arrays;

public interface Symbols {
    String NEGATIVE_SIGN = "-";
    String DECIMAL_POINT = ".";

    String ZERO = "";
    String ONE = "I";
    String FIVE = "V";
    String TEN = "X";
    String HUNDRED = "C";

    String EXT_HUNDREDS = "/";

    int[] VALUES = new int[] {0, 1, 5, 10, 100};
    String[] SYMBOLS = new String[] { ZERO, ONE, FIVE, TEN, HUNDRED };
    char[] CHAR_SYMBOLS = new char[] { 'I', 'V', 'X', 'C' };

    static int valueOf(char symbol) {
        symbol = Character.toUpperCase(symbol);
        int index = Arrays.binarySearch(CHAR_SYMBOLS, symbol);
        if (index < 0) {
            throw new NumberFormatException("Invalid symbol: \"" + symbol + "\".");
        }
        return VALUES[index + 1];
    }

    static int findNearest(int value) {
        for (int i = 0; i < VALUES.length; i++) {

            int before = VALUES[i];
            int after = VALUES[(i + 1) % VALUES.length];

            if (before <= value && after >= value) {

                int distanceBefore = value - before;
                int distanceAfter = after - value;

                if (distanceBefore <= distanceAfter) {
                    return i;
                }
                return i + 1;
            }
        }

        return VALUES.length - 1;
    }

    static String[] splitFraction(String realNumber) {
        String[] wholeFacture = realNumber.split("\\.");
        if (wholeFacture.length > 2) {
            throw new NumberFormatException("Multiple fractional units: " + wholeFacture.length + " for input: " + realNumber);
        }
        return wholeFacture;
    }

    static long getFractionalComponent(Number realishNumber) {
        return Long.valueOf(splitFraction(String.valueOf(realishNumber))[1]);
    }
}
