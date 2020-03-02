package com.vae.account.enums;

import com.vae.account.dto.PayrollDTO;
import com.vae.account.dto.PayrollTemplateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author vae
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum  ExcelConvertTypeEnums {

    PayrollTemplate(PayrollDTO.class, PayrollTemplateDTO.class);

    private Class sourceAnaysisClass;
    private Class targetAnaysisClass;

}
