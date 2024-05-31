package io.github.patternknife.pxb.domain.exceldbreadtask.queue;


import io.github.patternknife.pxb.config.logger.module.PxbAsyncLogConfig;
import io.github.patternknife.pxb.config.response.error.CustomExceptionUtils;
import io.github.patternknife.pxb.domain.exceldbreadtask.bo.ExcelDBReadSchedulerBO;
import io.github.patternknife.pxb.domain.exceldbreadtask.dao.ExcelDBReadTaskRepositorySupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Slf4j
@RequiredArgsConstructor
public class ExcelDBReadTaskEventWorker implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PxbAsyncLogConfig.class);

    private final ExcelDBReadTaskEventQueue eventQueue;
    private final ExcelDBReadTaskRepositorySupport excelDBReadTaskRepositorySupport;
    private final ExcelDBReadSchedulerBO excelDBReadSchedulerBO;

    @Override
    public void run() {
        if (eventQueue.isRemaining()) {
            ExcelDBReadTaskEventDomain excelDbReadTaskEventDomain = eventQueue.poll();
            try {
                excelDbReadTaskEventDomain = updateStatus(excelDbReadTaskEventDomain, ExcelDBReadTaskEventDomain.ExcelTaskStatus.PROGRESS);
                excelDbReadTaskEventDomain = processing(excelDbReadTaskEventDomain);
                markSuccess(excelDbReadTaskEventDomain);
            } catch (Exception e) {
                excelDbReadTaskEventDomain = excelDbReadTaskEventDomain.updateErrorMessage(CustomExceptionUtils.getAllCauses(e) + " / Due to an unhandled error, more detailed information is not disclosed for security reasons. Please request verification from the administrator.");
                handlingInCaseOfFailure(excelDbReadTaskEventDomain);
                logger.error("ExcelDbReadTask ID : " + excelDbReadTaskEventDomain.getId() + " / " + CustomExceptionUtils.getAllCauses(e) + " / " + CustomExceptionUtils.getFirstTwoStackTraces(e));
            }
        }

        excelDBReadSchedulerBO.flushingAllIfNoRemaining();
    }

    private ExcelDBReadTaskEventDomain processing(ExcelDBReadTaskEventDomain excelDBReadTaskEventDomain) throws Exception {

        return excelDBReadSchedulerBO.updateInMemoryWithDBDataInChunks(excelDBReadTaskEventDomain);
    }


    private void markSuccess(ExcelDBReadTaskEventDomain excelDbReadTaskEventDomain) {
        updateStatus(excelDbReadTaskEventDomain, ExcelDBReadTaskEventDomain.ExcelTaskStatus.SUCCESS);
    }

    private void handlingInCaseOfFailure(ExcelDBReadTaskEventDomain excelDbReadTaskEventDomain) {
        updateStatus(excelDbReadTaskEventDomain, ExcelDBReadTaskEventDomain.ExcelTaskStatus.FAILURE);
    }

    private ExcelDBReadTaskEventDomain updateStatus(ExcelDBReadTaskEventDomain excelDbReadTaskEventDomain, ExcelDBReadTaskEventDomain.ExcelTaskStatus status) {
        ExcelDBReadTaskEventDomain updatedExcelDBReadTaskEventDomain = excelDbReadTaskEventDomain.update(status);
        excelDBReadTaskRepositorySupport.update(updatedExcelDBReadTaskEventDomain);
        return updatedExcelDBReadTaskEventDomain;
    }
}
