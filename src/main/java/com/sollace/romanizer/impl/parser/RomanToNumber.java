package com.sollace.romanizer.impl.parser;

import java.util.*;

import com.sollace.romanizer.api.parser.Parser;

public class RomanToNumber implements Parser<String, Number> {

    @Override
    public Number parse(String from) {

        String[] wholeFracture = Symbols.splitFraction(from);
        int[] power = new int[1];
        long whole = parseInteger(wholeFracture[0], power);
        power[0] = 1;
        long fraction = wholeFracture.length > 1 ? parseInteger(wholeFracture[1], power) : 0;

        if (fraction > 0) {
            return whole + (fraction / (power[0] > 1 ? (double)Math.pow(10F, power[0]) : 1D));
        }

        return whole;
    }

    private long parseInteger(String from, int[] power) {
        Stack<StackEntry> entries = new Stack<>();
        entries.add(new StackEntry());

        char[] characters = from.toCharArray();
        for (int i = characters.length - 1; i >= 0; i--) {
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
            } else {
                if (base < head.base) {
                    head.multiplier *= -1;
                }
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
