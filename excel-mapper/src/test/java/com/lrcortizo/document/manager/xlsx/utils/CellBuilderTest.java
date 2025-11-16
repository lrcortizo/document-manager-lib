package com.lrcortizo.document.manager.xlsx.utils;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CellBuilderTest {

    @ParameterizedTest
    @MethodSource("argumentsStream")
    void should_build_cell_when_new_cellBuilder(final Object givenValue, final CellType givenCellType) {
        // Given
        final XSSFRow xssfRowMock = mock(XSSFRow.class);
        final XSSFCellStyle xssfCellStyleMock = mock(XSSFCellStyle.class);
        final XSSFCell xssfCellMock = mock(XSSFCell.class);
        final int givenIndex = 0;

        when(xssfRowMock.createCell(givenIndex)).thenReturn(xssfCellMock);

        // When
        final CellBuilder actualCellBuilder = new CellBuilder(xssfRowMock, givenCellType, xssfCellStyleMock, givenIndex, givenValue);

        // Then
        assertNotNull(actualCellBuilder);
        verify(xssfRowMock, times(1))
                .createCell(givenIndex);
        verify(xssfCellMock, times(1))
                .setCellStyle(xssfCellStyleMock);
    }

    @Test
    void given_null_parameters_should_build_cell_when_new_cellBuilder() {
        // Given
        final String givenValue = "A";
        final XSSFRow xssfRowMock = mock(XSSFRow.class);
        final XSSFCell xssfCellMock = mock(XSSFCell.class);
        final int givenIndex = 0;

        when(xssfRowMock.createCell(givenIndex)).thenReturn(xssfCellMock);

        // When
        final CellBuilder actualCellBuilder = new CellBuilder(xssfRowMock, null, null, givenIndex, givenValue);

        // Then
        assertNotNull(actualCellBuilder);
        verify(xssfRowMock, times(1))
                .createCell(givenIndex);
        verify(xssfCellMock, times(1))
                .setCellValue(givenValue);
        verify(xssfCellMock, times(0))
                .setCellStyle(null);
    }

    private static Stream<Arguments> argumentsStream() {
        return Stream.of(
                Arguments.of(new Object(), CellType.STRING),
                Arguments.of("A", CellType.STRING),
                Arguments.of(LocalDateTime.of(2024, 11, 12, 11, 0), CellType.STRING),
                Arguments.of(LocalDate.of(2024, 11, 12), CellType.STRING),
                Arguments.of(Boolean.TRUE, CellType.BOOLEAN),
                Arguments.of(Integer.parseInt("1"), CellType.NUMERIC),
                Arguments.of(Double.parseDouble("20.00"), CellType.NUMERIC),
                Arguments.of("SUM(A1:B1)", CellType.FORMULA),
                Arguments.of(null, CellType.BLANK)
        );
    }
}