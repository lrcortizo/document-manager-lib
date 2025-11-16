package com.lrcortizo.document.manager.toolkit.model.catalog;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum DocumentType {
    PDF("PdfProcessorService");

    private final String processorServiceClass;
}