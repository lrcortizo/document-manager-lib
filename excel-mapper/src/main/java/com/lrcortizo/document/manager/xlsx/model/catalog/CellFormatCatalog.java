package com.lrcortizo.document.manager.xlsx.model.catalog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.CellType;

@Getter
@AllArgsConstructor
public enum CellFormatCatalog {

    BLANK(0, null, CellType.BLANK),
    BOOLEAN(null, "BOOLEAN", CellType.BOOLEAN),
    DATE(null, "dd/mm/yyyy", CellType.STRING),
    DATE_SHORT(15, "d-mmm-yy", CellType.STRING),
    DATE_TIME(null, "dd/mm/yyyy hh:mm", CellType.STRING),
    FORMULA(0, "General", CellType.FORMULA),
    CURRENCY_EUR(7, "\"$\"#,##0_);(\"$\"#,##0)", CellType.NUMERIC),
    GENERAL(0, "General", CellType.STRING),
    H_MM_SS(21, "h:mm:ss", CellType.STRING),
    NUMBER(1, "0", CellType.NUMERIC),
    NUMBER_DP(2, "0.00", CellType.NUMERIC),
    STRING(49, "@", CellType.STRING);

    private final Integer fmtCode;
    private final String format;
    private final CellType type;
}