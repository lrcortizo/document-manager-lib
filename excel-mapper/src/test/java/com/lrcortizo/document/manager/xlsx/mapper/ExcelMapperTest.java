package com.lrcortizo.document.manager.xlsx.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lrcortizo.document.manager.xlsx.exception.ExcelExportException;
import com.lrcortizo.document.manager.xlsx.exception.ExcelImportException;
import com.lrcortizo.document.manager.xlsx.mapper.sheet.SheetExportManager;
import com.lrcortizo.document.manager.xlsx.model.SheetDTO;
import com.lrcortizo.document.manager.xlsx.model.catalog.SheetConstants;
import com.lrcortizo.document.manager.xlsx.test.model.SheetTestDTO;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class ExcelMapperTest {

    @Test
    void should_return_xSSFWorkbook_when_generateWorkbook() {
        // Given
        final List<? extends SheetDTO> givenSheets = List.of(SheetTestDTO.builder().build());
        final String givenSheetName = "sheetName";

        // When
        final XSSFWorkbook actualXssfWorkbook = ExcelMapper.generateWorkbook(givenSheets, givenSheetName);

        // Then
        assertNotNull(actualXssfWorkbook);
        assertDoesNotThrow(actualXssfWorkbook::close);
    }

    @Test
    void should_throw_ExcelExportException_when_runtime_exception_occurs() {
        // Given
        final List<SheetTestDTO> sheets = List.of(
                SheetTestDTO.builder()
                        .testColumnA("A")
                        .testColumnB("B")
                        .build()
        );
        final String sheetName = "TestSheet";
        final String errorMessage = "Error creating header row";

        // When & Then
        try (final MockedStatic<SheetExportManager> mockedStatic = mockStatic(SheetExportManager.class)) {
            mockedStatic.when(() -> SheetExportManager.createHeaderRow(any(XSSFWorkbook.class),
                            any(XSSFSheet.class), anyList()))
                    .thenThrow(new RuntimeException(errorMessage));

            assertThatThrownBy(() -> ExcelMapper.generateWorkbook(sheets, sheetName))
                    .isInstanceOf(ExcelExportException.class)
                    .hasMessage(errorMessage);
        }
    }

    @Test
    void should_return_sheetDTOs_when_obtainSheets() {
        try (final XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new File(
                Objects.requireNonNull(this.getClass().getClassLoader()
                        .getResource("mock/TestSheet.xlsx")).getFile()))) {

            // Given
            final ObjectMapper objectMapperMock = mock(ObjectMapper.class);
            final Class<? extends SheetDTO> sheetModel = SheetTestDTO.class;
            final SheetTestDTO expectedSheet = SheetTestDTO.builder().build();

            when(objectMapperMock.convertValue(any(), eq(SheetTestDTO.class)))
                    .thenReturn(expectedSheet);

            // When
            final List<SheetDTO> actualSheets = assertDoesNotThrow(
                    () -> ExcelMapper.obtainSheets(xssfWorkbook, objectMapperMock, sheetModel));

            // Then
            assertNotNull(actualSheets);
            assertSame(actualSheets.getFirst(), expectedSheet);

        } catch (final Exception ex) {
            fail("Missing Test resource /resources/mock/TestSheet.xlsx", ex);
        }
    }

    @Test
    void should_throws_excelImportException_when_obtainSheets() {

        try (final XSSFWorkbook xssfWorkbook = new XSSFWorkbook()) {

            // Given
            final ObjectMapper objectMapperMock = mock(ObjectMapper.class);
            final Class<? extends SheetDTO> sheetModel = SheetTestDTO.class;

            // Then
            assertThrows(ExcelImportException.class,
                    () -> ExcelMapper.obtainSheets(xssfWorkbook, objectMapperMock, sheetModel));


        } catch (final IOException ioe) {
            fail(ioe);
        }
    }

    @Test
    void should_return_xSSFWorkbook_when_obtainValidWorkbook() {
        // Given
        final MultipartFile excelFileMock = mock(MultipartFile.class);
        when(excelFileMock.getContentType()).thenReturn(SheetConstants.OPEN_XML);

        try (final InputStream resourceAsStream = this.getClass().getClassLoader()
                .getResourceAsStream("mock/TestSheet.xlsx")) {

            when(excelFileMock.getInputStream())
                    .thenReturn(resourceAsStream);

            // When
            final XSSFWorkbook actualXSSFWorkbook = assertDoesNotThrow(
                    () -> ExcelMapper.obtainValidWorkbook(excelFileMock));

            // Then
            assertNotNull(actualXSSFWorkbook);
            assertDoesNotThrow(actualXSSFWorkbook::close);

        } catch (final IOException ioe) {
            fail("Missing Test resource /resources/mock/TestSheet.xlsx", ioe);
        }
    }

    @Test
    void should_throws_excelImportException_when_obtainValidWorkbook() {
        // Given
        final MultipartFile excelFileMock = mock(MultipartFile.class);
        when(excelFileMock.getContentType()).thenReturn(SheetConstants.OPEN_XML);

        try {
            when(excelFileMock.getInputStream())
                    .thenThrow(IOException.class);

            // Then
            assertThrows(ExcelImportException.class,
                    () -> ExcelMapper.obtainValidWorkbook(excelFileMock));

        } catch (final IOException ioe) {
            fail(ioe);
        }
    }
}