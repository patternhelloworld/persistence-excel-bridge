package com.patternknife.pxb.domain.exceldbwritetask.queue;

import com.patternknife.pxb.domain.exceldbwritetask.dao.ExcelDBWriteTaskRepositorySupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


/*
*
* */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExcelDBWriteTaskEventListener {

    private final ExcelDBWriteTaskEventQueue eventQueue;
    private final ExcelDBWriteTaskRepositorySupport excelDBWriteTaskRepositorySupport;

    /*
    *   called right after '.publish'
    * */
    @EventListener
    public void onEvent(ExcelDBWriteTaskEventDomain excelTaskEventDomain) {

        if (!excelTaskEventDomain.isStandBy()) {
            log.info("Transaction(id:{}) status is not STANDBY!", excelTaskEventDomain.getId());
            return;
        }

        while (eventQueue.isFull()) {
            if (!excelTaskEventDomain.isQueueWait()) {
                excelTaskEventDomain = updateStatus(excelTaskEventDomain, ExcelDBWriteTaskEventDomain.ExcelTaskStatus.QUEUE_WAIT);
            }
        }
        excelTaskEventDomain = updateStatus(excelTaskEventDomain, ExcelDBWriteTaskEventDomain.ExcelTaskStatus.QUEUE);
        eventQueue.offer(excelTaskEventDomain);
    }

    private ExcelDBWriteTaskEventDomain updateStatus(ExcelDBWriteTaskEventDomain excelTaskEventDomain, ExcelDBWriteTaskEventDomain.ExcelTaskStatus status) {
        ExcelDBWriteTaskEventDomain.ExcelTaskStatus beforeStatus = excelTaskEventDomain.getStatus();
        ExcelDBWriteTaskEventDomain updatedExcelTaskEventDomain = excelTaskEventDomain.update(status);
        log.info("{\"transactionId\": {},\"before\":\"{}\", \"after\":\"{}\"}", excelTaskEventDomain.getId(), beforeStatus, status);
        excelDBWriteTaskRepositorySupport.update(updatedExcelTaskEventDomain);

        return updatedExcelTaskEventDomain;
    }
}
