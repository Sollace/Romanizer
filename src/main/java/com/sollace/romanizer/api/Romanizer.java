package com.sollace.romanizer.api;

import com.sollace.romanizer.api.parser.Converter;
import com.sollace.romanizer.impl.parser.NumberToRoman;
import com.sollace.romanizer.impl.parser.RomanToNumber;

public interface Romanizer {
    Converter<Number, String> TO_ROMAN = new NumberToRoman();
    Converter<String, Number> FROM_ROMAN = new RomanToNumber();

    static Number deromanize(String romanization) {
        return FROM_ROMAN.convert(romanization);
    }

    static String romanize(Number numerical) {
        return TO_ROMAN.convert(numerical);
    }
}
