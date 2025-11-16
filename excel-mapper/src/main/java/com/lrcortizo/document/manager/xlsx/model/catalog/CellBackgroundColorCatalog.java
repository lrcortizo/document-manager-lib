package com.lrcortizo.document.manager.xlsx.model.catalog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.xssf.usermodel.XSSFColor;

@Getter
@AllArgsConstructor
public enum CellBackgroundColorCatalog {

    WHITE_BG(new XSSFColor(new java.awt.Color(255, 255, 255), null)),
    YELLOW_BG(new XSSFColor(new java.awt.Color(196, 214, 0), null)),
    LIGHT_BLUE_BG(new XSSFColor(new java.awt.Color(242, 252, 255), null)),
    LIGHT_YELLOW_BG(new XSSFColor(new java.awt.Color(255, 248, 222), null)),
    LIGHT_GREEN_BG(new XSSFColor(new java.awt.Color(241, 255, 238), null)),
    GREY_BG(new XSSFColor(new java.awt.Color(211, 211, 211), null));

    private final XSSFColor color;
}
