package com.vae.account.dto;

import lombok.Data;

/**
 * @author vae
 */
@Data
public class ExcelCellDTO {

    private String key;

    private Integer index;

    private Object data;

    private Boolean primaryKey;

}
