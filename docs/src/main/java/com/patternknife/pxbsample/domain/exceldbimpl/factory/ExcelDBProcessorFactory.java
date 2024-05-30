package com.patternknife.pxbsample.domain.exceldbimpl.factory;

import com.patternknife.pxb.domain.exceldbprocessor.factory.IExcelDBProcessorFactory;
import com.patternknife.pxb.domain.exceldbprocessor.processor.ExcelDBProcessor;
import com.patternknife.pxbsample.domain.exceldbimpl.processor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExcelDBProcessorFactory implements IExcelDBProcessorFactory {

    private final ApplicationContext applicationContext;
    private final Map<Long, Class<? extends ExcelDBProcessor>> processorMap = new HashMap<>();

    @Autowired
    public ExcelDBProcessorFactory(ApplicationContext applicationContext) {

        this.applicationContext = applicationContext;

        // You can separate this same class into Read and Write if you'd like.
        // DB Write
        processorMap.put(1L, ClinicExcelDBReadWriteProcessor.class);
        // DB Read
        processorMap.put(55L, ClinicExcelDBReadWriteProcessor.class);

    }

    @Override
    public ExcelDBProcessor getProcessor(Long id) {
        Class<? extends ExcelDBProcessor> processorClass = processorMap.get(id);
        if (processorClass != null) {
            return applicationContext.getBean(processorClass);
        }
        throw new IllegalStateException("No processor available for task id: " + id);
    }

}

