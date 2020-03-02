package com.vae.account.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 工资模板表
 */
@Data
public class PayrollTemplateDTO {

    /**
     * 姓名
     */
    @ExcelProperty("姓名")
    private String name;
    /**
     * 身份证
     */
    @ExcelProperty("*证照号码")
    private String IdentityCardNo;
    /**
     * 收入
     */
    @ExcelProperty("*本期收入")
    private Double income;
    /**
     * 养老保险
     */
    @ExcelProperty("基本养老保险费")
    private Double pensionInsurance;
    /**
     * 医疗保险
     */
    @ExcelProperty("基本医疗保险费")
    private Double medicalInsurance;
    /**
     * 失业保险
     */
    @ExcelProperty("失业保险费")
    private Double unemployedInsurance;
    /**
     * 、住房公积金
     */
    @ExcelProperty("住房公积金")
    private Double housingFund;

}
