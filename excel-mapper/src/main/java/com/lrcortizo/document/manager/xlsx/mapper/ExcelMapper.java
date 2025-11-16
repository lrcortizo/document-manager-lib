package com.lrcortizo.document.manager.xlsx.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lrcortizo.document.manager.xlsx.exception.ExcelExportException;
import com.lrcortizo.document.manager.xlsx.exception.ExcelImportException;
import com.lrcortizo.document.manager.xlsx.mapper.sheet.SheetExportManager;
import com.lrcortizo.document.manager.xlsx.mapper.sheet.SheetImportManager;
import com.lrcortizo.document.manager.xlsx.model.SheetDTO;
import com.lrcortizo.document.manager.xlsx.model.catalog.SheetConstants;
import com.lrcortizo.document.manager.xlsx.utils.SheetValidator;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@UtilityClass
@Slf4j
public class ExcelMapper {

    public XSSFWorkbook generateWorkbook(final List<? extends SheetDTO> sheets, final String sheetName) {

        try {
            final XSSFWorkbook workbook = new XSSFWorkbook();
            final XSSFSheet spreadsheet = workbook.createSheet(sheetName);
            SheetExportManager.createHeaderRow(workbook, spreadsheet, sheets);
            SheetExportManager.createDataRows(workbook, spreadsheet, sheets);

            return workbook;

        } catch (final RuntimeException re) {
            log.debug(String.format("generateWorkbook exception: %s", re.getMessage()));
            throw new ExcelExportException(re.getMessage());
        }
    }

    public XSSFWorkbook obtainValidWorkbook(final MultipartFile excelFile) {
        SheetValidator.validateExcelFile(excelFile);
        return createWorkbook(excelFile);
    }

    public List<SheetDTO> obtainSheets(final XSSFWorkbook xssfWorkbook, final ObjectMapper objectMapper,
                                       final Class<? extends SheetDTO> sheetModel) {
        try {
            final XSSFSheet xssfSheet = getSheet(xssfWorkbook);
            final List<SheetDTO> sheets = SheetImportManager.obtainSheets(xssfSheet, objectMapper, sheetModel);
            xssfWorkbook.close();

            return sheets;
        } catch (final IllegalArgumentException | IOException ioe) {
            log.debug(String.format("obtainSheets exception: %s", ioe.getMessage()));
            throw new ExcelImportException(ioe.getMessage());
        }
    }

    private XSSFWorkbook createWorkbook(final MultipartFile excel) {
        try {
            ZipSecureFile.setMinInflateRatio(SheetConstants.COMPRESS_RATIO);
            return new XSSFWorkbook(excel.getInputStream());
        } catch (final IOException ioe) {
            log.debug(String.format("createWorkbook exception exception: %s", ioe.getMessage()));
            throw new ExcelImportException(ioe.getMessage());
        }
    }

    private XSSFSheet getSheet(final XSSFWorkbook xssfWorkbook) {
        return xssfWorkbook.getSheet(xssfWorkbook.getSheetName(0));
    }
}
