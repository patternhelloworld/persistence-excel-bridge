package com.patternknife.pxb.domain.excelgrouptask.dto;

import com.patternknife.pxb.domain.excelgrouptask.entity.ExcelGroupTask;
import com.patternknife.pxb.domain.excelgrouptask.enums.ExcelGroupTaskStatusConst;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ExcelGroupTaskReqDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateReq {

        @NotNull(message = "The number of rows per task cannot be empty.")
        private Integer rowCountPerTask;
        private String description;
        private Long id;

        public CreateReq(Integer rowCountPerTask, String description, Long id) {
            this.rowCountPerTask = rowCountPerTask;
            this.description = description;
            this.id = id;
        }

        public ExcelGroupTask toEntity() {
            return ExcelGroupTask.builder()
                    .rowCountPerTask(this.rowCountPerTask)
                    .description(this.description)
                    .id(this.id)
                    .status(ExcelGroupTaskStatusConst.NOT_STARTED.getValue())
                    .build();
        }
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateStatusReq {

        @NotNull(message = "The status cannot be empty.")
        private Integer status;

        public UpdateStatusReq(Integer status) {
            this.status = status;
        }

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateReq {

        @NotNull(message = "The number of rows per task cannot be empty.")
        private Integer rowCountPerTask;

        private String description;

        public UpdateReq(Integer rowCountPerTask, String description) {
            this.rowCountPerTask = rowCountPerTask;
            this.description = description;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProductSalesGroupTaskCreateReq {

        @NotNull(message = "ID cannot be empty.")
        private Long id;

        private Boolean yellowColorCellUpdate;
        private Boolean onlyEmptyColUpdate;
        private Boolean onlyNewRowInsert;

        public ProductSalesGroupTaskCreateReq(Long id, Boolean yellowColorCellUpdate, Boolean onlyEmptyColUpdate, Boolean onlyNewRowInsert) {
            this.id = id;
            this.yellowColorCellUpdate = yellowColorCellUpdate;
            this.onlyEmptyColUpdate = onlyEmptyColUpdate;
            this.onlyNewRowInsert = onlyNewRowInsert;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DBWriteGroupTaskCreateReq {

        @NotNull(message = "ID cannot be empty.")
        private Long id;

        private Boolean yellowColorCellUpdate;
        private Boolean onlyEmptyColUpdate;
        private Boolean onlyNewRowInsert;

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DBReadGroupTaskCreateReq {

        @NotNull(message = "ID cannot be empty.")
        private Long id;

        public DBReadGroupTaskCreateReq(Long id) {
            this.id = id;
        }
    }


}
