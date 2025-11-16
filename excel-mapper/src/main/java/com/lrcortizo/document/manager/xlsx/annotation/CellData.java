package com.lrcortizo.document.manager.xlsx.annotation;

import com.lrcortizo.document.manager.xlsx.model.catalog.CellFormatCatalog;
import com.lrcortizo.document.manager.xlsx.model.catalog.CellStyleCatalog;
import com.lrcortizo.document.manager.xlsx.model.selectable.VoidSelectable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CellData {

    int column();

    String name();

    CellFormatCatalog type() default CellFormatCatalog.GENERAL;

    CellStyleCatalog style() default CellStyleCatalog.STANDARD;

    Class<? extends Enum> selectable() default VoidSelectable.class;
}
