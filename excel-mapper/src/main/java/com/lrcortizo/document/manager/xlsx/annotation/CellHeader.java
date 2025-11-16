package com.lrcortizo.document.manager.xlsx.annotation;

import com.lrcortizo.document.manager.xlsx.model.catalog.CellStyleCatalog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CellHeader {

    String[] labels();

    boolean filterScroll() default false;

    int headerRow() default 0;

    CellStyleCatalog style() default CellStyleCatalog.HEADER;
}
