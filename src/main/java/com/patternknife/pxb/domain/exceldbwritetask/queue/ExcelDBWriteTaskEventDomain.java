package com.patternknife.pxb.domain.exceldbwritetask.queue;

import com.patternknife.pxb.util.CustomUtils;
import lombok.Value;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * An object that represents the status of a single task
 */
@Value(staticConstructor = "of")
public class ExcelDBWriteTaskEventDomain {

    Long id;
    ExcelTaskStatus status;

    Long groupId;

    Boolean yellowColorCellUpdate;
    Boolean onlyEmptyColUpdate;
    Boolean onlyNewRowInsert;


    Workbook workbook;

    Integer startRow;
    Integer endRow;

    String errorMessage;
    int errorCount;


    public static ExcelDBWriteTaskEventDomain create() {
        return ExcelDBWriteTaskEventDomain.of(null,  ExcelTaskStatus.STANDBY, null,
                null, null, null, null, null, null,null, 0);
    }


    public ExcelDBWriteTaskEventDomain update(ExcelTaskStatus status) {
        return ExcelDBWriteTaskEventDomain.of(id, status, groupId, yellowColorCellUpdate, onlyEmptyColUpdate, onlyNewRowInsert,
                workbook, startRow, endRow, errorMessage, errorCount);
    }

    public ExcelDBWriteTaskEventDomain updateErrorMessage(String errorMessage) {

        String addedErrorMessage = "";
        if(!CustomUtils.isEmpty(this.errorMessage)){
            addedErrorMessage = this.errorMessage + " || " + errorMessage;
        }else{
            addedErrorMessage = errorMessage;
        }

        return ExcelDBWriteTaskEventDomain.of(id, status, groupId, yellowColorCellUpdate, onlyEmptyColUpdate, onlyNewRowInsert,
                workbook, startRow, endRow, addedErrorMessage, errorCount + 1);
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
            throw new IllegalArgumentException("Wrong ExcelTaskStatus value :: " + value);
        }


    }
}
