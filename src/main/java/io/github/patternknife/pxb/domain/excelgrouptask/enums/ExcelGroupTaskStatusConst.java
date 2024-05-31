package io.github.patternknife.pxb.domain.excelgrouptask.enums;

public enum ExcelGroupTaskStatusConst {

    NOT_STARTED(0),
    IN_PROGRESS(1),
    COMPLETED(2);

    private final Integer value;

    ExcelGroupTaskStatusConst(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
