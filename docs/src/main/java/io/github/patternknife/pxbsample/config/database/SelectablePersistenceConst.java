package io.github.patternknife.pxbsample.config.database;

public enum SelectablePersistenceConst {

    MYSQL_8("dialect.database.config.io.github.patternknife.pxbsample.CustomMySQL8Dialect"),
    MSSQL("dialect.database.config.io.github.patternknife.pxbsample.CustomSQLServerDialect");

    private final String value;

    SelectablePersistenceConst(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
