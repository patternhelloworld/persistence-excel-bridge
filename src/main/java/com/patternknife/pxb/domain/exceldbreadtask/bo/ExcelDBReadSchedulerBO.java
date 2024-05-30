package com.patternknife.pxb.domain.exceldbreadtask.bo;

import com.patternknife.pxb.config.logger.module.PxbAsyncLogConfig;
import com.patternknife.pxb.domain.exceldbprocessor.cache.IExcelDBReadInMemoryData;
import com.patternknife.pxb.domain.exceldbprocessor.factory.IExcelDBProcessorFactory;
import com.patternknife.pxb.domain.exceldbprocessor.processor.ExcelDBReadProcessor;
import com.patternknife.pxb.domain.exceldbreadtask.queue.ExcelDBReadTaskEventDomain;
import com.patternknife.pxb.domain.exceldbreadtask.queue.ExcelDBReadTaskEventQueue;
import com.patternknife.pxb.domain.excelgrouptask.cache.PxbInMemoryExcelGroupTaskIds;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;


@RequiredArgsConstructor
@Service
public class ExcelDBReadSchedulerBO {

    private static final Logger logger = LoggerFactory.getLogger(PxbAsyncLogConfig.class);

    private final IExcelDBProcessorFactory iExcelDBProcessorFactory;


    private final IExcelDBReadInMemoryData excelDBReadInMemoryData;
    private final ExcelDBReadTaskEventQueue eventQueue;


    public void flushingAllIfNoRemaining() {

        for (Long taskId : PxbInMemoryExcelGroupTaskIds.getInstance().getReadTaskGroupIds()) {
            flushingIfNoRemaining(taskId);
        }
    }

    /*
     *   If no synchronized, the second thread will create the excel file again.
     * */
    private final Object lockedFlushing = new Object();
    public void flushingIfNoRemaining(Long excelGroupId){
        synchronized (lockedFlushing) {
            if (!eventQueue.isRemainingID(excelGroupId) && excelDBReadInMemoryData.hasDataForId(excelGroupId)) {
                try {
                    logger.info("[Preparing] Bulk " + excelGroupId);

                    flushInMemoryToExcelFile(excelGroupId);
                    // This task will not let the second thread be in this If Clause.
                    excelDBReadInMemoryData.clearDataForId(excelGroupId);

                    logger.info("[Completed] Bulk " + excelGroupId);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public ExcelDBReadTaskEventDomain updateInMemoryWithDBDataInChunks(ExcelDBReadTaskEventDomain excelDBReadTaskEventDomain) throws Exception {

        ((ExcelDBReadProcessor) iExcelDBProcessorFactory.getProcessor(excelDBReadTaskEventDomain.getGroupId())).cacheDBReadToInMemory(excelDBReadTaskEventDomain, excelDBReadInMemoryData);

        return excelDBReadTaskEventDomain;
    }

    public void flushInMemoryToExcelFile(Long excelGroupId) throws IOException {

        ((ExcelDBReadProcessor) iExcelDBProcessorFactory.getProcessor(excelGroupId)).flushInMemoryToExcelFile(excelGroupId, excelDBReadInMemoryData);
    }

}
