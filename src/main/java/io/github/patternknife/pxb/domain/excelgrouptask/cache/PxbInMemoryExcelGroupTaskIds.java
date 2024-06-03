package io.github.patternknife.pxb.domain.excelgrouptask.cache;

import io.github.patternknife.pxb.config.logger.module.PxbBootLogConfig;
import io.github.patternknife.pxb.domain.excelgrouptask.dao.ExcelGroupTaskRepository;
import io.github.patternknife.pxb.domain.excelgrouptask.entity.ExcelGroupTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PxbInMemoryExcelGroupTaskIds {

    private static final Logger logger = LoggerFactory.getLogger(PxbBootLogConfig.class);

    private static PxbInMemoryExcelGroupTaskIds instance;

    private List<Long> readTaskGroupIds = new ArrayList<>();
    private List<Long> writeTaskGroupIds = new ArrayList<>();

    private Boolean initializationCalled = false;

    private PxbInMemoryExcelGroupTaskIds() {
    }

    public static synchronized PxbInMemoryExcelGroupTaskIds getInstance() {
        if (instance == null) {
            instance = new PxbInMemoryExcelGroupTaskIds();
        }
        return instance;
    }

    public void initializeIds(ExcelGroupTaskRepository repository) {

        initializationCalled = true;

        List<ExcelGroupTask> tasks;

        try {
            tasks = repository.findAll();
        }catch (InvalidDataAccessResourceUsageException e){
            logger.error("'excel-group-tasks' table NOT found.");
            throw e;
        }

        if(tasks.isEmpty()){
            logger.warn("No data found in the 'excel-group-tasks' table.");
        }

        readTaskGroupIds = tasks.stream()
                .filter(task -> task.getReadOrWrite() == 1)
                .map(ExcelGroupTask::getId)
                .collect(Collectors.toList());

        writeTaskGroupIds = tasks.stream()
                .filter(task -> task.getReadOrWrite() == 2)
                .map(ExcelGroupTask::getId)
                .collect(Collectors.toList());

    }

    public List<Long> getReadTaskGroupIds() {
        if(!initializationCalled) {
            logger.error("Initialization has NOT been called even once.");
        }
        return readTaskGroupIds;
    }

    public List<Long> getWriteTaskGroupIds() {
        if(!initializationCalled) {
            logger.error("Initialization has NOT been called even once.");
        }
        return writeTaskGroupIds;
    }
}
