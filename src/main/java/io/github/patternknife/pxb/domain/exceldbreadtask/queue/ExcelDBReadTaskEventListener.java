package io.github.patternknife.pxb.domain.exceldbreadtask.queue;

import io.github.patternknife.pxb.config.response.error.exception.data.ExcelDBReadTaskNotFoundException;
import io.github.patternknife.pxb.domain.exceldbreadtask.dao.ExcelDBReadTaskRepositorySupport;
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
    public void onEvent(ExcelDBReadTaskEventDomain excelTaskEventDomain) throws ExcelDBReadTaskNotFoundException {

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

    private ExcelDBReadTaskEventDomain updateStatus(ExcelDBReadTaskEventDomain excelTaskEventDomain, ExcelDBReadTaskEventDomain.ExcelTaskStatus status) throws ExcelDBReadTaskNotFoundException {
        ExcelDBReadTaskEventDomain.ExcelTaskStatus beforeStatus = excelTaskEventDomain.getStatus();
        ExcelDBReadTaskEventDomain updatedExcelTaskEventDomain = excelTaskEventDomain.update(status);

        excelDBReadTaskRepositorySupport.update(updatedExcelTaskEventDomain);

        return updatedExcelTaskEventDomain;
    }
}
