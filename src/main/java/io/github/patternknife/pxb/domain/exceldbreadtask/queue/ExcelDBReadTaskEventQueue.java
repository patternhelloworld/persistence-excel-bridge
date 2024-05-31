package io.github.patternknife.pxb.domain.exceldbreadtask.queue;


import io.github.patternknife.pxb.config.logger.module.PxbAsyncLogConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;


public class ExcelDBReadTaskEventQueue {

    private static final Logger logger = LoggerFactory.getLogger(PxbAsyncLogConfig.class);

    private final Queue<ExcelDBReadTaskEventDomain> queue;
    private final int queueSize;

    private ExcelDBReadTaskEventQueue(int size) {
        this.queueSize = size;
        this.queue = new LinkedBlockingQueue<>(queueSize);
    }

    public static ExcelDBReadTaskEventQueue of(int size) {
        return new ExcelDBReadTaskEventQueue(size);
    }

    // Added
    public boolean offer(ExcelDBReadTaskEventDomain excelTaskEventDomain) {
        boolean returnValue = queue.offer(excelTaskEventDomain);
        logQueueSize("Added");
        return returnValue;
    }

    // Subtracted
    public ExcelDBReadTaskEventDomain poll() {
        if (queue.size() <= 0) {
            throw new IllegalStateException("No more items in the queue.");
        }
        ExcelDBReadTaskEventDomain excelTaskEventDomain = queue.poll();
        logQueueSize("Subtracted");
        return excelTaskEventDomain;
    }

    public void initializeQueueForId(Long excelGroupId) {

        synchronized(queue) {

            Queue<ExcelDBReadTaskEventDomain> filteredQueue = queue.stream()
                    .filter(item ->
                            !item.getGroupId().equals(excelGroupId))
                    .collect(Collectors.toCollection(LinkedBlockingQueue::new));


            queue.clear();
            queue.addAll(filteredQueue);
        }

        logQueueSize("Initialized");
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

    public boolean isRemainingID(Long excelGroupId) {

        EnumSet<ExcelDBReadTaskEventDomain.ExcelTaskStatus> pendingStatuses = EnumSet.of(
                ExcelDBReadTaskEventDomain.ExcelTaskStatus.STANDBY,
                ExcelDBReadTaskEventDomain.ExcelTaskStatus.QUEUE_WAIT,
                ExcelDBReadTaskEventDomain.ExcelTaskStatus.QUEUE,
                ExcelDBReadTaskEventDomain.ExcelTaskStatus.PROGRESS
        );

        return queue.stream().anyMatch(item ->
                item.getGroupId().equals(excelGroupId) &&
                        pendingStatuses.contains(item.getStatus())
        );
    }


    private void logQueueSize(String actionStr) {
        logger.info("Queue Status (" + actionStr + ") : {\"totalQueueSize\":{}, \"currentQueueSize\":{}}", queueSize, size());
    }

}
