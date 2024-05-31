package io.github.patternknife.pxb.unit.excelgrouptask;

import io.github.patternknife.pxb.domain.exceldbprocessor.maxid.ExcelDBMaxIdRes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExcelDBMaxIdResExtended extends ExcelDBMaxIdRes {
    public Long id;
}
