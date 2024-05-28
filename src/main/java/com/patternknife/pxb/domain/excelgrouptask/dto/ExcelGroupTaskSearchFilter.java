package com.patternknife.pxb.domain.excelgrouptask.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExcelGroupTaskSearchFilter {

    private String description;

    private Long id;
}
