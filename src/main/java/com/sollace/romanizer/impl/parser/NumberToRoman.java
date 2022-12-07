package com.sollace.romanizer.impl.parser;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.sollace.romanizer.api.parser.Converter;

public class NumberToRoman implements Converter<Number, String> {

    @Override
    public String convertTo(Number from, Locale locale) {
        boolean neg = from.doubleValue() < 0;
        long iFrom = Math.abs(from.longValue());
        long dFrom = Symbols.getFractionalComponent(Math.abs(from.doubleValue()), locale);

        if (iFrom == 0 && dFrom == 0) {
            return Symbols.ZERO;
        }

        StringBuilder output = new StringBuilder();

        DecimalFormatSymbols d = DecimalFormatSymbols.getInstance(locale);

        if (neg) {
            output.append(d.getMinusSign());
        }

        parseLong(iFrom, output);

        if (dFrom > 0) {
            output.append(d.getDecimalSeparator());
            parseLong(dFrom, output);
        }
        return output.toString();
    }

    private void parseLong(long iFrom, StringBuilder output) {
        PartitionedNumber n = PartitionedNumber.create(iFrom);
        long thousands = n.thousands();
        if (thousands > 3) {
            parseLong(thousands, output);
            output.append(Symbols.EXT_HUNDREDS);
            output.append(Symbols.THOUSAND);
        } else {
            repeat(Symbols.THOUSAND, thousands, output);
        }
        parseSecond(n.fiveHundreds(), Symbols.THOUSAND, Symbols.FIVE_HUNDRED, 2, output);
        parseSecond(n.hundreds(), Symbols.FIVE_HUNDRED, Symbols.HUNDRED, 5, output);

        byte tens = (byte)(n.tens() + n.fifties() * 5);

        int indexValue = Symbols.VALUES[Symbols.findNearest(tens)];

        if (indexValue == 10) {
            byte units = (byte)(10 - n.units());
            int unitIndexValue = Symbols.VALUES[Symbols.findNearest(units)];
            if (tens == 9 && unitIndexValue < 3) {
                parseGeneric(units, Symbols.ONE, output);
                output.append(Symbols.HUNDRED);
            } else {
                repeat(Symbols.TEN, indexValue - tens, output);
                output.append(Symbols.HUNDRED);
                parseGeneric(n.units(), Symbols.ONE, output);
            }
        } else {
            parseSecond(n.fifties(), Symbols.HUNDRED, Symbols.FIFTY, 2, output);
            parseSecond(n.tens(), Symbols.FIFTY, Symbols.TEN, 5, output);
            parseGeneric(n.units(), Symbols.ONE, output);
        }
    }

    private void parseSecond(byte units, String originSymbol, String offsetSymbol, int switchZone, StringBuilder output) {
        if (units == 0) {
            return;
        }

        int index = Symbols.findNearest(units);
        int indexValue = Symbols.VALUES[index];

        if (indexValue == switchZone) {
            repeat(offsetSymbol, switchZone - units, output);
            output.append(originSymbol);
        } else {
            repeat(offsetSymbol, units, output);
        }
    }

    private void parseGeneric(byte units, String offsetSymbol, StringBuilder output) {
        if (units == 0) {
            return;
        }
        int index = Symbols.findNearest(units);

        int indexValue = Symbols.VALUES[index];

        if (units < indexValue) {
            repeat(offsetSymbol, indexValue - units, output);
        }
        output.append(Symbols.SYMBOLS[index]);
        if (units > indexValue) {
            repeat(offsetSymbol, units - indexValue, output);
        }
    }

    private void repeat(String symbol, long times, StringBuilder output) {
        for (long i = 0; i < times; i++) {
            output.append(symbol);
        }
    }


    record PartitionedNumber (long thousands, byte fiveHundreds, byte hundreds, byte fifties, byte tens, byte units) {
        static PartitionedNumber create(long number) {
            byte tens = (byte)((number / 10) % 10);
            byte hundred = (byte)((number / 100) % 10);

            return new PartitionedNumber(
                number / 1000L,
                (byte)(hundred / 5),
                (byte)(hundred % 5),
                (byte)(tens / 5),
                (byte)(tens % 5),
                (byte)(number % 10)
            );
        }
    }
}
