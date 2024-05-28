package com.patternknife.pxbsample.domain.clinic.dto;


import com.patternknife.pxb.domain.excelgrouptask.entity.ExcelGroupTask;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

public class ExcelClinicGroupTaskDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateReq {
        private Long id;
        private Integer status;
        private Integer rowCountPerTask;
        private Integer totalRow;
        private String description;
        private String savedFileExt;
        private String originalFileName;

        @Builder
        public CreateReq(Long id, Integer status, Integer rowCountPerTask, Integer totalRow,
                         String description, String savedFileExt, String originalFileName) {
            this.id = id;
            this.status = status;
            this.rowCountPerTask = rowCountPerTask;
            this.totalRow = totalRow;
            this.description = description;
            this.savedFileExt = savedFileExt;
            this.originalFileName = originalFileName;
        }

        public ExcelGroupTask toEntity() {
            return ExcelGroupTask.builder()
                    .id(this.id)
                    .status(this.status)
                    .rowCountPerTask(this.rowCountPerTask)
                    .totalRow(this.totalRow)
                    .description(this.description)
                    .savedFileExt(this.savedFileExt)
                    .originalFileName(this.originalFileName)
                    .build();
        }
    }



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
        private Long excelGroupTask;
        private Long totalRow;
        private String description;
        private String savedFileExt;
        private String originalFileName;
        private Timestamp createdAt;
        private Timestamp updatedAt;

        @QueryProjection
        public OneRes(Long id, Integer status, Long excelGroupTask, Long totalRow,
                      String description, String savedFileExt, String originalFileName,
                      Timestamp createdAt, Timestamp updatedAt) {
            this.id = id;
            this.status = status;
            this.excelGroupTask = excelGroupTask;
            this.totalRow = totalRow;
            this.description = description;
            this.savedFileExt = savedFileExt;
            this.originalFileName = originalFileName;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

    }


}
