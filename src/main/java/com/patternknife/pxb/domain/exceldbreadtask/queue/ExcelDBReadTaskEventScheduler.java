package com.patternknife.pxb.domain.exceldbreadtask.queue;


import com.patternknife.pxb.domain.exceldbreadtask.bo.ExcelDBReadSchedulerBO;
import com.patternknife.pxb.domain.exceldbreadtask.dao.ExcelDBReadTaskRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ExcelDBReadTaskEventScheduler {

    private final ExcelDBReadTaskEventQueue eventQueue;
    private final ExcelDBReadTaskRepositorySupport excelDBReadTaskRepositorySupport;
    private final ExcelDBReadSchedulerBO excelDBReadSchedulerBO;

    @Async("taskScheduler")
    @Scheduled(fixedRate = 5000)
    public void schedule() {
        new ExcelDBReadTaskEventWorker(eventQueue, excelDBReadTaskRepositorySupport, excelDBReadSchedulerBO)
                .run();
    }

}
