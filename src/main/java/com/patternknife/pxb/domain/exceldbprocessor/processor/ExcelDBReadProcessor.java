package com.patternknife.pxb.domain.exceldbprocessor.processor;

import com.patternknife.pxb.domain.exceldbprocessor.cache.IExcelDBReadInMemoryData;
import com.patternknife.pxb.domain.exceldbprocessor.maxid.ExcelDBMaxIdRes;
import com.patternknife.pxb.domain.exceldbreadtask.queue.ExcelDBReadTaskEventDomain;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface ExcelDBReadProcessor extends ExcelDBProcessor {

    /*
    *  To get metadata such as maxId for "read"
    * */
    Page<? extends ExcelDBMaxIdRes> snapshotDBRead(int pageSize) throws Exception;

    void cacheDBReadToInMemory(ExcelDBReadTaskEventDomain excelDBReadTaskEventDomain, IExcelDBReadInMemoryData excelDBReadInMemoryData) throws Exception;

    void flushInMemoryToExcelFile(Long excelGroupId, IExcelDBReadInMemoryData excelDBReadInMemoryData) throws IOException;

    default void createCell(Row row, int column, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
    }

}
