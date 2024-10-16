package com.sollace.romanizer.impl.parser;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

public interface Symbols {
    String ZERO = "nulla";
    String ONE = "I";
    String FIVE = "V";
    String TEN = "X";
    String FIFTY = "L";
    String HUNDRED = "C";
    String FIVE_HUNDRED = "D";
    String THOUSAND = "M";

    String EXT_HUNDREDS = "/";

    int[] VALUES = new int[] { 0, 1, 5, 10, 50, 100, 500, 1000 };
    String[] SYMBOLS = new String[] { ZERO, ONE, FIVE, TEN, FIFTY, HUNDRED, FIVE_HUNDRED, THOUSAND };
    char[] CHAR_SYMBOLS = new char[] { 'I', 'V', 'X', 'L', 'C', 'D', 'M' };

    static int valueOf(char symbol) {
        symbol = Character.toUpperCase(symbol);
        int index = indexOf(CHAR_SYMBOLS, symbol);
        if (index < 0) {
            throw new NumberFormatException("Invalid symbol: \"" + symbol + "\". Check: " + (symbol == 'C'));
        }
        return VALUES[index + 1];
    }

    private static int indexOf(char[] chars, char symbol) {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == symbol) {
                return i;
            }
        }

        return -1;
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

    static String[] splitFraction(String realNumber, Locale locale) {
        DecimalFormatSymbols d = DecimalFormatSymbols.getInstance(locale);
        String[] wholeFacture = realNumber.split("\\" + d.getDecimalSeparator());
        if (wholeFacture.length > 2) {
            throw new NumberFormatException("Multiple fractional units: " + wholeFacture.length + " for input: " + realNumber);
        }
        if (wholeFacture.length < 2) {
            return new String[] {realNumber, "0"};
        }
        return wholeFacture;
    }

    static long getFractionalComponent(Number realishNumber, Locale locale) {
        // TODO: Need better fractional handling
        return 0;
        //return Long.valueOf(splitFraction(String.format(locale, "%.90f", realishNumber.doubleValue()), locale)[1]);
    }
}
