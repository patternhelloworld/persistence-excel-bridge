package com.patternknife.pxb.domain.exceldbprocessor.cache;

public interface IExcelDBReadInMemoryData {
    Boolean hasDataForId(Long excelGroupId);
    void clearDataForId(Long excelGroupId);
}
