package com.lrcortizo.document.manager.service.processor;

import com.lrcortizo.document.manager.exception.DocumentManagerException;
import com.lrcortizo.document.manager.model.catalog.DocumentType;
import com.lrcortizo.document.manager.service.processor.pdf.PdfProcessorService;
import com.lrcortizo.document.manager.service.processor.pdf.PdfReductionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                DocumentProcessorResolver.class,
                PdfProcessorService.class,
                PdfReductionService.class
        })
class DocumentProcessorResolverTest {

    private final DocumentProcessorResolver documentProcessorResolver;

    @Autowired
    public DocumentProcessorResolverTest(final DocumentProcessorResolver documentProcessorResolver) {
        this.documentProcessorResolver = documentProcessorResolver;
    }

    @Test
    void should_return_instance_for_all_algorithms() {
        Arrays.stream(DocumentType.values())
                .forEach(processorService -> assertNotNull(this.documentProcessorResolver
                        .resolve(processorService.getProcessorServiceClass())));
    }

    @Test
    void given_invalidKey_when_resolve_then_throwException() {
        // Given
        final String invalidKey = "Invalid";

        // When
        // Then
        assertThrows(DocumentManagerException.class, () -> this.documentProcessorResolver.resolve(invalidKey));
    }
}
