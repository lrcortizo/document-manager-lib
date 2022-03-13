package com.lrcortizo.document.manager.service.processor;

public interface DocumentProcessorService {

    byte[] reduceSize(final byte[] document, final int maxDocumentSize, final int maxImageSize);
}
