package com.patternknife.pxb.domain.exceldbprocessor.factory;

import com.patternknife.pxb.domain.exceldbprocessor.processor.ExcelDBProcessor;

public interface IExcelDBProcessorFactory {
    ExcelDBProcessor getProcessor(Long excelGroupId) throws IllegalStateException;
}
