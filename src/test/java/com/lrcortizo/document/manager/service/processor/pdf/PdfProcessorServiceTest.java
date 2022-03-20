package com.lrcortizo.document.manager.service.processor.pdf;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = PdfProcessorService.class)
@MockBean(PdfReductionService.class)
class PdfProcessorServiceTest {

    private static final int MAX_DOCUMENT_SIZE = 3500;
    private static final int MAX_IMAGE_SIZE = 1000;

    private final PdfProcessorService pdfProcessorService;
    private final PdfReductionService pdfReductionService;

    @Autowired
    public PdfProcessorServiceTest(final PdfProcessorService pdfProcessorService,
                                   final PdfReductionService pdfReductionService) {
        this.pdfProcessorService = pdfProcessorService;
        this.pdfReductionService = pdfReductionService;
    }

    @Test
    void given_smallPdf_when_reducePdf_then_return_same_pdf() throws IOException {
        // given
        final byte[] originalPdf = Files.readAllBytes(Paths.get("src/test/resources/mocks/pdf/sample-small.pdf"));

        // When
        final byte[] result = this.pdfProcessorService.reduceSize(originalPdf, MAX_DOCUMENT_SIZE, MAX_IMAGE_SIZE);

        // then
        assertEquals(originalPdf, result);

    }

    @Test
    void processPdf_should_return_smaller_byteArray_when_big_pdf() throws IOException {
        // given
        final byte[] originalPdf = Files.readAllBytes(Paths.get("src/test/resources/mocks/pdf/sample-big.pdf"));
        when(this.pdfReductionService.reducePdf(originalPdf, MAX_IMAGE_SIZE)).thenReturn("".getBytes());

        // When
        final byte[] result = this.pdfProcessorService.reduceSize(originalPdf, MAX_DOCUMENT_SIZE, MAX_IMAGE_SIZE);

        // then
        assertTrue(result.length < originalPdf.length);
    }
}
