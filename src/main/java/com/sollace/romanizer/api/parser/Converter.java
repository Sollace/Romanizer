package com.sollace.romanizer.api.parser;

import java.util.Locale;

public interface Converter<From, To> {
    To convertTo(From from, Locale locale);

    default To convertTo(From from) {
        return convertTo(from, Locale.getDefault());
    }
}
