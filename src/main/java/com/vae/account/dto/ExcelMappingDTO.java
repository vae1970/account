package com.vae.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author vae
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcelMappingDTO {

    private String name;

    private List<ExcelMappingItemDTO> list;

}
