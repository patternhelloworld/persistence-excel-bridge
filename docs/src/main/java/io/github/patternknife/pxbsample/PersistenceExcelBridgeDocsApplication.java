package io.github.patternknife.pxbsample;

import io.github.patternknife.pxb.domain.excelgrouptask.cache.PxbInMemoryExcelGroupTaskIds;
import io.github.patternknife.pxb.domain.excelgrouptask.dao.ExcelGroupTaskRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;


@SpringBootApplication(scanBasePackages =  {"io.github.patternknife.pxbsample", "io.github.patternknife.pxb"})
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
