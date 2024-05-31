package io.github.patternknife.pxb.domain.exceldbprocessor.processor;

import io.github.patternknife.pxb.domain.exceldbwritetask.queue.ExcelDBWriteTaskEventDomain;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelDBWriteProcessor extends ExcelDBProcessor {

    void validateColumnsBeforeDBWrite(Workbook workbook);

    ExcelDBWriteTaskEventDomain updateTableFromExcel(ExcelDBWriteTaskEventDomain excelTaskEventDomain);

    ExcelDBWriteTaskEventDomain logNotUpdatedInfo(ExcelDBWriteTaskEventDomain excelDBWriteTaskEventDomain,
                                                  String msg, int rowIndex, Boolean isWholeRowFailed);

}
