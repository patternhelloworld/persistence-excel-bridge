package io.github.patternknife.pxbsample.domain.exceldbimpl.processor;

public enum ExcelClinicTaskColumnMapping {
    NAME(0, "name", "Name", "name"),
    PHONE(1, "phone_number", "Phone", "phoneNumber");

    private final int columnNumber;
    private final String clinicTableColumnName;
    private final String excelColumnName;
    private final String entityColumnName;

    ExcelClinicTaskColumnMapping(int columnNumber, String clinicTableColumnName, String excelColumnName, String entityColumnName) {
        this.columnNumber = columnNumber;
        this.clinicTableColumnName = clinicTableColumnName;
        this.excelColumnName = excelColumnName;
        this.entityColumnName = entityColumnName;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public String getClinicTableColumnName() {
        return clinicTableColumnName;
    }

    public String getExcelColumnName() {
        return excelColumnName;
    }

    public String getEntityColumnName() {
        return entityColumnName;
    }
}

