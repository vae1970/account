package com.vae.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author vae
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcelMappingItemDTO {

    @NotNull
    private String sourceKey;
    @NotNull
    private String targetKey;
    @NotNull
    private Boolean primaryKey;

}
