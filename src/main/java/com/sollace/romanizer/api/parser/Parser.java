package com.sollace.romanizer.api.parser;

public interface Parser<From, To> {
    To parse(From from);
}
