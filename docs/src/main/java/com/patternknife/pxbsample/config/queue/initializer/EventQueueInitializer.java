package com.patternknife.pxbsample.config.queue.initializer;


import com.patternknife.pxb.domain.exceldbreadtask.queue.ExcelDBReadTaskEventQueue;
import com.patternknife.pxb.domain.exceldbwritetask.queue.ExcelDBWriteTaskEventQueue;
import com.patternknife.pxbsample.domain.exceldbimpl.cache.ExcelDBReadInMemoryData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class EventQueueInitializer {
    @Bean
    public ExcelDBWriteTaskEventQueue transactionClinicEventQueue() {
        return ExcelDBWriteTaskEventQueue.of(1_000);
    }

    @Bean
    public ExcelDBReadTaskEventQueue transactionDBReadEventQueue() {
        /*
         * This code sets the capacity of the event queue (ExcelDBReadTaskEventQueue).
         * Here, 1,000 represents the maximum number of events that can be stored in the event queue.
         * Therefore, once the capacity of the event queue is determined,
         * new events will be added to the queue whenever they occur.
         * If the capacity is exceeded, new events will not be added.
         */
        return ExcelDBReadTaskEventQueue.of(1_000);
    }
    @Bean
    public ExcelDBReadInMemoryData excelDBReadTaskData() {
        return ExcelDBReadInMemoryData.of();
    }
}
