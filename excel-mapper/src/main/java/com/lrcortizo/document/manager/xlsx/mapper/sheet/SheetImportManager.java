package com.lrcortizo.document.manager.xlsx.mapper.sheet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lrcortizo.document.manager.xlsx.annotation.CellData;
import com.lrcortizo.document.manager.xlsx.exception.ExcelImportException;
import com.lrcortizo.document.manager.xlsx.model.SheetDTO;
import com.lrcortizo.document.manager.xlsx.model.catalog.SheetConstants;
import com.lrcortizo.document.manager.xlsx.utils.CellDataManager;
import com.lrcortizo.document.manager.xlsx.utils.StreamManager;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@UtilityClass
@Slf4j
public class SheetImportManager {

    public List<SheetDTO> obtainSheets(final Sheet sheet, final ObjectMapper objectMapper,
                                       final Class<? extends SheetDTO> sheetModel) {
        final List<Row> rows = StreamManager.obtainStreamFromIterator(sheet.spliterator())
                .filter(row -> isStartRow(row.getRowNum()))
                .toList();

        final List<CellData> cellsData = CellDataManager.obtainCellData(sheetModel);

        return rows.stream()
                .map(row -> obtainRowsData(row, cellsData))
                .map(rowsData -> SheetMapper.obtainSheet(objectMapper, rowsData, sheetModel))
                .toList();
    }

    private Map<String, Object> obtainRowsData(final Row row, final List<CellData> cellsData) {
        final Map<Integer, String> cellLabelByColumn = obtainCellDataByColumn(cellsData);
        final Map<Integer, Object> cellByColumn = StreamManager.obtainStreamFromIterator(row.spliterator())
                .collect(Collectors.toUnmodifiableMap(Cell::getColumnIndex, SheetImportManager::convertCellValue));

        try {
            return cellByColumn.keySet().stream()
                    .collect(Collectors.toUnmodifiableMap(cellLabelByColumn::get, cellByColumn::get));
        } catch (final RuntimeException npe) {
            log.debug(String.format("Excel file doesnt match with model type: %s", npe.getMessage()));
            throw new ExcelImportException("Excel file doesnt match with model type");
        }

    }

    private Map<Integer, String> obtainCellDataByColumn(final List<CellData> cellsData) {
        return cellsData.stream()
                .collect(Collectors.toUnmodifiableMap(CellData::column, CellData::name));
    }

    private Object convertCellValue(final Cell cell) {
        return switch (cell.getCellType()) {
            case _NONE, BLANK -> "";
            case NUMERIC -> convertNumericCell(cell);
            case BOOLEAN -> cell.getBooleanCellValue();
            case STRING -> cell.getStringCellValue();
            case FORMULA -> obtainFormula(cell);
            case ERROR -> cell.getErrorCellValue();
        };
    }

    private Object convertNumericCell(final Cell cell) {
        return DateUtil.isCellDateFormatted(cell) ? convertDateCell(cell) : cell.getNumericCellValue();
    }

    private Object convertDateCell(final Cell cell) {
        final LocalDateTime dateTime = cell.getLocalDateTimeCellValue();
        // If no time component (00:00:00), return LocalDate only
        return LocalTime.MIN.equals(dateTime.toLocalTime()) ? dateTime.toLocalDate() : dateTime;
    }

    private Object obtainFormula(final Cell cell) {
        final String formula = cell.getCellFormula();
        return "TRUE()".equalsIgnoreCase(formula) || "FALSE()".equalsIgnoreCase(formula) ?
                cell.getBooleanCellValue() :
                formula;
    }

    private boolean isStartRow(final int rowNumber) {
        return SheetConstants.DATA_START_ROW <= rowNumber;
    }
}