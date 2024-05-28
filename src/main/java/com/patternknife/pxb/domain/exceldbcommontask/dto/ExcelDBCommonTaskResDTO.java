package com.patternknife.pxb.domain.exceldbcommontask.dto;

import com.patternknife.pxb.domain.exceldbreadtask.entity.ExcelDBReadTask;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

public class ExcelDBCommonTaskResDTO {

    @Getter
    public static class CreateOrUpdateRes {

        private Long id;

        public CreateOrUpdateRes(ExcelDBReadTask excelDBReadTask) {
            this.id = excelDBReadTask.getId();
        }
    }

    @Getter
    public static class OneRes {

        private Long id;

        private Long groupId;

        private Integer startRow;
        private Integer endRow;

        private String description;

        private Integer status;
        private String errorMessage;
        private int errorCount;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp updatedAt;


        @QueryProjection
        public OneRes(Long id, Long groupId, Integer startRow, Integer endRow, String description, Integer status,
                      String errorMessage, int errorCount, Timestamp createdAt, Timestamp updatedAt) {
            this.id = id;
            this.groupId = groupId;
            this.startRow = startRow;
            this.endRow = endRow;
            this.description = description;
            this.status = status;
            this.errorMessage = errorMessage;
            this.errorCount = errorCount;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }


    /*
    *   a progress bar with four components, summing up to 100%
    * */
    @Getter
    @AllArgsConstructor
    public static class StatusRes {

        private Long successCount;
        private Long failureCount;
        private Long progressCount;
        private Long waitingCount;
    }

    @Getter
    public static class StartEndTimestampRes {

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp startCreatedAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp endUpdatedAt;

        @QueryProjection
        public StartEndTimestampRes(Timestamp startCreatedAt, Timestamp endUpdatedAt) {
            this.startCreatedAt = startCreatedAt;
            this.endUpdatedAt = endUpdatedAt;
        }
    }

}
