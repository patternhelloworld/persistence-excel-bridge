package com.patternknife.pxb.domain.exceldbwritetask.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExcelDBWriteTaskSearchFilter {

    private Integer status;
}
