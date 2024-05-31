package io.github.patternknife.pxb.domain.exceldbwritetask.queue;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


/*
 *   When an event object is created, it is published.
 */
@Component
@RequiredArgsConstructor
public class ExcelDBWriteEventPublisher {
    private final ApplicationEventPublisher publisher;

    public void publish(ExcelDBWriteTaskEventDomain excelTaskEventDomain) {
        publisher.publishEvent(excelTaskEventDomain);
    }
}
