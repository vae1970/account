package com.vae.account.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 工资表
 */
@Data
public class PayrollDTO {

    /**
     * 姓名
     */
    @ExcelProperty(index = 1)
    private String name;
    /**
     * 身份证
     */
    @ExcelProperty("身份证")
    private String IdentityCardNo;
    /**
     * 收入
     */
    @ExcelProperty("收入额")
    private Double income;
    /**
     * 养老保险
     */
    @ExcelProperty("代扣养老保险")
    private Double pensionInsurance;
    /**
     * 医疗保险
     */
    @ExcelProperty("代扣医疗保险")
    private Double medicalInsurance;
    /**
     * 失业保险
     */
    @ExcelProperty("代扣失业保险")
    private Double unemployedInsurance;
    /**
     * 、住房公积金
     */
    @ExcelProperty("代扣住房公积金")
    private Double housingFund;
    /**
     * 个税
     */
    @ExcelProperty("个税-全年累进")
    private Double personalIncomeTax;

}
