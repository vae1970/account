package com.vae.account.enums;

import com.vae.account.consts.ExcelConst;
import com.vae.account.dto.ExcelMappingDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author vae
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum TypeEnums {

    /**
     * first
     */
    first(ExcelConst.first),
    /**
     * second
     */
    second(ExcelConst.second);

    private ExcelMappingDTO excelMappingDTO;

}
