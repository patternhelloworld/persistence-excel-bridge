package com.patternknife.pxb.domain.excelgrouptask.dto;

import com.patternknife.pxb.domain.excelgrouptask.entity.ExcelGroupTask;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ExcelGroupTaskResDTO {

    @Getter
    public static class CreateOrUpdateRes {

        private Long id;

        public CreateOrUpdateRes(ExcelGroupTask excelGroupTask) {
            this.id = excelGroupTask.getId();
        }
    }

    @Getter
    public static class OneRes {

        private Long id;
        private Integer status;

        private Integer rowCountPerTask;
        private Integer totalRow;

        private String description;
        private String savedFileExt;
        private String originalFileName;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime excelUpdatedAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp updatedAt;

        @Setter
        private Long currentFileSize;

        @QueryProjection
        public OneRes(Long id, Integer status, Integer rowCountPerTask, Integer totalRow, String description, String savedFileExt,
                      String originalFileName, LocalDateTime excelUpdatedAt, Timestamp createdAt, Timestamp updatedAt) {

            this.id = id;
            this.status = status;
            this.rowCountPerTask = rowCountPerTask;
            this.totalRow = totalRow;
            this.description = description;
            this.savedFileExt = savedFileExt;
            this.originalFileName = originalFileName;
            this.excelUpdatedAt = excelUpdatedAt;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }

}
