package com.lrcortizo.document.manager.xlsx.utils;

import com.lrcortizo.document.manager.xlsx.exception.ExcelImportException;
import com.lrcortizo.document.manager.xlsx.model.catalog.SheetConstants;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SheetValidatorTest {

    @Test
    void should_validate_when_validateExcelFile() {
        // Given
        final MultipartFile excelFileMock = mock(MultipartFile.class);
        when(excelFileMock.getContentType()).thenReturn(SheetConstants.OPEN_XML);

        // Then
        assertDoesNotThrow(() -> SheetValidator.validateExcelFile(excelFileMock));
    }

    @Test
    void should_throws_excelFileImportException_when_validateExcelFile() {
        // Given
        final MultipartFile excelFileMock = mock(MultipartFile.class);
        when(excelFileMock.getContentType()).thenReturn("text/html");

        // Then
        assertThrows(ExcelImportException.class, () -> SheetValidator.validateExcelFile(excelFileMock));
    }
}