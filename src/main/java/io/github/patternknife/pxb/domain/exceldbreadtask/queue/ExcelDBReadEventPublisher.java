package io.github.patternknife.pxb.domain.exceldbreadtask.queue;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ExcelDBReadEventPublisher {
    private final ApplicationEventPublisher publisher;

    public void publish(ExcelDBReadTaskEventDomain excelTaskEventDomain) {
        publisher.publishEvent(excelTaskEventDomain);
    }
}
