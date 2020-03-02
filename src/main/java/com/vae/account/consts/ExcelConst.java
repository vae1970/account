package com.vae.account.consts;

import com.vae.account.dto.ExcelMappingDTO;
import com.vae.account.dto.ExcelMappingItemDTO;

import java.util.Arrays;

/**
 * @author vae
 */
public class ExcelConst {

    public static final ExcelMappingDTO first = ExcelMappingDTO.builder().name("第一步").list(
            Arrays.asList(
                    ExcelMappingItemDTO.builder().sourceKey("身份证").targetKey("*证照号码").primaryKey(true).build(),
                    ExcelMappingItemDTO.builder().sourceKey("姓         名").targetKey("姓名").primaryKey(false).build(),
                    ExcelMappingItemDTO.builder().sourceKey("收入额").targetKey("*本期收入").primaryKey(false).build(),
                    ExcelMappingItemDTO.builder().sourceKey("代扣养老保险").targetKey("基本养老保险费").primaryKey(false).build(),
                    ExcelMappingItemDTO.builder().sourceKey("代扣医疗保险").targetKey("基本医疗保险费").primaryKey(false).build(),
                    ExcelMappingItemDTO.builder().sourceKey("代扣失业保险").targetKey("失业保险费").primaryKey(false).build(),
                    ExcelMappingItemDTO.builder().sourceKey("代扣住房公积金").targetKey("住房公积金").primaryKey(false).build()
            )
    ).build();

    public static final ExcelMappingDTO second = ExcelMappingDTO.builder().name("第一步").list(
            Arrays.asList(
                    ExcelMappingItemDTO.builder().sourceKey("证照号码").targetKey("身份证").primaryKey(true).build(),
                    ExcelMappingItemDTO.builder().sourceKey("姓名").targetKey("姓         名").primaryKey(false).build(),
                    ExcelMappingItemDTO.builder().sourceKey("累计应补(退)税额").targetKey("个税-全年累进").primaryKey(false).build()
            )
    ).build();

}
