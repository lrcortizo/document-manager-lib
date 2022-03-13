package com.lrcortizo.document.manager.service.processor.pdf;

import com.lrcortizo.document.manager.service.processor.DocumentProcessorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PdfProcessorService implements DocumentProcessorService {

    private final PdfReductionService pdfReductionService;

    @Override
    public byte[] reduceSize(final byte[] pdf, final int maxDocumentSize, final int maxImageSize) {
        return maxDocumentSize < pdf.length ? this.pdfReductionService.reducePdf(pdf, maxImageSize) : pdf;
    }
}
