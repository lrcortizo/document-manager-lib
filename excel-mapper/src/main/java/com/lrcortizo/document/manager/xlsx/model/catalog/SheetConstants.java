package com.lrcortizo.document.manager.xlsx.model.catalog;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SheetConstants {

    public static final String OPEN_XML = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static final short DATA_START_ROW = 1;

    public static final double COMPRESS_RATIO = 0.006;
}
