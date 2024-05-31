package io.github.patternknife.pxb.domain.exceldbprocessor.factory;

import io.github.patternknife.pxb.domain.exceldbprocessor.processor.ExcelDBProcessor;

public interface IExcelDBProcessorFactory {
    ExcelDBProcessor getProcessor(Long excelGroupId) throws IllegalStateException;
}
