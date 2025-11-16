package com.lrcortizo.document.manager.xlsx.test.model;

import com.lrcortizo.document.manager.xlsx.annotation.CellData;
import com.lrcortizo.document.manager.xlsx.annotation.CellHeader;
import com.lrcortizo.document.manager.xlsx.model.SheetDTO;
import com.lrcortizo.document.manager.xlsx.model.catalog.CellFormatCatalog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@AllArgsConstructor
@Getter
@CellHeader(labels = {"Test1, Test2"})
public class SheetTestDTO implements SheetDTO {

    @CellData(name = "testColumnA", column = 0, type = CellFormatCatalog.STRING)
    String testColumnA;

    @CellData(name = "testColumnB", column = 1, type = CellFormatCatalog.STRING)
    String testColumnB;
}
