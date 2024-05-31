package io.github.patternknife.pxb.domain.exceldbwritetask.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/*
*   Data in "LinkedBlockingQueue" will be processed by async threads implementing Runnable.
* */
@Slf4j
public class ExcelDBWriteTaskEventQueue {

    private final Queue<ExcelDBWriteTaskEventDomain> queue;
    private final int queueSize;

    private ExcelDBWriteTaskEventQueue(int size) {
        this.queueSize = size;
        this.queue = new LinkedBlockingQueue<>(queueSize);
    }

    public static ExcelDBWriteTaskEventQueue of(int size) {
        return new ExcelDBWriteTaskEventQueue(size);
    }

    // added to "LinkedBlockingQueue"
    public boolean offer(ExcelDBWriteTaskEventDomain excelTaskEventDomain) {
        boolean returnValue = queue.offer(excelTaskEventDomain);
        healthCheck();
        return returnValue; // Returning 'false' means that Queue is full
    }

    // removed from "LinkedBlockingQueue"
    public ExcelDBWriteTaskEventDomain poll() {
        if (queue.size() <= 0) {
            throw new IllegalStateException("No events in the queue !");
        }
        ExcelDBWriteTaskEventDomain excelTaskEventDomain = queue.poll();
        healthCheck();
        return excelTaskEventDomain;
    }

    private int size() {
        return queue.size();
    }

    public boolean isFull() {
        return size() == queueSize;
    }

    public boolean isRemaining() {
        return size() > 0;
    }

    private void healthCheck() {
        log.info("{\"totalQueueSize\":{}, \"currentQueueSize\":{}}", queueSize, size());
    }
}
