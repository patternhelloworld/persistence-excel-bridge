package com.patternknife.pxbsample.domain.clinic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClinicSearchFilter {

    // Like 검색
    private String clinicCode;
    private String name;
    private String phoneNumber;

    private String ownerName;
    private String accountYNC;

    private Long maxId;

    private Boolean latitudeOrLongitudeNotInserted;

}
