package com.lrcortizo.document.manager.xlsx.utils;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;


@UtilityClass
public class SheetTextManager {

    private static final String JOINER = ", ";
    private static final String DIVIDER = ",";

    public String createCommaSeparatedValues(final List<String> elements) {
        return String.join(JOINER, elements);
    }


    public List<Boolean> splitCommaSeparatedBooleans(final String commaSeparatedValue) {
        return Stream.of(splitStrings(commaSeparatedValue))
                .filter(Predicate.not(String::isBlank))
                .map(String::trim)
                .map(Boolean::parseBoolean)
                .toList();
    }

    public List<String> splitCommaSeparatedStrings(final String commaSeparatedValue) {
        return Stream.of(splitStrings(commaSeparatedValue))
                .filter(Predicate.not(String::isBlank))
                .map(String::trim)
                .toList();
    }

    private String[] splitStrings(final String commaSeparatedValue) {
        return commaSeparatedValue != null ?
                commaSeparatedValue.split(DIVIDER) :
                new String[]{};
    }
}
