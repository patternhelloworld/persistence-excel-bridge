package com.patternknife.pxbsample.domain.clinic.enums;

public enum ClinicStatusConst {
    운영(1),
    폐업(2),
    중지(3);

    private final Integer value;

    ClinicStatusConst(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static ClinicStatusConst valueOf(Integer value) {
        for (ClinicStatusConst clinicStatusConst : ClinicStatusConst.values()) {
            if (clinicStatusConst.getValue().equals(value)) {
                return clinicStatusConst;
            }
        }
        throw new IllegalArgumentException("확인되지 않은 병원 Status 입니다. :: " + value);
    }
}