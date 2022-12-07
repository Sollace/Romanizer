package com.sollace.romanizer.impl.parser;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.sollace.romanizer.api.parser.Converter;

public class NumberToRoman implements Converter<Number, String> {

    @Override
    public String convertTo(Number from, Locale locale) {
        boolean neg = from.doubleValue() < 0;
        long iFrom = Math.abs(from.longValue());

        StringBuilder output = new StringBuilder();

        DecimalFormatSymbols d = DecimalFormatSymbols.getInstance(locale);

        if (neg) {
            output.append(d.getMinusSign());
        }

        // 99
        // XXXXXXXXXIX

        // IC

        parseLong(iFrom, output);

        double dFrom = Math.abs(from.doubleValue());
        if (dFrom > 0) {
            output.append(d.getDecimalSeparator());
            parseLong(Symbols.getFractionalComponent(dFrom, locale), output);
        }
        return output.toString();
    }

    private void parseLong(long iFrom, StringBuilder output) {
        parseHundreds(new PartitionedNumber(iFrom), output);
    }

    private void parseHundreds(PartitionedNumber n, StringBuilder output) {
        long hundreds = n.hundreds();
        if (hundreds > 3) {
            parseLong(hundreds, output);
            output.append(Symbols.EXT_HUNDREDS);
            output.append(Symbols.HUNDRED);
        } else {
            repeat(Symbols.HUNDRED, hundreds, output);
        }
        parseTens(n, output);
    }

    private void parseTens(PartitionedNumber n, StringBuilder output) {

        if (n.tens() == 0) {
            parseUnits(n, output);
            return;
        }

        int index = Symbols.findNearest(n.tens());
        int indexValue = Symbols.VALUES[index];

        if (indexValue == 10) {
            // 80,90
            parseUnits(n, output);
            output.append(Symbols.HUNDRED);
            return;
        }

        repeat(Symbols.TEN, n.tens(), output);
        parseUnits(n, output);
    }

    private void parseUnits(PartitionedNumber n, StringBuilder output) {
        byte units = n.units();
        if (units == 0) {
            return;
        }

        int index = Symbols.findNearest(units);

        int indexValue = Symbols.VALUES[index];

        if (indexValue < units) {
            repeat(Symbols.ONE, units - indexValue, output);
        }
        output.append(Symbols.SYMBOLS[index]);
        if (indexValue > units) {
            repeat(Symbols.ONE, indexValue - units, output);
        }
    }

    private void repeat(String symbol, long times, StringBuilder output) {
        for (long i = 0; i < times; i++) {
            output.append(symbol);
        }
    }


    record PartitionedNumber (long hundreds, byte tens, byte units) {
        PartitionedNumber(long number) {
            this(number / 100L,
                (byte)((number / 10) % 10),
                (byte)(number % 10)
            );
        }
    }
}
