package com.patternknife.pxbsample;

import com.patternknife.pxb.domain.excelgrouptask.cache.PxbInMemoryExcelGroupTaskIds;
import com.patternknife.pxb.domain.excelgrouptask.dao.ExcelGroupTaskRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;


@SpringBootApplication(scanBasePackages =  {"com.patternknife.pxbsample", "com.patternknife.pxb"})
public class PersistenceExcelBridgeDocsApplication {

    private final ExcelGroupTaskRepository excelGroupTaskRepository;

    public PersistenceExcelBridgeDocsApplication(ExcelGroupTaskRepository excelGroupTaskRepository) {
        this.excelGroupTaskRepository = excelGroupTaskRepository;
    }

    @PostConstruct
    void init() {
        PxbInMemoryExcelGroupTaskIds service = PxbInMemoryExcelGroupTaskIds.getInstance();
        service.initializeIds(excelGroupTaskRepository);
    }

    public static void main(String[] args) {
        SpringApplication.run(PersistenceExcelBridgeDocsApplication.class, args);
    }

}
