package com.lrcortizo.document.manager.service;

import com.lrcortizo.document.manager.model.catalog.DocumentType;
import com.lrcortizo.document.manager.service.processor.DocumentProcessorResolver;
import com.lrcortizo.document.manager.service.processor.DocumentProcessorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DocumentManagerService.class)
@MockBean(DocumentProcessorResolver.class)
class DocumentManagerServiceTest {

    private static final String DOCUMENT = "Test document";

    private final DocumentManagerService documentManagerService;
    private final DocumentProcessorResolver documentProcessorResolver;

    @Autowired
    public DocumentManagerServiceTest(final DocumentManagerService documentManagerService,
                                      final DocumentProcessorResolver documentProcessorResolver) {
        this.documentManagerService = documentManagerService;
        this.documentProcessorResolver = documentProcessorResolver;
    }

    @Test
    void given_document_when_reduceDocumentSize_then_returnDocument() {
        // Given
        final byte[] documentBytes = DOCUMENT.getBytes();
        final DocumentProcessorService documentProcessorService = mock(DocumentProcessorService.class);
        when(this.documentProcessorResolver.resolve(DocumentType.PDF.getProcessorServiceClass()))
                .thenReturn(documentProcessorService);
        when(documentProcessorService.reduceSize(eq(documentBytes), anyInt(), anyInt()))
                .thenReturn(documentBytes);

        // When
        final byte[] result = this.documentManagerService.reduceDocumentSize(documentBytes, DocumentType.PDF);

        // Then
        assertThat(result)
                .isNotEmpty()
                .isEqualTo(documentBytes);
    }
}
