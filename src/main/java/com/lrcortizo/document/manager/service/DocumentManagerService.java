package com.lrcortizo.document.manager.service;

import com.lrcortizo.document.manager.exception.DocumentManagerException;
import com.lrcortizo.document.manager.model.catalog.DocumentType;
import com.lrcortizo.document.manager.service.processor.DocumentProcessorResolver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class DocumentManagerService {

    private static final int MB_SIZE = 1024 * 1024;

    private static final int DEFAULT_MAX_DOCUMENT_SIZE = 1;

    private static final int DEFAULT_MAX_IMAGE_SIZE = 1200;

    private final DocumentProcessorResolver documentProcessorResolver;

    public byte[] reduceDocumentSize(final byte[] document, final DocumentType documentType) throws DocumentManagerException {
        return this.reduceDocumentSize(document, documentType, DEFAULT_MAX_DOCUMENT_SIZE, DEFAULT_MAX_IMAGE_SIZE);
    }

    public byte[] reduceDocumentSize(final byte[] document, final DocumentType documentType,
                                     final int maxDocumentMbSize, final int maxImagePixelsSize) throws DocumentManagerException {
        try {
            return this.documentProcessorResolver
                    .resolve(documentType.getProcessorServiceClass())
                    .reduceSize(document, MB_SIZE * maxDocumentMbSize, maxImagePixelsSize);
        } catch (final IllegalArgumentException e) {
            throw new DocumentManagerException(String.format("Document processor service not found for type %s",
                    documentType.name()));
        }
    }
}
