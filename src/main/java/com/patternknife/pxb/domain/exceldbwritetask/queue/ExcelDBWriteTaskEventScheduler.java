package com.patternknife.pxb.domain.exceldbwritetask.queue;

import com.patternknife.pxb.domain.exceldbwritetask.bo.factory.IExcelDBWriteBOFactory;
import com.patternknife.pxb.domain.exceldbwritetask.dao.ExcelDBWriteTaskRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/*
 *
 * */
@Component
@RequiredArgsConstructor
public class ExcelDBWriteTaskEventScheduler {
    private final ExcelDBWriteTaskEventQueue eventQueue;
    private final ExcelDBWriteTaskRepositorySupport excelDBWriteTaskRepositorySupport;
    private final IExcelDBWriteBOFactory iExcelDBWriteBOFactory;

    @Async("taskScheduler")
    @Scheduled(fixedRate = 1000)
    public void schedule() {
        new ExcelDBWriteTaskEventWorker(eventQueue, excelDBWriteTaskRepositorySupport, iExcelDBWriteBOFactory)
            .run();
    }
}
