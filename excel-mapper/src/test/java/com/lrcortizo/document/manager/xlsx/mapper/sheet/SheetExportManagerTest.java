package com.lrcortizo.document.manager.xlsx.mapper.sheet;

import com.lrcortizo.document.manager.xlsx.model.SheetDTO;
import com.lrcortizo.document.manager.xlsx.test.model.SheetBooleanTestDTO;
import com.lrcortizo.document.manager.xlsx.test.model.SheetTestDTO;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.helpers.ColumnHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SheetExportManagerTest {

    @Test
    void _should_when() {
        try (final XSSFWorkbook givenWorkbook = new XSSFWorkbook()) {

            // Given
            final XSSFSheet sheetMock = mock(XSSFSheet.class);
            final XSSFRow rowMock = mock(XSSFRow.class);
            final XSSFCell xssfCellMock = mock(XSSFCell.class);
            final SheetTestDTO givenSheet = SheetTestDTO.builder()
                    .testColumnA("A")
                    .testColumnB("B")
                    .build();
            final List<? extends SheetDTO> givenDataSheets = List.of(givenSheet);
            when(sheetMock.createRow(0))
                    .thenReturn(rowMock);
            when(rowMock.createCell(0))
                    .thenReturn(xssfCellMock);

            // Then
            assertDoesNotThrow(
                    () -> SheetExportManager.createHeaderRow(givenWorkbook, sheetMock, givenDataSheets));

        } catch (final IOException ioe) {
            fail(ioe);
        }
    }

    @Test
    void _should_when_() {
        try (final XSSFWorkbook givenWorkbook = new XSSFWorkbook()) {

            // Given
            final XSSFSheet sheetMock = mock(XSSFSheet.class);
            final XSSFRow rowMock = mock(XSSFRow.class);
            final XSSFCell xssfCellMock = mock(XSSFCell.class);
            final ColumnHelper columnHelperMock = mock(ColumnHelper.class);
            final SheetTestDTO givenSheet = SheetTestDTO.builder()
                    .testColumnA("A")
                    .testColumnB("B")
                    .build();
            final List<? extends SheetDTO> givenDataSheets = List.of(givenSheet);
            when(sheetMock.createRow(1))
                    .thenReturn(rowMock);
            when(sheetMock.getColumnHelper())
                    .thenReturn(columnHelperMock);
            when(rowMock.createCell(0))
                    .thenReturn(xssfCellMock);
            when(rowMock.createCell(1))
                    .thenReturn(xssfCellMock);

            // Then
            assertDoesNotThrow(
                    () -> SheetExportManager.createDataRows(givenWorkbook, sheetMock, givenDataSheets));

        } catch (final IOException ioe) {
            fail(ioe);
        }
    }

    @Test
    void _should_when__() {
        try (final XSSFWorkbook givenWorkbook = new XSSFWorkbook()) {

            // Given
            final XSSFSheet sheetMock = mock(XSSFSheet.class);
            final XSSFRow rowMock = mock(XSSFRow.class);
            final XSSFCell xssfCellMock = mock(XSSFCell.class);
            final ColumnHelper columnHelperMock = mock(ColumnHelper.class);
            final DataValidationHelper dvHelperMock = mock(DataValidationHelper.class);
            final XSSFDataValidation xssfDataValidationMock = mock(XSSFDataValidation.class);
            final SheetBooleanTestDTO givenSheet = SheetBooleanTestDTO.builder()
                    .testColumnA("A")
                    .testColumnB(Boolean.TRUE)
                    .build();
            final List<? extends SheetDTO> givenDataSheets = List.of(givenSheet);
            when(sheetMock.createRow(1))
                    .thenReturn(rowMock);
            when(sheetMock.getColumnHelper())
                    .thenReturn(columnHelperMock);
            when(dvHelperMock.createValidation(any(), any()))
                    .thenReturn(xssfDataValidationMock);
            when(sheetMock.getDataValidationHelper())
                    .thenReturn(dvHelperMock);
            when(rowMock.createCell(0))
                    .thenReturn(xssfCellMock);
            when(rowMock.createCell(1))
                    .thenReturn(xssfCellMock);

            // Then
            assertDoesNotThrow(
                    () -> SheetExportManager.createDataRows(givenWorkbook, sheetMock, givenDataSheets));

        } catch (final IOException ioe) {
            fail(ioe);
        }
    }
}