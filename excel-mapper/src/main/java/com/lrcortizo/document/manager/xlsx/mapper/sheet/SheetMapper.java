package com.lrcortizo.document.manager.xlsx.mapper.sheet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lrcortizo.document.manager.xlsx.annotation.CellData;
import com.lrcortizo.document.manager.xlsx.model.SheetDTO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@UtilityClass
@Slf4j
public class SheetMapper {

    public SheetDTO obtainSheet(final ObjectMapper objectMapper, final Map<String, Object> sheetData,
                                final Class<? extends SheetDTO> cls) {
        final Map<String, Object> nestedData = buildNestedStructure(objectMapper, sheetData, cls);
        return objectMapper.convertValue(nestedData, cls);
    }

    public Map<String, Object> obtainSheetValueByName(final SheetDTO sheetData) {
        return flattenObject(sheetData);
    }

    private Map<String, Object> flattenObject(final Object object) {
        return Optional.ofNullable(object)
                .map(obj -> Arrays.stream(obj.getClass().getDeclaredFields())
                        .flatMap(field -> processFieldToFlat(obj, field).entrySet().stream())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                .orElse(Map.of());
    }

    private Map<String, Object> processFieldToFlat(final Object obj, final Field field) {
        return getFieldValue(field, obj)
                .map(fieldValue -> extractFieldData(field, fieldValue))
                .orElse(Map.of());
    }

    private Optional<Object> getFieldValue(final Field field, final Object obj) {
        try {
            if (!field.canAccess(obj)) {
                makeAccessible(field);
            }
            return Optional.ofNullable(field.get(obj));
        } catch (final IllegalAccessException e) {
            log.warn("Unable to access field '{}' on class '{}'", field.getName(), obj.getClass().getSimpleName(), e);
            return Optional.empty();
        }
    }

    private void makeAccessible(final Field field) {
        field.setAccessible(true);
    }

    private Map<String, Object> extractFieldData(final Field field, final Object fieldValue) {
        return Optional.ofNullable(field.getAnnotation(CellData.class))
                .map(cellData -> Map.of(cellData.name(), fieldValue))
                .orElseGet(() -> extractNestedSheetData(field, fieldValue));
    }

    private Map<String, Object> extractNestedSheetData(final Field field, final Object fieldValue) {
        return SheetDTO.class.isAssignableFrom(field.getType())
                ? flattenObject(fieldValue)
                : Map.of();
    }

    private Map<String, Object> buildNestedStructure(final ObjectMapper objectMapper,
                                                     final Map<String, Object> flatData,
                                                     final Class<? extends SheetDTO> cls) {
        return Arrays.stream(cls.getDeclaredFields())
                .map(field -> processSheetField(objectMapper, flatData, field))
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<String, Object> processSheetField(final ObjectMapper objectMapper,
                                                  final Map<String, Object> flatData,
                                                  final Field field) {
        return Optional.ofNullable(field.getAnnotation(CellData.class))
                .map(cellData -> Map.of(field.getName(), flatData.get(cellData.name())))
                .orElseGet(() -> buildNestedField(objectMapper, flatData, field));
    }

    private Map<String, Object> buildNestedField(final ObjectMapper objectMapper,
                                                 final Map<String, Object> flatData,
                                                 final Field field) {
        return SheetDTO.class.isAssignableFrom(field.getType())
                ? buildNestedFieldMap(objectMapper, flatData, field)
                : Map.of();
    }

    private Map<String, Object> buildNestedFieldMap(final ObjectMapper objectMapper,
                                                    final Map<String, Object> flatData,
                                                    final Field field) {
        final Map<String, Object> nestedData = buildNestedStructure(
                objectMapper, flatData, (Class<? extends SheetDTO>) field.getType());
        final Object nestedObject = objectMapper.convertValue(nestedData, field.getType());
        return Map.of(field.getName(), nestedObject);
    }
}
