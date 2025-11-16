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
@CellHeader(labels = {"Parent Field"})
public class ParentSheetTestDTO implements SheetDTO {

    @CellData(name = "parentField", column = 0, type = CellFormatCatalog.STRING)
    private String parentField;

    private SheetTestDTO nestedSheet;
}
