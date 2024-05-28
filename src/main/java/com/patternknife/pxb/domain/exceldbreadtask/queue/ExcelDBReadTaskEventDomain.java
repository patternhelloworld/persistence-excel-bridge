package com.patternknife.pxb.domain.exceldbreadtask.queue;

import com.patternknife.pxb.util.CustomUtils;
import lombok.Value;


@Value(staticConstructor = "of")
public class ExcelDBReadTaskEventDomain {

    Long id;
    ExcelTaskStatus status;

    Long groupId;


    Integer pageNum;
    Integer pageSize;

    Long maxId;


    String errorMessage;
    int errorCount;


    public static ExcelDBReadTaskEventDomain create() {
        return ExcelDBReadTaskEventDomain.of(null,  ExcelTaskStatus.STANDBY,
                null, null, null, null,null,  0);
    }


    public ExcelDBReadTaskEventDomain update(ExcelTaskStatus status) {
        return ExcelDBReadTaskEventDomain.of(id, status, groupId, pageNum, pageSize, maxId,
                errorMessage, errorCount);
    }

    public ExcelDBReadTaskEventDomain updateErrorMessage(String errorMessage) {

        String addedErrorMessage = "";
        if(!CustomUtils.isEmpty(this.errorMessage)){
            addedErrorMessage = this.errorMessage + " || " + errorMessage;
        }else{
            addedErrorMessage = errorMessage;
        }

        return ExcelDBReadTaskEventDomain.of(id, status, groupId, pageNum, pageSize, maxId,
                addedErrorMessage, errorCount + 1);
    }


    public boolean isStandBy(){
        return status == ExcelTaskStatus.STANDBY;
    }


    public boolean isQueueWait() {
        return status == ExcelTaskStatus.QUEUE_WAIT;
    }

    public enum ExcelTaskStatus {

        STANDBY(0),
        QUEUE_WAIT(1),
        QUEUE(2),
        PROGRESS(3),
        SUCCESS(4),
        FAILURE(5);

        private final int value;

        private ExcelTaskStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ExcelTaskStatus valueOf(int value) {
            for (ExcelTaskStatus status : ExcelTaskStatus.values()) {
                if (status.getValue() == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Wrong ExcelTaskStatus :: " + value);
        }


    }
}
