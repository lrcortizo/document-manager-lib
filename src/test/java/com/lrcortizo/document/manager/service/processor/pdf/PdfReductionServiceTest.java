package com.lrcortizo.document.manager.service.processor.pdf;

import com.lrcortizo.document.manager.exception.DocumentManagerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = PdfReductionService.class
)
class PdfReductionServiceTest {

    private static final int MAX_IMAGE_SIZE = 1000;

    private final PdfReductionService pdfReductionService;

    @Autowired
    public PdfReductionServiceTest(final PdfReductionService pdfReductionService) {
        this.pdfReductionService = pdfReductionService;
    }

    @Test
    void given_bigPdf_when_reducePdf_then_return_smallPdf() throws IOException {
        // Given
        final byte[] pdfDocument = Files.readAllBytes(Paths.get("src/test/resources/mocks/pdf/sample-big.pdf"));

        // When
        final byte[] result = this.pdfReductionService.reducePdf(pdfDocument, MAX_IMAGE_SIZE);

        // Then
        assertTrue(result.length < pdfDocument.length);
    }

    @Test
    void given_pdfWithErrors_when_reducePdf_then_throwException() {
        // Given
        final byte[] pdfDocument = "".getBytes();

        // When
        // Then
        assertThrows(DocumentManagerException.class,
                () -> this.pdfReductionService.reducePdf(pdfDocument, MAX_IMAGE_SIZE));
    }
}
