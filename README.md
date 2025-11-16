**Java document manager library.**

# Modules

## document-tookit

Document toolkit. 
This module is designed using a strategy pattern to add more features and support more document types.

Current supported document types:
- PDF

Current features:
- Document size reduction

### How to use
- import the **DocumentManagerService** bean to access the library features through its methods
- Use the **DocumentType** enum class to indicate the wanted document type

### How to add more document types
- Add new processor service class implementation of **DocumentProcessorService** interface
- Create new type in **DocumentType** enum class with its correct processor service class name
- Update this readme info

### How to add more features
- Add new feature to **DocumentProcessorService** interface or create a new interface if it is more appropriate
- Implement the feature in all implementations of the interface or create the new implementation classes for all types in case of a new interface
- Adapt **DocumentManagerService** class to support new feature
- Update this readme info

## excel-mapper

This module provides tools for mapping an Excel file to Java classes and vice versa.

### How to use

#### SheetDTO model

``` Java
public interface SheetDTO {}  
``` 
The exported model in Excel format must implement the SheetDTO interface, for example:

``` Java
@CellHeader(labels = {"Example name", "Example code"}, filterScroll = true)  
public record ExampleSheetDTO (  
  
        @CellData(name = "exammpleName", column = 0, type = CellFormatCatalog.STRING)  
        String exammpleName,  
  
        @CellData(name = "exampleCode", column = 1, type = CellFormatCatalog.STRING)  
        String exampleCode)  implements SheetDTO { }
``` 

This model must have the following Java annotations
@CellHeader - [Excel header]
@CellData   - [Data column]

#### @CellHeader annotation

``` Java
@CellHeader(
  labels = { "Column1", "Column2" ....},
  filterScroll = true / false,
  style = CellStyleCatalog.YOUR_STYLE,
  headerRow = 0
)
``` 
- **labels**: specifies the order of the column names in the header.
- **filterScroll**: enables the column sorting filter. [DEFAULT: false]
- **style**: defines the style for the entire header row. [DEFAULT: CellStyleCatalog.HEADER]
- **headerRow**: indicates the row number where the header should start. [DEFAULT: 0]


#### @CellData notation

``` Java
public record ExampleSheetDTO (  
  
        @CellData(name = "name", column = 0, type = CellFormatCatalog.STRING, style = CellStyleCatalog.LOCKED)  
        String name,  
  
        @CellData(name = "code", column = 1, type = CellFormatCatalog.STRING)  
        String code,
........
``` 
- **name**: is the variable name of the column
  (the same as the model variable). [REQUIRED]
- **column**: is the horizontal order of the column. [REQUIRED]
- **type**: is the Excel variable type needed for conversion - allowed values: STRING, FORMULA, NUMBER, BOOLEAN, DATE, DATE_SHORT, DATE_TIME, H_MM_SS, NUMBER_DP. [DEFAULT: GENERAL]
- **style**: is the style of the individual cell.
  [DEFAULT: CellStyleCatalog.STANDARD]
- **selectable**:  is the enumeration that must be selected.
  Note: the enumeration requires toString() to retrieve the value from the selection window. [DEFAULT: null]

**_Ejemplos de modelo de columnas._**

- @CellData(name = "isBoolean", column = 0, type = CellFormatCatalog.BOOLEAN, selectable = BooleanSelectable.class)  
  boolean isBoolean,

- @CellData(name = "myString", column = 1, type = CellFormatCatalog.STRING)  
  String myString,

- @CellData(name = "myInt", column = 2, type = CellFormatCatalog.NUMBER)  
  int/long/double myInt,

- @CellData(name = "myExcelFormula", column = 3, type = CellFormatCatalog.FORMULA)  
  String myExcelFormula,

- @CellData(name = "myLocalDate", column = 4, type = CellFormatCatalog.DATE)  
  LocalDate myLocalDate,

- @CellData(name = "myTime", column = 5, type = CellFormatCatalog.H_MM_SS)  
  LocalDateTime myTime,

- @CellData(name = "myCurrency", column = 6, type = CellFormatCatalog.CURRENCY_EUR)  
  Double myCurrency,


#### Excel import
Example
``` Java
public void importExcel(final MultipartFile file) {  
  
    final XSSFWorkbook xssfWorkbook = ImportManager.obtainValidWorkbook(file);  
    
    final List<SheetDTO> sheets = ImportManager
                    .obtainSheets(xssfWorkbook, this.objectMapper, SheetDTOImplement.class);
    ........
``` 

**MultipartFile file** with the same format as the model, is converted into an XSSFWorkbook.class from the Apache Poi library (spreadsheet model), then it is converted into a list of our models (e.g., SheetDTOImplement.class).

#### Excel export
Example
``` Java
public void exportExcel(final List<SheetDTOImplement> mySheets) {   
   final String mySheetName = "Exportacion Excel";  
   final XSSFWorkbook xssfWorkbook = ExportManager
                  .generateWorkbook(productSheets, this.objectMapper, mySheetName);
    ........
``` 

It is converted into an XSSFWorkbook.class from the Apache Poi library (spreadsheet model), then we need to pass everything to an OutputStream.

``` Java
try (final ByteArrayOutputStream stream = new ByteArrayOutputStream()) {  
    workbook.write(stream);  
    workbook.close();
}
```
Always remember to close the workbook stream.


#### Excel cell styles

**CellStyleCatalog**

Styles example:
``` Java
HEADER(CellBackgroundColorCatalog.YELLOW_BG, CellFontStyleCatalog.CALIBRI_WHITE_BOLD, HorizontalAlignment.LEFT, false),  
STANDARD(CellBackgroundColorCatalog.WHITE_BG, CellFontStyleCatalog.CALIBRI_AUTO, HorizontalAlignment.LEFT, false),
LOCKED(CellBackgroundColorCatalog.GREY_BG, CellFontStyleCatalog.CALIBRI_AUTO, HorizontalAlignment.LEFT, true)
``` 
**CellBackgroundColorCatalog**

Color example:

``` Java
WHITE_BG(new XSSFColor(new java.awt.Color(255, 255, 255), null)), ** Blanco **
YELLOW_BG(new XSSFColor(new java.awt.Color(196, 214, 0), null))  ** Amarillo **
``` 

**CellFontStyleCatalog**

Fonts example:

``` Java
CALIBRI_WHITE_BOLD("Calibri", (short) 14, true, HSSFColor.HSSFColorPredefined.WHITE.getIndex()),  
CALIBRI_AUTO("Calibri", (short) 11, false, HSSFColor.HSSFColorPredefined.AUTOMATIC.getIndex());
```
