package com.vae.account.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author vae
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExcelMapping extends BaseEntity {

    private String name;

}

