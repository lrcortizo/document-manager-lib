package com.lrcortizo.document.manager.xlsx.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public class CellBuilder {

    private XSSFRow row;
    private XSSFCell cell;
    private CellType type;
    private XSSFCellStyle style;

    public CellBuilder(final XSSFRow row, final CellType type, final XSSFCellStyle style, final int index,
                       final Object value) {
        this.row = row;
        this.setCell(type, style, index, value);
    }

    private void setCell(final CellType type, final XSSFCellStyle style, final int index, final Object value) {
        this.cell = this.row.createCell(index);
        this.setCellType(type);
        this.setCellValue(value);
        this.setCellStyle(style);
    }

    private void setCellType(final CellType type) {
        this.type = type;

        if (CellType.FORMULA != type) {
            this.cell.setCellType(this.type);
        }
    }

    private void setCellValue(final Object value) {
        switch (this.type) {
            case FORMULA -> this.cell.setCellFormula((String) value);
            case BLANK -> this.cell.setBlank();
            case null, default -> this.setDefaultCellValue(value);
        }
    }

    private void setDefaultCellValue(final Object value) {
        switch (value) {
            case final String s -> this.cell.setCellValue(s);
            case final Boolean b -> this.cell.setCellValue(b);
            case final LocalDate ld -> this.cell.setCellValue(ld);
            case final LocalDateTime ldt -> this.cell.setCellValue(ldt);
            case final Double d -> this.cell.setCellValue(d);
            case final Integer i -> this.cell.setCellValue(i);
            case null, default -> this.cell.setCellValue(Strings.EMPTY);
        }
    }

    private void setCellStyle(final XSSFCellStyle style) {
        if (style != null) {
            this.cell.setCellStyle(style);
        }
    }
}