package com.patternknife.pxb.domain.exceldbwritetask.bo.factory;

import com.patternknife.pxb.domain.exceldbwritetask.bo.IExcelDBWriteTaskBO;

public interface IExcelDBWriteBOFactory {
    IExcelDBWriteTaskBO getBO(Long excelGroupId) throws IllegalStateException;
}
