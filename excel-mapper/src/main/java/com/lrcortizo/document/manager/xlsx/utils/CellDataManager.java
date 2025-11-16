package com.lrcortizo.document.manager.xlsx.utils;

import com.lrcortizo.document.manager.xlsx.annotation.CellData;
import com.lrcortizo.document.manager.xlsx.annotation.CellHeader;
import com.lrcortizo.document.manager.xlsx.model.SheetDTO;
import com.lrcortizo.document.manager.xlsx.model.catalog.CellFormatCatalog;
import lombok.experimental.UtilityClass;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


@UtilityClass
public class CellDataManager {

    public CellHeader obtainCellHeader(final AnnotatedElement annotatedElement) {
        return annotatedElement.getAnnotation(CellHeader.class);
    }

    public List<CellData> obtainCellData(final Class<? extends SheetDTO> sheetClass) {
        return collectCellData(sheetClass)
                .sorted(Comparator.comparing(CellData::column))
                .toList();
    }

    public String[] obtainAllLabels(final Class<? extends SheetDTO> sheetClass) {
        return collectLabels(sheetClass).toArray(String[]::new);
    }

    public void addDataCell(final XSSFRow row, final CellData cellData, final XSSFCellStyle style, final Object value) {
        buildCell(row, cellData.column(), value, cellData.type(), style);
    }

    public void addHeaderDataCell(final XSSFRow row, final XSSFCellStyle style, final Object value, final int cellColumn) {
        buildCell(row, cellColumn, value, CellFormatCatalog.STRING, style);
    }

    private Stream<CellData> collectCellData(final Class<?> clazz) {
        return Stream.of(clazz.getDeclaredFields())
                .flatMap(CellDataManager::processCellDataField);
    }

    private Stream<CellData> processCellDataField(final Field field) {
        return Optional.ofNullable(field.getAnnotation(CellData.class))
                .map(Stream::of)
                .orElseGet(() -> processNestedCellData(field));
    }

    private Stream<CellData> processNestedCellData(final Field field) {
        return SheetDTO.class.isAssignableFrom(field.getType())
                ? collectCellData(field.getType())
                : Stream.empty();
    }

    private Stream<String> collectLabels(final Class<?> clazz) {
        final CellHeader header = clazz.getAnnotation(CellHeader.class);
        final Stream<String> currentLabels = header != null
                ? Stream.of(header.labels())
                : Stream.empty();

        final Stream<String> nestedLabels = Stream.of(clazz.getDeclaredFields())
                .flatMap(CellDataManager::processLabelField);

        return Stream.concat(currentLabels, nestedLabels);
    }

    private Stream<String> processLabelField(final Field field) {
        return SheetDTO.class.isAssignableFrom(field.getType())
                ? collectLabels(field.getType())
                : Stream.empty();
    }

    private void buildCell(final XSSFRow row, final int cellIndex, final Object value, final CellFormatCatalog cellFormat,
                           final XSSFCellStyle style) {
        new CellBuilder(row, cellFormat.getType(), style, cellIndex,
                SheetDateManager.obtainDataFormat(value, cellFormat));
    }
}
