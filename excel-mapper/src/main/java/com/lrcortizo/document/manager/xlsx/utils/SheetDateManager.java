package com.lrcortizo.document.manager.xlsx.utils;

import com.lrcortizo.document.manager.xlsx.model.catalog.CellFormatCatalog;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@UtilityClass
public class SheetDateManager {

    public Object obtainDataFormat(final Object data, final CellFormatCatalog cellFormat) {
        return switch (cellFormat) {
            case DATE, DATE_SHORT -> parseLocalDate(data).orElse(data);
            case DATE_TIME, H_MM_SS -> parseLocalDateTime(data).orElse(data);
            default -> data;
        };
    }

    private Optional<Object> parseLocalDate(final Object data) {
        return Optional.ofNullable(data)
                .map(String::valueOf)
                .map(LocalDate::parse);
    }

    private Optional<Object> parseLocalDateTime(final Object data) {
        return Optional.ofNullable(data)
                .map(String::valueOf)
                .map(LocalDateTime::parse);
    }
}