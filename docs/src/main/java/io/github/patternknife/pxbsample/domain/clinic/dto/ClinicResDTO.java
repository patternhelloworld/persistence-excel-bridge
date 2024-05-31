package io.github.patternknife.pxbsample.domain.clinic.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.patternknife.pxb.domain.exceldbprocessor.maxid.ExcelDBMaxIdRes;
import io.github.patternknife.pxbsample.domain.clinic.entity.Clinic;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ClinicResDTO {

    @Getter
    public static class Id {

        private Long id;

        public Id(Clinic clinic) {
            this.id = clinic.getId();
        }
    }

    @Getter
    public static class One {

        private Long id;

        private String name;
        private String phoneNumber;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp updatedAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime deletedAt;

        @QueryProjection
        public One(Long id, String name, String phoneNumber,
                   Timestamp createdAt, Timestamp updatedAt, LocalDateTime deletedAt) {

            this.id = id;
            this.name = name;
            this.phoneNumber = phoneNumber;

            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.deletedAt = deletedAt;

        }
    }

    @Getter
    public static class OneDetails extends ExcelDBMaxIdRes {

        private String name;
        private String phoneNumber;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Timestamp updatedAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime deletedAt;

        @QueryProjection
        public OneDetails(Long id,  String name, String phoneNumber, Timestamp createdAt, Timestamp updatedAt, LocalDateTime deletedAt) {

            this.id = id;
            this.name = name;
            this.phoneNumber = phoneNumber;

            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.deletedAt = deletedAt;
        }
    }


    @Getter
    public static class CommonStatistics {
        private String monthYear;
        private Long counts;

        @QueryProjection
        public CommonStatistics(String monthYear, Long counts) {
            this.monthYear = monthYear;
            this.counts = counts;
        }
    }


    @Getter
    public static class DeletedOrRestoredClinicInfo {
        private Long id;
        private Long adminId;

        public DeletedOrRestoredClinicInfo(Long id, Long adminId) {
            this.id = id;
            this.adminId = adminId;
        }
    }

    @Getter
    public static class IdAndFullAddress {
        private Long id;
        private String fullAddress;

        @QueryProjection
        public IdAndFullAddress(Long id, String fullAddress) {
            this.id = id;
            this.fullAddress = fullAddress;
        }
    }

}
