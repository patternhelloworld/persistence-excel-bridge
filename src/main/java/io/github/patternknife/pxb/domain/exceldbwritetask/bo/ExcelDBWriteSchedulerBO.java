package io.github.patternknife.pxb.domain.exceldbwritetask.bo;

import io.github.patternknife.pxb.config.logger.module.PxbAsyncLogConfig;
import io.github.patternknife.pxb.domain.exceldbprocessor.factory.IExcelDBProcessorFactory;
import io.github.patternknife.pxb.domain.exceldbprocessor.processor.ExcelDBWriteProcessor;
import io.github.patternknife.pxb.domain.exceldbwritetask.queue.ExcelDBWriteTaskEventDomain;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ExcelDBWriteSchedulerBO {

    private static final Logger logger = LoggerFactory.getLogger(PxbAsyncLogConfig.class);

    private final IExcelDBProcessorFactory iExcelDBProcessorFactory;

    public ExcelDBWriteTaskEventDomain updateTableFromExcel(ExcelDBWriteTaskEventDomain excelDBWriteTaskEventDomain) {

        return ((ExcelDBWriteProcessor)iExcelDBProcessorFactory.getProcessor(excelDBWriteTaskEventDomain.getGroupId())).updateTableFromExcel(excelDBWriteTaskEventDomain);
    }

}
