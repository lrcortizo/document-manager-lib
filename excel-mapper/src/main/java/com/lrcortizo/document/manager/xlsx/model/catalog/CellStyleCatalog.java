package com.lrcortizo.document.manager.xlsx.model.catalog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

@Getter
@AllArgsConstructor
public enum CellStyleCatalog {

    HEADER(CellBackgroundColorCatalog.YELLOW_BG, CellFontStyleCatalog.CALIBRI_WHITE_BOLD, HorizontalAlignment.LEFT, false),
    STANDARD(CellBackgroundColorCatalog.WHITE_BG, CellFontStyleCatalog.CALIBRI_AUTO, HorizontalAlignment.LEFT, false),
    LOCKED(CellBackgroundColorCatalog.GREY_BG, CellFontStyleCatalog.CALIBRI_AUTO, HorizontalAlignment.LEFT, true),
    LIGHT_BLUE(CellBackgroundColorCatalog.LIGHT_BLUE_BG, CellFontStyleCatalog.CALIBRI_AUTO, HorizontalAlignment.LEFT, false),
    LIGHT_GREEN(CellBackgroundColorCatalog.LIGHT_GREEN_BG, CellFontStyleCatalog.CALIBRI_AUTO, HorizontalAlignment.LEFT, false);

    private final CellBackgroundColorCatalog backgroundColor;
    private final CellFontStyleCatalog fontStyle;
    private final HorizontalAlignment cellAlignment;
    private final boolean isLocked;
}
