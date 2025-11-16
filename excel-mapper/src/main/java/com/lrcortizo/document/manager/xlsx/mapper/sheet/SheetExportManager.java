package com.lrcortizo.document.manager.xlsx.mapper.sheet;

import com.lrcortizo.document.manager.xlsx.annotation.CellData;
import com.lrcortizo.document.manager.xlsx.annotation.CellHeader;
import com.lrcortizo.document.manager.xlsx.model.SheetDTO;
import com.lrcortizo.document.manager.xlsx.model.catalog.CellFormatCatalog;
import com.lrcortizo.document.manager.xlsx.model.catalog.SheetConstants;
import com.lrcortizo.document.manager.xlsx.model.selectable.VoidSelectable;
import com.lrcortizo.document.manager.xlsx.utils.CellDataManager;
import com.lrcortizo.document.manager.xlsx.utils.CellStyleManager;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@UtilityClass
public class SheetExportManager {

    public void createHeaderRow(final XSSFWorkbook workbook,
                                final XSSFSheet sheet,
                                final List<? extends SheetDTO> dataSheets) {

        final Class<? extends SheetDTO> sheetClass = dataSheets.getFirst().getClass();
        final CellHeader cellHeader = CellDataManager.obtainCellHeader(sheetClass);
        final String[] labels = CellDataManager.obtainAllLabels(sheetClass);
        final int totalColumns = labels.length;

        final XSSFRow headerRow = sheet.createRow(cellHeader.headerRow());
        sheet.createFreezePane(0, 1);

        IntStream.range(0, totalColumns)
                .forEach(column -> createHeaderCell(workbook, headerRow, cellHeader, labels[column], column));
        autosizeColumns(sheet, totalColumns);
        setColumnsFilter(sheet, cellHeader, dataSheets.size(), totalColumns);
    }

    public void createDataRows(final XSSFWorkbook workbook, final XSSFSheet sheet,
                               final List<? extends SheetDTO> dataSheets) {

        IntStream.range(0, dataSheets.size())
                .forEach(cellIndex -> createDataRow(
                        workbook, sheet, dataSheets.get(cellIndex), cellIndex + SheetConstants.DATA_START_ROW));

        setColumnsStyle(workbook, sheet, dataSheets.getFirst().getClass());
        autosizeColumns(sheet, dataSheets.size());
    }

    private void setColumnsFilter(final XSSFSheet sheet, final CellHeader cellHeader, final int totalRows,
                                  final int totalColumns) {
        if (cellHeader.filterScroll()) {
            sheet.setAutoFilter(new CellRangeAddress(0, totalRows - 1, 0, totalColumns - 1));
        }
    }

    private void createDataRow(final XSSFWorkbook workbook, final XSSFSheet sheet, final SheetDTO dataSheet,
                               final int indexRow) {
        final XSSFRow row = sheet.createRow(indexRow);
        final List<CellData> cellsData = CellDataManager.obtainCellData(dataSheet.getClass());
        final Map<String, Object> sheetValueByName = SheetMapper.obtainSheetValueByName(dataSheet);
        cellsData.forEach(cellData -> createDataCell(workbook, row, cellData, sheetValueByName));
        cellsData.forEach(cellData -> setValidator(sheet, cellData, indexRow));
    }

    private void createDataCell(final XSSFWorkbook workbook, final XSSFRow row, final CellData cellData,
                                final Map<String, Object> sheetValueByName) {
        final Object value = sheetValueByName.get(cellData.name());
        final XSSFCellStyle xssfCellStyle = CellStyleManager.obtainDataCellStyle(workbook, cellData.style(), cellData.type());
        CellDataManager.addDataCell(row, cellData, xssfCellStyle, value);
    }

    private void createHeaderCell(final XSSFWorkbook workbook, final XSSFRow row, final CellHeader cellHeader,
                                  final Object value, final int cellColumn) {
        final XSSFCellStyle xssfCellStyle = CellStyleManager.obtainDataCellStyle(workbook, cellHeader.style(),
                CellFormatCatalog.STRING);

        CellDataManager.addHeaderDataCell(row, xssfCellStyle, value, cellColumn);
    }

    private void setColumnsStyle(final XSSFWorkbook workbook, final XSSFSheet sheet,
                                 final Class<? extends SheetDTO> sheetClass) {
        final List<CellData> cellsData = CellDataManager.obtainCellData(sheetClass);
        IntStream.range(0, cellsData.size())
                .forEach(column -> setColumnStyle(workbook, sheet, cellsData.get(column), column));
    }

    private void setColumnStyle(final XSSFWorkbook workbook,
                                final XSSFSheet sheet,
                                final CellData cellData, final int column) {
        final XSSFCellStyle xssfCellStyle = CellStyleManager.obtainDataCellStyle(workbook, cellData.style(), cellData.type());
        sheet.getColumnHelper().setColDefaultStyle(column, xssfCellStyle);
    }

    private void setValidator(final XSSFSheet sheet, final CellData cellData, final int row) {
        final String[] explicitConstants = Stream.of(cellData.selectable())
                .filter(Predicate.not(VoidSelectable.class::equals))
                .map(Class::getEnumConstants)
                .flatMap(selectableConstants -> Stream.of(selectableConstants).map(Enum::toString))
                .toArray(String[]::new);

        if (explicitConstants.length != 0) {
            addValidator(sheet, explicitConstants, cellData.column(), row);
        }
    }

    private void addValidator(final XSSFSheet sheet, final String[] explicitConstants, final int column, final int row) {
        final DataValidationHelper dvHelper = sheet.getDataValidationHelper();
        final DataValidationConstraint dvConstraint = dvHelper.createExplicitListConstraint(explicitConstants);
        final CellRangeAddressList addressList = new CellRangeAddressList(
                SheetConstants.DATA_START_ROW, row, column, column);

        final DataValidation dataValidation = dvHelper.createValidation(dvConstraint, addressList);

        if (dataValidation instanceof final XSSFDataValidation xssfDataValidation) {
            xssfDataValidation.setSuppressDropDownArrow(true);
            xssfDataValidation.setShowErrorBox(true);
        }
        sheet.addValidationData(dataValidation);
    }

    private void autosizeColumns(final XSSFSheet sheet, final int column) {
        IntStream.range(0, column)
                .forEach(sheet::autoSizeColumn);
    }
}