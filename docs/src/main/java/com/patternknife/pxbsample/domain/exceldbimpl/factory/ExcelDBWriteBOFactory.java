package com.patternknife.pxbsample.domain.exceldbimpl.factory;


import com.patternknife.pxb.domain.exceldbwritetask.bo.IExcelDBWriteTaskBO;
import com.patternknife.pxb.domain.exceldbwritetask.bo.factory.IExcelDBWriteBOFactory;
import com.patternknife.pxbsample.domain.exceldbimpl.bo.ExcelDBWriteClinicTaskBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExcelDBWriteBOFactory implements IExcelDBWriteBOFactory {

    private final ApplicationContext applicationContext;
    private final Map<Long, Class<? extends IExcelDBWriteTaskBO>> boMap = new HashMap<>();

    @Autowired
    public ExcelDBWriteBOFactory(ApplicationContext applicationContext) {

        this.applicationContext = applicationContext;

        // DB Write
        boMap.put(1L, ExcelDBWriteClinicTaskBO.class);

    }

    @Override
    public IExcelDBWriteTaskBO getBO(Long excelGroupTaskId) throws IllegalStateException {
        Class<? extends IExcelDBWriteTaskBO> boClass = boMap.get(excelGroupTaskId);
        if (boClass != null) {
            return applicationContext.getBean(boClass);
        }
        throw new IllegalStateException("No bo available for task id: " + excelGroupTaskId);
    }



}

