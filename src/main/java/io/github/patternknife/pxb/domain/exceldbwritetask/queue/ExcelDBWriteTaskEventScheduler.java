package io.github.patternknife.pxb.domain.exceldbwritetask.queue;

import io.github.patternknife.pxb.domain.exceldbwritetask.bo.ExcelDBWriteSchedulerBO;
import io.github.patternknife.pxb.domain.exceldbwritetask.dao.ExcelDBWriteTaskRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ExcelDBWriteTaskEventScheduler {

    private final ExcelDBWriteTaskEventQueue eventQueue;
    private final ExcelDBWriteTaskRepositorySupport excelDBWriteTaskRepositorySupport;
    private final ExcelDBWriteSchedulerBO excelDBWriteSchedulerBO;

    @Async("taskScheduler")
    @Scheduled(fixedRate = 1000)
    public void schedule() {
        new ExcelDBWriteTaskEventWorker(eventQueue, excelDBWriteTaskRepositorySupport, excelDBWriteSchedulerBO)
            .run();
    }
}
