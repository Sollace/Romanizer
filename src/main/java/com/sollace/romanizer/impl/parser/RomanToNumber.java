package com.sollace.romanizer.impl.parser;

import java.text.DecimalFormatSymbols;
import java.util.*;

import com.sollace.romanizer.api.parser.Converter;

public class RomanToNumber implements Converter<String, Number> {

    @Override
    public Number convertTo(String from, Locale locale) {
        if (Symbols.ZERO.equalsIgnoreCase(from)) {
            return 0;
        }

        DecimalFormatSymbols d = DecimalFormatSymbols.getInstance(locale);
        boolean negative = false;
        if (from.charAt(0) == d.getMinusSign()) {
            from = from.substring(1);
            negative = true;
        }

        String[] wholeFracture = Symbols.splitFraction(from, locale);
        double whole = parseInteger(wholeFracture[0]);
        long fraction = wholeFracture.length > 1 ? parseInteger(wholeFracture[1]) : 0;

        if (fraction > 0) {
            int power = String.valueOf(fraction).length();
            whole += (fraction / (power > 1 ? (double)Math.pow(10F, power) : 1D));
        }

        if (negative) {
            return -whole;
        }

        return whole;
    }

    private long parseInteger(String from) {
        if ("0".contentEquals(from) || "nulla".equalsIgnoreCase(from)) {
            return 0L;
        }

        Stack<StackEntry> entries = new Stack<>();
        entries.add(new StackEntry());

        char[] characters = from.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            StackEntry head = entries.peek();

            if (characters[i] == Symbols.EXT_HUNDREDS.charAt(0)) {
                long multiplier = valueOf(entries);
                entries.clear();
                i++;
                entries.push(new StackEntry()).base = multiplier * Symbols.valueOf(characters[i]);
                continue;
            }

            int base = Symbols.valueOf(characters[i]);

            if (base == head.base) {
                head.multiplier++;
            } else if (base > head.base) {
                head.multiplier *= -1;
                entries.push(new StackEntry()).base = base;
            } else {
                entries.push(new StackEntry()).base = base;
            }
        }

        return valueOf(entries);
    }

    private long valueOf(Stack<StackEntry> entries) {
        return entries.stream().mapToLong(entry -> entry.base * entry.multiplier).sum();
    }

    static class StackEntry {
        int multiplier = 1;
        long base;
    }
}
