package com.vae.account.service;

import com.vae.account.dto.ExcelMappingDTO;
import com.vae.account.dto.ExcelMappingItemDTO;
import com.vae.account.enums.TypeEnums;
import com.vae.account.util.ExcelUtil;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author vae
 */
@Service
public class ExcelService {

    /**
     * merge excel
     *
     * @param excelConvertType merge rule
     * @param sourceFile       source file
     * @param targetFile       target file
     * @param sourceSheetName  source sheet name
     * @param targetSheetName  target sheet name
     * @return workbook
     */
    public Workbook merge(TypeEnums excelConvertType, MultipartFile sourceFile, MultipartFile targetFile
            , String sourceSheetName, String targetSheetName) {
        Workbook sourceWorkbook = ExcelUtil.multipartFileToExcel(sourceFile);
        Workbook targetWorkbook = ExcelUtil.multipartFileToExcel(targetFile);
        Sheet sourceSheet = ExcelUtil.getSheetByName(sourceWorkbook, sourceSheetName);
        Sheet targetSheet = ExcelUtil.getSheetByName(targetWorkbook, targetSheetName);
        assert sourceSheet != null;
        assert targetSheet != null;
        List<Integer> sourceExcelKeyIndex = new ArrayList<>();
        List<Integer> targetExcelKeyIndex = new ArrayList<>();
        List<Integer> sourceExcelDataIndex = new ArrayList<>();
        List<Integer> targetExcelDataIndex = new ArrayList<>();
        Map<String, Integer> sourceExcelIndexMap = new HashedMap<>();
        Map<String, Integer> targetExcelIndexMap = new HashedMap<>();
        int firstSourceRowIndex, firstTargetRowIndex;
        //  匹配index
        ExcelMappingDTO excelMappingDTO = excelConvertType.getExcelMappingDTO();
        List<String> sourceKeyList = excelMappingDTO.getList().stream().map(ExcelMappingItemDTO::getSourceKey).collect(Collectors.toList());
        List<String> targetKeyList = excelMappingDTO.getList().stream().map(ExcelMappingItemDTO::getTargetKey).collect(Collectors.toList());
        firstSourceRowIndex = ExcelUtil.findIndex(sourceExcelIndexMap, sourceSheet, sourceKeyList);
        firstTargetRowIndex = ExcelUtil.findIndex(targetExcelIndexMap, targetSheet, targetKeyList);
        excelMappingDTO.getList().forEach(key -> {
            if (key.getPrimaryKey()) {
                sourceExcelKeyIndex.add(sourceExcelIndexMap.get(key.getSourceKey()));
                targetExcelKeyIndex.add(targetExcelIndexMap.get(key.getTargetKey()));
            } else {
                sourceExcelDataIndex.add(sourceExcelIndexMap.get(key.getSourceKey()));
                targetExcelDataIndex.add(targetExcelIndexMap.get(key.getTargetKey()));
            }
        });
        assert sourceKeyList.size() == targetKeyList.size();
        assert sourceExcelDataIndex.size() == targetExcelDataIndex.size();
        //  匹配源excel
        Map<String, Row> sourceRowMap = getKeyRowMap(sourceSheet, sourceExcelKeyIndex, firstSourceRowIndex);
        //  修改row
        writeRow(targetSheet, sourceRowMap, sourceExcelKeyIndex, sourceExcelDataIndex, targetExcelDataIndex, targetExcelKeyIndex);
        //  新增row
        int firstTargetDataRowIndex = firstTargetRowIndex + 1;
        sourceRowMap.values().stream().sorted(Comparator.comparing(Row::getRowNum).reversed()).forEach(sourceRow -> {
            targetSheet.shiftRows(firstTargetDataRowIndex, targetSheet.getLastRowNum(), 1);
            Row targetRow = targetSheet.createRow(firstTargetDataRowIndex);
            ExcelUtil.copyRow(sourceRow, targetRow, sourceExcelKeyIndex, targetExcelKeyIndex);
            ExcelUtil.copyRow(sourceRow, targetRow, sourceExcelDataIndex, targetExcelDataIndex);
        });
        return targetWorkbook;
    }

    /**
     * match primary key with row
     *
     * @param sheet        sheet
     * @param keyIndexList key index list
     * @param skipRowIndex row index for skip
     * @return map
     */
    private Map<String, Row> getKeyRowMap(Sheet sheet, List<Integer> keyIndexList, int skipRowIndex) {
        Map<String, Row> sourceRowMap = new HashedMap<>();
        for (Row row : ExcelUtil.listRow(sheet)) {
            if (row == null || row.getRowNum() == skipRowIndex) {
                continue;
            }
            boolean exist = true;
            String[] array = new String[keyIndexList.size()];
            for (int i = 0; i < keyIndexList.size(); i++) {
                Integer excelKeyIndex = keyIndexList.get(i);
                Cell cell = row.getCell(excelKeyIndex);
                exist &= ExcelUtil.existCell(cell);
                array[i] = ExcelUtil.getStringValue(cell);
            }
            String key = String.join("_", array).replaceAll(" ", "");
            if (exist) {
                sourceRowMap.put(key, row);
            }
        }
        return sourceRowMap;
    }

    private void writeRow(Sheet targetSheet, Map<String, Row> sourceRowMap, List<Integer> sourceExcelKeyIndex, List<Integer> sourceExcelDataIndex, List<Integer> targetExcelDataIndex, List<Integer> targetExcelKeyIndex) {
        for (Row targetRow : ExcelUtil.listRow(targetSheet)) {
            if (targetRow == null) {
                continue;
            }
            String[] array = new String[targetExcelKeyIndex.size()];
            for (int i = 0; i < targetExcelKeyIndex.size(); i++) {
                Integer excelKeyIndex = targetExcelKeyIndex.get(i);
                Cell cell = targetRow.getCell(excelKeyIndex);
                array[i] = ExcelUtil.getStringValue(cell);
            }
            String key = String.join("_", array).replaceAll(" ", "");
            Row sourceRow = sourceRowMap.get(key);
            if (sourceRow != null) {
                sourceRowMap.remove(key);
                for (int i = 0; i < sourceExcelDataIndex.size(); i++) {
                    ExcelUtil.copyRow(sourceRow, targetRow, sourceExcelKeyIndex, targetExcelKeyIndex);
                    ExcelUtil.copyRow(sourceRow, targetRow, sourceExcelDataIndex, targetExcelDataIndex);
                }
            }
        }
    }


}
