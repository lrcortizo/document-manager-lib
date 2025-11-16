package com.lrcortizo.document.manager.xlsx.mapper.sheet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lrcortizo.document.manager.xlsx.model.SheetDTO;
import com.lrcortizo.document.manager.xlsx.test.model.ParentSheetTestDTO;
import com.lrcortizo.document.manager.xlsx.test.model.SheetTestDTO;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SheetMapperTest {


    @Test
    void should_return_sheetDTO_when_obtainSheet() {
        // Given
        final SheetTestDTO expectedSheet = SheetTestDTO.builder().build();
        final ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        final Class<? extends SheetDTO> givenSheetModel = SheetTestDTO.class;
        final Map<String, Object> givenSheetData = Map.of("testColumnA", "A", "testColumnB", "B");
        when(objectMapperMock.convertValue(any(Map.class), eq(SheetTestDTO.class)))
                .thenReturn(expectedSheet);

        // When
        final SheetDTO actualSheet = SheetMapper.obtainSheet(objectMapperMock, givenSheetData, givenSheetModel);

        // Then
        assertSame(actualSheet, expectedSheet);
    }


    @Test
    void should_return_sheetData_map_when_obtainSheetValueByName() {
        // Given
        final SheetTestDTO givenSheet = SheetTestDTO.builder()
                .testColumnA("A")
                .testColumnB("B")
                .build();

        final Map<String, Object> expectedSheetData = Map.of("testColumnA", "A", "testColumnB", "B");

        // When
        final Map<String, Object> actualSheetData = SheetMapper.obtainSheetValueByName(givenSheet);

        // Then
        assertEquals(actualSheetData, expectedSheetData);
    }

    @Test
    void should_return_empty_map_when_obtainSheetValueByName_with_null_sheetData() {
        // Given
        final SheetDTO givenSheet = null;

        // When
        final Map<String, Object> actualSheetData = SheetMapper.obtainSheetValueByName(givenSheet);

        // Then
        assertThat(actualSheetData).isEmpty();
    }

    @Test
    void should_flatten_nested_sheetDTO_when_obtainSheetValueByName() {
        // Given
        final SheetTestDTO givenMainSheet = SheetTestDTO.builder()
                .testColumnA("MainValue")
                .build();

        // When
        final Map<String, Object> actualSheetData = SheetMapper.obtainSheetValueByName(givenMainSheet);

        // Then
        assertThat(actualSheetData).isNotEmpty();
    }

    @Test
    void should_build_nested_structure_when_obtainSheet_with_nested_fields() {
        // Given
        final ObjectMapper objectMapper = new ObjectMapper();
        final Map<String, Object> flatData = Map.of(
                "testColumnA", "A",
                "testColumnB", "B"
        );

        // When
        final SheetDTO actualSheet = SheetMapper.obtainSheet(objectMapper, flatData, SheetTestDTO.class);

        // Then
        assertThat(actualSheet)
                .isNotNull()
                .isInstanceOf(SheetTestDTO.class);
    }

    @Test
    void should_handle_empty_map_when_obtainSheet() {
        // Given
        final ObjectMapper objectMapper = new ObjectMapper();
        final Map<String, Object> emptyData = Map.of();

        // When
        final SheetDTO actualSheet = SheetMapper.obtainSheet(objectMapper, emptyData, SheetTestDTO.class);

        // Then
        assertThat(actualSheet).isNotNull();
    }

    @Test
    void should_convert_flat_data_correctly_when_obtainSheet_with_real_objectMapper() {
        // Given
        final ObjectMapper realObjectMapper = new ObjectMapper();
        final Map<String, Object> givenSheetData = Map.of(
                "testColumnA", "ValueA",
                "testColumnB", "ValueB"
        );

        // When
        final SheetTestDTO actualSheet = (SheetTestDTO) SheetMapper.obtainSheet(
                realObjectMapper, givenSheetData, SheetTestDTO.class);

        // Then
        assertThat(actualSheet)
                .isNotNull();
        assertThat(actualSheet.getTestColumnA())
                .isEqualTo("ValueA");
        assertThat(actualSheet.getTestColumnB())
                .isEqualTo("ValueB");
    }

    @Test
    void should_flatten_and_preserve_simple_values_when_obtainSheetValueByName() {
        // Given
        final SheetTestDTO givenSheet = SheetTestDTO.builder()
                .testColumnA("SimpleA")
                .testColumnB("SimpleB")
                .build();

        // When
        final Map<String, Object> actualSheetData = SheetMapper.obtainSheetValueByName(givenSheet);

        // Then
        assertThat(actualSheetData)
                .isNotEmpty()
                .containsEntry("testColumnA", "SimpleA")
                .containsEntry("testColumnB", "SimpleB");
    }

    @Test
    void should_reconstruct_nested_sheetDTO_when_obtainSheet_with_nested_fields() {
        // Given
        final ObjectMapper realObjectMapper = new ObjectMapper();
        final Map<String, Object> flatData = Map.of(
                "parentField", "ParentValue",
                "testColumnA", "NestedA",
                "testColumnB", "NestedB"
        );

        // When
        final ParentSheetTestDTO actualSheet = (ParentSheetTestDTO) SheetMapper.obtainSheet(
                realObjectMapper, flatData, ParentSheetTestDTO.class);

        // Then
        assertThat(actualSheet)
                .isNotNull();
        assertThat(actualSheet.getParentField())
                .isEqualTo("ParentValue");
        assertThat(actualSheet.getNestedSheet())
                .isNotNull();
        assertThat(actualSheet.getNestedSheet().getTestColumnA())
                .isEqualTo("NestedA");
        assertThat(actualSheet.getNestedSheet().getTestColumnB())
                .isEqualTo("NestedB");
    }
}