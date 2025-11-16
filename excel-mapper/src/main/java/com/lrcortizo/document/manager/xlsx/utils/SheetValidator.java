package com.lrcortizo.document.manager.xlsx.utils;

import com.lrcortizo.document.manager.xlsx.exception.ExcelImportException;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import static com.lrcortizo.document.manager.xlsx.model.catalog.SheetConstants.OPEN_XML;

@UtilityClass
public class SheetValidator {

    public void validateExcelFile(final MultipartFile file) {
        if(!hasValidExcelFormat(file)) {
            throw new ExcelImportException(String.format("MultipartFile content type is not: %s ", OPEN_XML));
        }
    }

    private boolean hasValidExcelFormat(final MultipartFile file) {
        return OPEN_XML.equals(file.getContentType());
    }
}
