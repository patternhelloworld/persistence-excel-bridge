package com.patternknife.pxbsample.config.database;

public enum SelectablePersistenceConst {

    MYSQL_8("com.patternknife.pxbsample.config.database.dialect.CustomMySQL8Dialect"),
    MSSQL("com.patternknife.pxbsample.config.database.dialect.CustomSQLServerDialect");

    private final String value;

    SelectablePersistenceConst(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
