package com.vae.account.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 映射关系
 * @author vae
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExcelMappingItem extends BaseEntity {

    private Long excelMappingId;

    private String sourceKey;

    private String targetKey;

    private boolean primaryKey;

}
