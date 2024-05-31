package io.github.patternknife.pxb.domain.exceldbreadtask.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExcelDBReadTaskSearchFilter {

    private Integer status;
}
