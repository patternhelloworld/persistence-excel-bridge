package io.github.patternknife.pxb.domain.exceldbwritetask.queue;


import io.github.patternknife.pxb.config.logger.module.PxbAsyncLogConfig;
import io.github.patternknife.pxb.config.response.error.CustomExceptionUtils;
import io.github.patternknife.pxb.domain.exceldbwritetask.bo.ExcelDBWriteSchedulerBO;
import io.github.patternknife.pxb.domain.exceldbwritetask.dao.ExcelDBWriteTaskRepositorySupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
 *
 * */
@Slf4j
@RequiredArgsConstructor
public class ExcelDBWriteTaskEventWorker implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PxbAsyncLogConfig.class);

    private final ExcelDBWriteTaskEventQueue eventQueue;
    private final ExcelDBWriteTaskRepositorySupport excelDBWriteTaskRepositorySupport;
    private final ExcelDBWriteSchedulerBO excelDBWriteSchedulerBO;

    @Override
    public void run() {
        if (eventQueue.isRemaining()) {
            ExcelDBWriteTaskEventDomain excelDBWriteTaskEventDomain = eventQueue.poll();
            try {
                excelDBWriteTaskEventDomain = updateStatus(excelDBWriteTaskEventDomain, ExcelDBWriteTaskEventDomain.ExcelTaskStatus.PROGRESS);
                excelDBWriteTaskEventDomain = processing(excelDBWriteTaskEventDomain);
                markSuccess(excelDBWriteTaskEventDomain);
            } catch (Exception e) {
                excelDBWriteTaskEventDomain = excelDBWriteTaskEventDomain.updateErrorMessage(CustomExceptionUtils.getAllCauses(e) + " / An unhandled error occurred. For more details, please contact the administrator as specific information cannot be disclosed due to security reasons.");
                handlingInCaseOfFailure(excelDBWriteTaskEventDomain);
                logger.error(excelDBWriteTaskEventDomain.getId() + " (ID) :  " + CustomExceptionUtils.getAllCauses(e) + " / " + CustomExceptionUtils.getFirstTwoStackTraces(e));
            }
        }
    }

    private ExcelDBWriteTaskEventDomain processing(ExcelDBWriteTaskEventDomain excelDBWriteTaskEventDomain) {
        return excelDBWriteSchedulerBO.updateTableFromExcel(excelDBWriteTaskEventDomain);
    }

    private void markSuccess(ExcelDBWriteTaskEventDomain excelDBWriteTaskEventDomain) {
        updateStatus(excelDBWriteTaskEventDomain, ExcelDBWriteTaskEventDomain.ExcelTaskStatus.SUCCESS);
    }

    private void handlingInCaseOfFailure(ExcelDBWriteTaskEventDomain excelDBWriteTaskEventDomain) {
        updateStatus(excelDBWriteTaskEventDomain, ExcelDBWriteTaskEventDomain.ExcelTaskStatus.FAILURE);
    }

    private ExcelDBWriteTaskEventDomain updateStatus(ExcelDBWriteTaskEventDomain excelDBWriteTaskEventDomain, ExcelDBWriteTaskEventDomain.ExcelTaskStatus status) {
        ExcelDBWriteTaskEventDomain updatedExcelDBWriteTaskEventDomain = excelDBWriteTaskEventDomain.update(status);
        excelDBWriteTaskRepositorySupport.update(updatedExcelDBWriteTaskEventDomain);
        return updatedExcelDBWriteTaskEventDomain;
    }
}
