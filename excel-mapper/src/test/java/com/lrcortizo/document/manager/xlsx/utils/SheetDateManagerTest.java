package com.lrcortizo.document.manager.xlsx.utils;

import com.lrcortizo.document.manager.xlsx.model.catalog.CellFormatCatalog;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SheetDateManagerTest {

    @Test
    void should_return_localDate_when_obtainDataFormat() {
        // Given
        final Object givenData = "2024-11-26";
        final CellFormatCatalog givenCellFormat = CellFormatCatalog.DATE;

        // When
       final Object actualObject = SheetDateManager.obtainDataFormat(givenData, givenCellFormat);

        // Then
        assertThat(actualObject)
                .isNotNull()
                .isInstanceOf(LocalDate.class);
    }

    @Test
    void should_return_localDateTime_when_obtainDataFormat() {
        // Given
        final Object givenData = "2024-11-26T10:55:00";
        final CellFormatCatalog givenCellFormat = CellFormatCatalog.DATE_TIME;

        // When
        final Object actualObject = SheetDateManager.obtainDataFormat(givenData, givenCellFormat);

        // Then
        assertThat(actualObject)
                .isNotNull()
                .isInstanceOf(LocalDateTime.class);
    }

    @Test
    void should_return_string_when_obtainDataFormat() {
        // Given
        final Object givenData = "String";
        final CellFormatCatalog givenCellFormat = CellFormatCatalog.STRING;

        // When
        final Object actualObject = SheetDateManager.obtainDataFormat(givenData, givenCellFormat);

        // Then
        assertThat(actualObject)
                .isNotNull()
                .isInstanceOf(String.class);
    }
}