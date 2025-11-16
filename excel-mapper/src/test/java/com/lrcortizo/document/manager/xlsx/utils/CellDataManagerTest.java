package com.lrcortizo.document.manager.xlsx.utils;

import com.lrcortizo.document.manager.xlsx.annotation.CellData;
import com.lrcortizo.document.manager.xlsx.annotation.CellHeader;
import com.lrcortizo.document.manager.xlsx.model.SheetDTO;
import com.lrcortizo.document.manager.xlsx.model.catalog.CellFormatCatalog;
import com.lrcortizo.document.manager.xlsx.test.model.ParentSheetTestDTO;
import com.lrcortizo.document.manager.xlsx.test.model.SheetTestDTO;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CellDataManagerTest {

    @Test
    void should_return_cellHeader_when_obtainCellHeader() {
        // Given
        final Class<? extends SheetDTO> givenSheetClass = SheetTestDTO.class;

        // When
        final CellHeader actualCellHeader = CellDataManager.obtainCellHeader(givenSheetClass);

        // Then
        assertNotNull(actualCellHeader);
    }


    @Test
    void should_return_cellData_list_when_obtainCellData() {
        // Given
        final Class<? extends SheetDTO> givenSheetClass = SheetTestDTO.class;

        // When
        final List<CellData> actualCellData = CellDataManager.obtainCellData(givenSheetClass);

        // Then
        assertThat(actualCellData).isNotNull()
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    void should_call_buildCell_createCell_when_addDataCell() {
        // Given
        final String givenValue = "A";
        final XSSFRow xssfRowMock = mock(XSSFRow.class);
        final XSSFCell xssfCellMock = mock(XSSFCell.class);
        final CellData cellDataMock = mock(CellData.class);
        final int givenCellColumn = 0;
        when(cellDataMock.type()).thenReturn(CellFormatCatalog.STRING);
        when(cellDataMock.column()).thenReturn(givenCellColumn);
        when(xssfRowMock.createCell(givenCellColumn)).thenReturn(xssfCellMock);

        // When
        assertDoesNotThrow(
                () -> CellDataManager.addDataCell(xssfRowMock, cellDataMock, null, givenValue));

        // Then
        verify(xssfRowMock, times(1)).createCell(givenCellColumn);
    }

    @Test
    void should_call_buildCell_createCell_when_addHeaderDataCell() {
        // Given
        final String givenValue = "A";
        final XSSFRow xssfRowMock = mock(XSSFRow.class);
        final XSSFCell xssfCellMock = mock(XSSFCell.class);
        final int givenCellColumn = 0;
        when(xssfRowMock.createCell(givenCellColumn)).thenReturn(xssfCellMock);

        // When
        assertDoesNotThrow(
                () -> CellDataManager.addHeaderDataCell(xssfRowMock, null, givenValue, givenCellColumn));

        // Then
        verify(xssfRowMock, times(1))
                .createCell(givenCellColumn);
    }

    @Test
    void should_collect_cellData_from_nested_sheetDTO_field_when_obtainCellData() {
        // Given
        final Class<? extends SheetDTO> givenNestedSheetClass = ParentSheetTestDTO.class;

        // When
        final List<CellData> actualCellData = CellDataManager.obtainCellData(givenNestedSheetClass);

        // Then
        assertThat(actualCellData).isNotNull()
                .hasSizeGreaterThan(1) // Al menos 1 del padre + los del hijo
                .allMatch(cellData -> cellData.column() >= 0);
    }
}