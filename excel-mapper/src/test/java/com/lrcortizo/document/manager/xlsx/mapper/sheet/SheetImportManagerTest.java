package com.lrcortizo.document.manager.xlsx.mapper.sheet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lrcortizo.document.manager.xlsx.exception.ExcelImportException;
import com.lrcortizo.document.manager.xlsx.model.SheetDTO;
import com.lrcortizo.document.manager.xlsx.test.model.SheetTestDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class SheetImportManagerTest {

    private final Cell cellMock;

    public SheetImportManagerTest() {
        this.cellMock = mock(Cell.class);
        this.cellMock();
    }

    @ParameterizedTest
    @EnumSource(CellType.class)
    void should_return_sheetDTOs_when_obtainSheets(final CellType givenCellType) {
        // Given
        final Row rowMock = mock(Row.class);
        final XSSFSheet xssfSheetMock = mock(XSSFSheet.class);
        final ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        final Class<? extends SheetDTO> givenSheetModel = SheetTestDTO.class;

        when(this.cellMock.getCellType())
                .thenReturn(givenCellType);
        when(this.cellMock.getColumnIndex())
                .thenReturn(0);
        when(rowMock.getRowNum())
                .thenReturn(2);
        when(rowMock.spliterator())
                .thenReturn(List.of(this.cellMock).spliterator());
        when(xssfSheetMock.spliterator())
                .thenReturn(List.of(rowMock).spliterator());

        // When
        final List<SheetDTO> actualSheets = assertDoesNotThrow(() ->
                SheetImportManager.obtainSheets(xssfSheetMock, objectMapperMock, givenSheetModel));

        // Then
        assertThat(actualSheets).isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    void given_wrong_columnIndex_should_throws_excelImportException_when_obtainSheets() {
        // Given
        final Row rowMock = mock(Row.class);
        final XSSFSheet xssfSheetMock = mock(XSSFSheet.class);
        final ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        final Class<? extends SheetDTO> givenSheetModel = SheetTestDTO.class;

        when(this.cellMock.getCellType())
                .thenReturn(CellType.STRING);
        when(this.cellMock.getColumnIndex())
                .thenReturn(3);
        when(rowMock.getRowNum())
                .thenReturn(2);
        when(rowMock.spliterator())
                .thenReturn(List.of(this.cellMock).spliterator());
        when(xssfSheetMock.spliterator())
                .thenReturn(List.of(rowMock).spliterator());

        // Then
        assertThrows(ExcelImportException.class, () ->
                SheetImportManager.obtainSheets(xssfSheetMock, objectMapperMock, givenSheetModel));
    }

    @Test
    void should_return_sheetDTOs_when_obtainSheets_with_localDate() {
        // Given
        final Cell dateCellMock = mock(Cell.class);
        final LocalDateTime dateTimeWithoutTime = LocalDateTime.of(2025, 11, 7, 0, 0, 0);

        when(dateCellMock.getCellType())
                .thenReturn(CellType.NUMERIC);
        when(dateCellMock.getLocalDateTimeCellValue())
                .thenReturn(dateTimeWithoutTime);

        try (final var dateUtilMock = mockStatic(DateUtil.class)) {
            dateUtilMock.when(() -> DateUtil.isCellDateFormatted(dateCellMock))
                    .thenReturn(true);

            // When
            final Row rowMock = mock(Row.class);
            final XSSFSheet xssfSheetMock = mock(XSSFSheet.class);
            final ObjectMapper objectMapperMock = mock(ObjectMapper.class);
            final Class<? extends SheetDTO> givenSheetModel = SheetTestDTO.class;

            when(dateCellMock.getColumnIndex())
                    .thenReturn(0);
            when(rowMock.getRowNum())
                    .thenReturn(2);
            when(rowMock.spliterator())
                    .thenReturn(List.of(dateCellMock).spliterator());
            when(xssfSheetMock.spliterator())
                    .thenReturn(List.of(rowMock).spliterator());

            // When
            final List<SheetDTO> actualSheets = assertDoesNotThrow(() ->
                    SheetImportManager.obtainSheets(xssfSheetMock, objectMapperMock, givenSheetModel));

            // Then
            assertThat(actualSheets)
                    .isNotNull()
                    .isNotEmpty()
                    .hasSize(1);
        }
    }

    @Test
    void should_return_sheetDTOs_when_obtainSheets_with_localDateTime() {
        // Given
        final Cell dateCellMock = mock(Cell.class);
        final LocalDateTime dateTimeWithTime = LocalDateTime.of(2025, 11, 7, 10, 30, 15);

        when(dateCellMock.getCellType())
                .thenReturn(CellType.NUMERIC);
        when(dateCellMock.getLocalDateTimeCellValue())
                .thenReturn(dateTimeWithTime);

        try (final var dateUtilMock = mockStatic(DateUtil.class)) {
            dateUtilMock.when(() -> DateUtil.isCellDateFormatted(dateCellMock))
                    .thenReturn(true);

            // When
            final Row rowMock = mock(Row.class);
            final XSSFSheet xssfSheetMock = mock(XSSFSheet.class);
            final ObjectMapper objectMapperMock = mock(ObjectMapper.class);
            final Class<? extends SheetDTO> givenSheetModel = SheetTestDTO.class;

            when(dateCellMock.getColumnIndex())
                    .thenReturn(0);
            when(rowMock.getRowNum())
                    .thenReturn(2);
            when(rowMock.spliterator())
                    .thenReturn(List.of(dateCellMock).spliterator());
            when(xssfSheetMock.spliterator())
                    .thenReturn(List.of(rowMock).spliterator());

            // When
            final List<SheetDTO> actualSheets = assertDoesNotThrow(() ->
                    SheetImportManager.obtainSheets(xssfSheetMock, objectMapperMock, givenSheetModel));

            // Then
            assertThat(actualSheets)
                    .isNotNull()
                    .isNotEmpty()
                    .hasSize(1);
        }
    }

    @Test
    void should_return_sheetDTOs_when_obtainSheets_with_numericCell_isNotDateFormatted() {
        // Given
        final Cell numericCellMock = mock(Cell.class);

        when(numericCellMock.getCellType())
                .thenReturn(CellType.NUMERIC);
        when(numericCellMock.getNumericCellValue())
                .thenReturn(123.45);

        try (final var dateUtilMock = mockStatic(DateUtil.class)) {
            dateUtilMock.when(() -> DateUtil.isCellDateFormatted(numericCellMock))
                    .thenReturn(false);

            // When
            final Row rowMock = mock(Row.class);
            final XSSFSheet xssfSheetMock = mock(XSSFSheet.class);
            final ObjectMapper objectMapperMock = mock(ObjectMapper.class);
            final Class<? extends SheetDTO> givenSheetModel = SheetTestDTO.class;

            when(numericCellMock.getColumnIndex())
                    .thenReturn(0);
            when(rowMock.getRowNum())
                    .thenReturn(2);
            when(rowMock.spliterator())
                    .thenReturn(List.of(numericCellMock).spliterator());
            when(xssfSheetMock.spliterator())
                    .thenReturn(List.of(rowMock).spliterator());

            // When
            final List<SheetDTO> actualSheets = assertDoesNotThrow(() ->
                    SheetImportManager.obtainSheets(xssfSheetMock, objectMapperMock, givenSheetModel));

            // Then
            assertThat(actualSheets)
                    .isNotNull()
                    .isNotEmpty()
                    .hasSize(1);
        }
    }


    private void cellMock() {
        when(this.cellMock.getNumericCellValue())
                .thenReturn((double) 0);
        when(this.cellMock.getBooleanCellValue())
                .thenReturn(Boolean.TRUE);
        when(this.cellMock.getStringCellValue())
                .thenReturn("A");
        when(this.cellMock.getCellFormula())
                .thenReturn("SUM()");
        when(this.cellMock.getErrorCellValue())
                .thenReturn((byte) 1);
    }
}