package com.lrcortizo.document.manager.service.processor.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.lrcortizo.document.manager.exception.DocumentManagerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.stream.IntStream;

@Service
@Slf4j
public class PdfReductionService {

    private static final int BITS_PER_COMPONENT = 8;

    public byte[] reducePdf(final byte[] pdf, final int maxImageSize) {
        try {
            final PdfReader reader = new PdfReader(pdf);
            final int xrefSize = reader.getXrefSize();
            // Look for image and manipulate image stream
            IntStream.range(0, xrefSize)
                    .mapToObj(reader::getPdfObject)
                    .filter(Objects::nonNull)
                    .filter(PdfObject::isStream)
                    .map(PRStream.class::cast)
                    .forEach(stream -> this.processStream(stream, maxImageSize));

            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final PdfStamper stamper = new PdfStamper(reader, outputStream);

            reader.removeFields();
            reader.removeUnusedObjects();

            IntStream.rangeClosed(1, reader.getNumberOfPages())
                    .forEach(index -> this.setPageContent(reader, index + 1));

            stamper.close();
            return outputStream.toByteArray();
        } catch (final DocumentException | IOException | UncheckedIOException exception) {
            log.error("Error processing pdf", exception);
            throw new DocumentManagerException(exception);
        }
    }

    private void setPageContent(final PdfReader reader, final int index) {
        try {
            reader.setPageContent(index, reader.getPageContent(index));
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private void processStream(final PRStream stream, final int maxImageSize) {
        final PdfObject pdfSubtype = stream.get(PdfName.SUBTYPE);
        if (pdfSubtype != null && pdfSubtype.toString().equals(PdfName.IMAGE.toString())) {
            try {
                this.processImage(stream, maxImageSize);
            } catch (final IOException exception) {
                log.warn("Error processing image", exception);
            }
        }
    }

    private void processImage(final PRStream stream, final int maxImageSize) throws IOException {
        final PdfImageObject image = new PdfImageObject(stream);
        final BufferedImage bufferedImage = image.getBufferedImage();
        if (bufferedImage != null) {
            this.processExistingImage(stream, bufferedImage, maxImageSize);
        }
    }

    private void processExistingImage(final PRStream stream, final BufferedImage bufferedImage, final int maxImageSize)
            throws IOException {
        final double factor = this.getFactor(bufferedImage, maxImageSize);
        final int width = (int) (bufferedImage.getWidth() * factor);
        final int height = (int) (bufferedImage.getHeight() * factor);
        if (factor < 1.0F) {
            // only process images with reduction factor, small images not need to reduce
            final BufferedImage newBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            final AffineTransform affineTransform = AffineTransform.getScaleInstance(factor, factor);
            final Graphics2D graphics2D = newBufferedImage.createGraphics();
            graphics2D.drawRenderedImage(bufferedImage, affineTransform);
            final ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
            ImageIO.write(newBufferedImage, "JPG", imgBytes);
            stream.clear();
            stream.setData(imgBytes.toByteArray(), false, PdfStream.BEST_COMPRESSION);
            stream.put(PdfName.TYPE, PdfName.XOBJECT);
            stream.put(PdfName.SUBTYPE, PdfName.IMAGE);
            stream.put(PdfName.FILTER, PdfName.DCTDECODE);
            stream.put(PdfName.WIDTH, new PdfNumber(width));
            stream.put(PdfName.HEIGHT, new PdfNumber(height));
            stream.put(PdfName.BITSPERCOMPONENT, new PdfNumber(BITS_PER_COMPONENT));
            stream.put(PdfName.COLORSPACE, PdfName.DEVICERGB);
        }
    }

    private double getFactor(final BufferedImage bufferedImage, final int maxImageSize) {
        // factor between 0 and 1
        final double factorWidth = this.getFactor(bufferedImage.getWidth(), maxImageSize);
        final double factorHeight = this.getFactor(bufferedImage.getHeight(), maxImageSize);
        // return min factor (max reduction)
        return Math.min(factorWidth, factorHeight);
    }

    private double getFactor(final int size, final int maxImageSize) {
        // if size in pixels less than maxImageSize factor 1, no reduction
        // otherwise factor is maxImageSize/size to fit to the maxImageSize
        return size <= maxImageSize ? 1.0F : (((double) maxImageSize) / size);
    }
}
