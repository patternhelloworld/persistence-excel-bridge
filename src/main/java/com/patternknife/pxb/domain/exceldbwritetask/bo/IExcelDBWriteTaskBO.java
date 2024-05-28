package com.patternknife.pxb.domain.exceldbwritetask.bo;

import com.patternknife.pxb.domain.exceldbwritetask.queue.ExcelDBWriteTaskEventDomain;

public interface IExcelDBWriteTaskBO {
    ExcelDBWriteTaskEventDomain updateTableFromExcel(ExcelDBWriteTaskEventDomain excelTaskEventDomain);
    ExcelDBWriteTaskEventDomain logNotUpdatedInfo(ExcelDBWriteTaskEventDomain excelDBWriteTaskEventDomain,
                                                  String msg, int rowIndex, Boolean isWholeRowFailed);
}
