package com.sollace.romanizer.impl.parser;

import java.text.DecimalFormatSymbols;
import java.util.*;

import com.sollace.romanizer.api.parser.Converter;

public class RomanToNumber implements Converter<String, Number> {

    @Override
    public Number convertTo(String from, Locale locale) {

        if ("nulla".equalsIgnoreCase(from)) {
            return 0;
        }

        DecimalFormatSymbols d = DecimalFormatSymbols.getInstance(locale);
        boolean negative = false;
        if (from.charAt(0) == d.getMinusSign()) {
            from = from.substring(1);
            negative = true;
        }

        String[] wholeFracture = Symbols.splitFraction(from, locale);
        int[] power = new int[1];
        long whole = parseInteger(wholeFracture[0], power);
        power[0] = 1;
        long fraction = wholeFracture.length > 1 ? parseInteger(wholeFracture[1], power) : 0;

        if (fraction > 0) {
            whole += (fraction / (power[0] > 1 ? (double)Math.pow(10F, power[0]) : 1D));
        }

        if (negative) {
            return -whole;
        }

        return whole;
    }

    private long parseInteger(String from, int[] power) {
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

        power[0] = entries.size();

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
