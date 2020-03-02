package com.vae.account.util;

import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author vae
 */
public class ExcelUtil {

    public static List<Row> listRow(Sheet sheet) {
        return Optional.ofNullable(sheet).map(Sheet::rowIterator)
                .map(iterator -> Spliterators.spliteratorUnknownSize(iterator, Spliterator.SORTED))
                .map(spliterator -> StreamSupport.stream(spliterator, false)).orElse(Stream.empty())
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unused")
    public static List<List<Object>> getTable(Sheet sheet) {
        short maxWidth = 0;
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            short cellNum = sheet.getRow(i).getLastCellNum();
            if (cellNum > maxWidth) {
                maxWidth = cellNum;
            }
        }
        List<List<Object>> data = new ArrayList<>(Collections.nCopies(sheet.getLastRowNum()
                , new ArrayList<>(Collections.nCopies(maxWidth, null))));
        foreachCell(sheet, cell -> data.get(cell.getRowIndex()).set(cell.getColumnIndex(), getCellValue(cell)));
        return data;
    }

    public static void foreachCell(Sheet sheet, Consumer<Cell> consumer) {
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                if (cell != null) {
                    consumer.accept(cell);
                }
            }
        }
    }

    public static Integer findIndex(Map<String, Integer> indexMap, Sheet sheet, List<String> keyList) {
        keyList = Optional.ofNullable(keyList).orElse(new ArrayList<>());
        List<String> finalKeyList = keyList;
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            List<String> collect = Optional.ofNullable(row.cellIterator())
                    .map(iterator -> Spliterators.spliteratorUnknownSize(iterator, Spliterator.SORTED))
                    .map(spliterator -> StreamSupport.stream(spliterator, false)).orElse(Stream.empty())
                    .map(ExcelUtil::getStringValue).collect(Collectors.toList());
            IntStream.range(0, collect.size()).forEach(index -> {
                String cellValue = collect.get(index);
                if (finalKeyList.contains(cellValue)) {
                    indexMap.put(cellValue, index);
                }
            });
            if (indexMap.size() == keyList.size()) {
                return i;
            } else {
                indexMap.clear();
            }
        }
        return 0;
    }

    public static String getStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        String s;
        try {
            Object value = getCellValue(cell);
            s = value == null ? null : String.valueOf(value);
        } catch (Exception e) {
            System.out.println(cell.getRowIndex() + "," + cell.getColumnIndex());
            throw e;
        }
        return s;
    }

    public static Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case FORMULA:
                FormulaEvaluator formulaEvaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = formulaEvaluator.evaluate(cell);
                return getCellValue(cellValue);
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case STRING:
                return cell.getStringCellValue();
            default:
                return null;
        }
    }

    public static CellType getCellType(Cell cell) {
        CellType cellType = cell.getCellType();
        if (cellType == CellType.FORMULA) {
            FormulaEvaluator formulaEvaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            return cellValue.getCellType();
        } else {
            return cellType;
        }
    }


    private static Object getCellValue(CellValue cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringValue();
            case NUMERIC:
                return cell.getNumberValue();
            default:
                return null;
        }
    }


    public static boolean existCell(Cell cell) {
        String value = getStringValue(cell);
        return !(cell == null || value == null || value.replaceAll(" ", "").isEmpty());
    }

    public static void copyRow(Row sourceRow, Row targetRow, List<Integer> sourceKeyIndex, List<Integer> targetKeyIndex) {
        for (int i = 0; i < sourceKeyIndex.size(); i++) {
            Cell sourceCell = sourceRow.getCell(sourceKeyIndex.get(i));
            Cell targetCell = targetRow.getCell(targetKeyIndex.get(i));
            if (targetCell == null) {
                targetCell = targetRow.createCell(targetKeyIndex.get(i));
            } else if (existCell(targetCell)) {
                continue;
            }
            if (getCellValue(sourceCell) != null) {
                switch (sourceCell.getCellType()) {
                    case NUMERIC:
                        targetCell.setCellValue(sourceCell.getNumericCellValue());
                        break;
                    case STRING:
                        targetCell.setCellValue(sourceCell.getStringCellValue());
                        break;
                    case BOOLEAN:
                        targetCell.setCellValue(sourceCell.getBooleanCellValue());
                        break;
                    case FORMULA:
                        CellType cellType = getCellType(sourceCell);
                        Object cellValue = getCellValue(sourceCell);
                        if (cellType == CellType.NUMERIC) {
                            targetCell.setCellValue((double) cellValue);
                        } else if (cellType == CellType.STRING) {
                            targetCell.setCellValue((String) cellValue);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public static Workbook multipartFileToExcel(MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            return WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Sheet getSheetByName(Workbook workbook, String sheetName) {
        sheetName = sheetName == null ? "" : sheetName;
        if (workbook == null) {
            return null;
        }
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        while (sheetIterator.hasNext()) {
            Sheet next = sheetIterator.next();
            String tempSheetName = next.getSheetName();
            if (sheetName.equals(tempSheetName)) {
                return next;
            }
        }
        return null;
    }

}
