package com.patternknife.pxbsample.domain.clinic.dto;

import com.patternknife.pxbsample.domain.clinic.entity.Clinic;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class ClinicReqDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateOrUpdateOne {

        @NotEmpty(message = "병원 이름은 빈 값이면 안됩니다.")
        private String name;

        private String phoneNumber;



        @Builder
        public CreateOrUpdateOne(String name,  String phoneNumber) {
            this.name = name;
            this.phoneNumber = phoneNumber;
        }


        public Clinic toEntity() {
            return Clinic.builder()
                    .name(this.name)
                    .phoneNumber(this.phoneNumber)
                    .build();
        }
    }

}
