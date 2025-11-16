package com.lrcortizo.document.manager.xlsx.model.catalog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.hssf.util.HSSFColor;

@Getter
@AllArgsConstructor
public enum CellFontStyleCatalog {

    CALIBRI_WHITE_BOLD("Calibri", (short) 14, true, HSSFColor.HSSFColorPredefined.WHITE.getIndex()),
    CALIBRI_AUTO("Calibri", (short) 11, false, HSSFColor.HSSFColorPredefined.AUTOMATIC.getIndex());

    private final String fontName;
    private final short fontSize;
    private final boolean bold;
    private final short colorIndex;
}
