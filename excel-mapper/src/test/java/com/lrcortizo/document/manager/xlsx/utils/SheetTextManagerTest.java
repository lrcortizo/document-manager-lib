package com.lrcortizo.document.manager.xlsx.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SheetTextManagerTest {

    @Test
    void should_return_when_createCommaSeparatedValues() {
        // Given
        final List<String> givenValues = List.of("A", "B", "C");

        // When
        final String actualString = SheetTextManager.createCommaSeparatedValues(givenValues);

        // Then
        assertEquals("A, B, C", actualString);
    }

    @Test
    void should_return_string_array_when_splitCommaSeparatedStrings() {
        // Given
        final String givenCommaSeparated = "A, B,C ";

        // When
        final List<String> actualStrings = SheetTextManager.splitCommaSeparatedStrings(givenCommaSeparated);

        // Then
        assertNotNull(actualStrings);
        assertFalse(actualStrings.isEmpty());
        assertEquals("A", actualStrings.getFirst());
        assertEquals("B", actualStrings.get(1));
        assertEquals("C", actualStrings.get(2));
    }

    @Test
    void should_return_empty_array_when_splitCommaSeparatedStrings() {
        // When
        final List<String> actualStrings = SheetTextManager.splitCommaSeparatedStrings(null);

        // Then
        assertNotNull(actualStrings);
        assertTrue(actualStrings.isEmpty());
    }

    @Test
    void should_return_boolean_array_when_splitCommaSeparatedBooleans() {
        // Given
        final String givenCommaSeparated = "TRUE, FALSE, false, true";

        // When
        final List<Boolean> actualBooleans = SheetTextManager.splitCommaSeparatedBooleans(givenCommaSeparated);

        // Then
        assertNotNull(actualBooleans);
        assertFalse(actualBooleans.isEmpty());
        assertEquals(Boolean.TRUE, actualBooleans.getFirst());
        assertEquals(Boolean.FALSE, actualBooleans.get(1));
        assertEquals(Boolean.FALSE, actualBooleans.get(2));
        assertEquals(Boolean.TRUE, actualBooleans.get(3));
    }
}