package com.lrcortizo.document.manager.xlsx.utils;

import com.lrcortizo.document.manager.xlsx.model.catalog.CellBackgroundColorCatalog;
import com.lrcortizo.document.manager.xlsx.model.catalog.CellFontStyleCatalog;
import com.lrcortizo.document.manager.xlsx.model.catalog.CellFormatCatalog;
import com.lrcortizo.document.manager.xlsx.model.catalog.CellStyleCatalog;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@UtilityClass
public class CellStyleManager {

    public XSSFCellStyle obtainDataCellStyle(final XSSFWorkbook workbook, final CellStyleCatalog cellStyleCatalog,
                                             final CellFormatCatalog cellFormat) {
        return applyCellStyle(workbook, cellFormat, cellStyleCatalog.getBackgroundColor(), cellStyleCatalog.getFontStyle(),
                cellStyleCatalog.getCellAlignment(), cellStyleCatalog.isLocked());
    }

    private XSSFCellStyle applyCellStyle(final XSSFWorkbook workbook, final CellFormatCatalog cellFormat,
                                         final CellBackgroundColorCatalog backgroundColor,
                                         final CellFontStyleCatalog fontStyle, final HorizontalAlignment cellAlignment,
                                         final boolean isLocked) {
        return setXSSFCellStyle(workbook, cellFormat, backgroundColor.getColor(), cellAlignment,
                setCellFont(workbook, fontStyle), isLocked);
    }

    private XSSFCellStyle setXSSFCellStyle(final XSSFWorkbook workbook, final CellFormatCatalog cellFormat,
                                           final XSSFColor color, final HorizontalAlignment alignment,
                                           final Font font, final boolean isLocked) {
        final XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(color);
        cellStyle.setAlignment(alignment);
        cellStyle.setFont(font);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setWrapText(true);
        cellStyle.setLocked(isLocked);
        setHairBorderStyle(cellStyle);
        setDataFormat(workbook, cellFormat, cellStyle);

        return cellStyle;
    }

    private Font setCellFont(final Workbook workbook, final CellFontStyleCatalog fontStyle) {
        final Font font = workbook.createFont();
        font.setFontHeightInPoints(fontStyle.getFontSize());
        font.setBold(fontStyle.isBold());
        font.setFontName(fontStyle.getFontName());
        font.setColor(fontStyle.getColorIndex());
        return font;
    }

    private void setHairBorderStyle(final XSSFCellStyle cellStyle) {
        cellStyle.setBorderBottom(BorderStyle.HAIR);
        cellStyle.setBorderTop(BorderStyle.HAIR);
        cellStyle.setBorderRight(BorderStyle.HAIR);
        cellStyle.setBorderLeft(BorderStyle.HAIR);
    }

    private void setDataFormat(final Workbook workbook, final CellFormatCatalog cellFormat, final XSSFCellStyle cellStyle) {
        switch (cellFormat) {
            case null -> cellStyle.setDataFormat(CellFormatCatalog.GENERAL.getFmtCode());
            case GENERAL, BLANK, BOOLEAN, FORMULA -> cellStyle.setDataFormat(CellFormatCatalog.GENERAL.getFmtCode());
            case STRING -> cellStyle.setDataFormat(CellFormatCatalog.STRING.getFmtCode());
            case NUMBER -> cellStyle.setDataFormat(CellFormatCatalog.NUMBER.getFmtCode());
            default -> cellStyle.setDataFormat(registerDataFormat(workbook, cellFormat.getFormat()));
        }
    }

    private short registerDataFormat(final Workbook workbook, final String format) {
        final CreationHelper createHelper = workbook.getCreationHelper();
        return createHelper.createDataFormat().getFormat(format);
    }
}