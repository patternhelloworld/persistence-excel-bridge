package com.patternknife.pxb.domain.exceldbreadtask.queue;

import com.patternknife.pxb.domain.exceldbreadtask.dao.ExcelDBReadTaskRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ExcelDBReadTaskEventListener {

    private final ExcelDBReadTaskEventQueue eventQueue;
    private final ExcelDBReadTaskRepositorySupport excelDBReadTaskRepositorySupport;

    // Instantly called after 'publish'
    @EventListener
    public void onEvent(ExcelDBReadTaskEventDomain excelTaskEventDomain) {

        if (!excelTaskEventDomain.isStandBy()) {
            return;
        }

        while (eventQueue.isFull()) {
            if (!excelTaskEventDomain.isQueueWait()) {
                excelTaskEventDomain = updateStatus(excelTaskEventDomain, ExcelDBReadTaskEventDomain.ExcelTaskStatus.QUEUE_WAIT);
            }
        }
        excelTaskEventDomain = updateStatus(excelTaskEventDomain, ExcelDBReadTaskEventDomain.ExcelTaskStatus.QUEUE);
        eventQueue.offer(excelTaskEventDomain);
    }

    private ExcelDBReadTaskEventDomain updateStatus(ExcelDBReadTaskEventDomain excelTaskEventDomain, ExcelDBReadTaskEventDomain.ExcelTaskStatus status) {
        ExcelDBReadTaskEventDomain.ExcelTaskStatus beforeStatus = excelTaskEventDomain.getStatus();
        ExcelDBReadTaskEventDomain updatedExcelTaskEventDomain = excelTaskEventDomain.update(status);

        excelDBReadTaskRepositorySupport.update(updatedExcelTaskEventDomain);

        return updatedExcelTaskEventDomain;
    }
}
