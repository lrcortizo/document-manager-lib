package com.lrcortizo.document.manager.xlsx.utils;

import com.lrcortizo.document.manager.xlsx.model.catalog.CellFormatCatalog;
import com.lrcortizo.document.manager.xlsx.model.catalog.CellStyleCatalog;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CellStyleManagerTest {

    @ParameterizedTest
    @EnumSource(CellFormatCatalog.class)
    void should_return_XSSFCellStyle_when_obtainDataCellStyle(final CellFormatCatalog givenCellType) {
        // Given
        final XSSFWorkbook givenWorkbook = new XSSFWorkbook();
        final CellStyleCatalog givenCellStyleCatalog = CellStyleCatalog.STANDARD;

        // When
        final XSSFCellStyle actualXSSFCellStyle = CellStyleManager
                .obtainDataCellStyle(givenWorkbook, givenCellStyleCatalog, givenCellType);

        // Then
        assertNotNull(actualXSSFCellStyle);

        Assertions.assertAll("XSSFCellStyle",
                () -> assertEquals(BorderStyle.HAIR, actualXSSFCellStyle.getBorderBottom()),
                () -> assertEquals(BorderStyle.HAIR, actualXSSFCellStyle.getBorderTop()),
                () -> assertEquals(BorderStyle.HAIR, actualXSSFCellStyle.getBorderRight()),
                () -> assertEquals(BorderStyle.HAIR, actualXSSFCellStyle.getBorderLeft()),
                () -> assertEquals(FillPatternType.SOLID_FOREGROUND, actualXSSFCellStyle.getFillPattern()),
                () -> assertTrue(actualXSSFCellStyle.getWrapText()),
                () -> assertFalse(actualXSSFCellStyle.getLocked()),
                () -> assertEquals(givenCellStyleCatalog.getCellAlignment(), actualXSSFCellStyle.getAlignment()),
                () -> assertEquals(givenCellStyleCatalog.getFontStyle().getFontName(), actualXSSFCellStyle.getFont()
                        .getFontName())
        );
    }
}