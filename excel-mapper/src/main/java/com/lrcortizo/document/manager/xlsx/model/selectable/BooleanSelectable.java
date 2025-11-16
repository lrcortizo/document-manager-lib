package com.lrcortizo.document.manager.xlsx.model.selectable;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BooleanSelectable {

    TRUE("=TRUE()"),
    FALSE("=FALSE()"),
    VERDADERO("=VERDADERO()"),
    FALSO("=FALSO()");

    final String excelValue;

    @Override
    public String toString() {
        return this.excelValue;
    }
}
