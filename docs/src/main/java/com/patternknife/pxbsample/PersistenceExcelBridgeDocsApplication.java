package com.patternknife.pxbsample;

import com.patternknife.pxb.domain.excelgrouptask.dao.ExcelGroupTaskRepository;
import com.patternknife.pxb.domain.excelgrouptask.cache.InMemoryExcelGroupTasks;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


@SpringBootApplication(scanBasePackages =  {"com.patternknife.pxbsample", "com.patternknife.pxb"})
public class PersistenceExcelBridgeDocsApplication {

    private final ExcelGroupTaskRepository excelGroupTaskRepository;

    public PersistenceExcelBridgeDocsApplication(ExcelGroupTaskRepository excelGroupTaskRepository) {
        this.excelGroupTaskRepository = excelGroupTaskRepository;
    }

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        InMemoryExcelGroupTasks service = InMemoryExcelGroupTasks.getInstance();
        service.initialize(excelGroupTaskRepository);
    }

    public static void main(String[] args) {
        SpringApplication.run(PersistenceExcelBridgeDocsApplication.class, args);
    }

}
