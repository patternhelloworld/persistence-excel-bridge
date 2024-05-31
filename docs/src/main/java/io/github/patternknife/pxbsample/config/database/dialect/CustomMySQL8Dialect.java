package io.github.patternknife.pxbsample.config.database.dialect;


import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.MySQLDialect;


public class CustomMySQL8Dialect extends MySQLDialect {

    @Override
    public void initializeFunctionRegistry(FunctionContributions functionContributions) {
        super.initializeFunctionRegistry(functionContributions);
    }

    // 'group_concat' & 'date_format' exist, so they don't need to be implemented

}
