package io.github.patternknife.pxbsample.domain.clinic.enums;

public enum ClinicAddressVerifiedConst {

    검증전(0),
    검증성공(1),
    검증실패(2);

    private final Integer value;

    ClinicAddressVerifiedConst(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}