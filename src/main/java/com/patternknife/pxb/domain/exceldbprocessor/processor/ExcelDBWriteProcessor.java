package com.patternknife.pxb.domain.exceldbprocessor.processor;

import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelDBWriteProcessor extends ExcelDBProcessor {

    void validateColumnsBeforeDBWrite(Workbook workbook);

}
