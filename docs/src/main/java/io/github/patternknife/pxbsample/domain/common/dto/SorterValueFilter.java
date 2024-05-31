package io.github.patternknife.pxbsample.domain.common.dto;

import lombok.Data;

@Data
public class SorterValueFilter {
    private String column;
    private Boolean asc;
}
