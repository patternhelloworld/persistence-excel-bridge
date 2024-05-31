package io.github.patternknife.pxb.domain.excelgrouptask.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.patternknife.pxb.config.response.error.exception.data.AlreadyInProgressException;
import io.github.patternknife.pxb.config.response.error.exception.data.ResourceNotFoundException;
import io.github.patternknife.pxb.domain.file.service.ExcelGroupTaskFileService;

import io.github.patternknife.pxb.domain.exceldbprocessor.factory.IExcelDBProcessorFactory;
import io.github.patternknife.pxb.domain.exceldbprocessor.maxid.ExcelDBMaxIdRes;
import io.github.patternknife.pxb.domain.exceldbprocessor.processor.ExcelDBProcessor;
import io.github.patternknife.pxb.domain.exceldbprocessor.processor.ExcelDBReadProcessor;
import io.github.patternknife.pxb.domain.exceldbprocessor.processor.ExcelDBWriteProcessor;
import io.github.patternknife.pxb.domain.exceldbreadtask.dao.ExcelDBReadTaskRepository;
import io.github.patternknife.pxb.domain.exceldbreadtask.entity.ExcelDBReadTask;
import io.github.patternknife.pxb.domain.exceldbreadtask.queue.ExcelDBReadEventPublisher;
import io.github.patternknife.pxb.domain.exceldbreadtask.queue.ExcelDBReadTaskEventDomain;
import io.github.patternknife.pxb.domain.exceldbreadtask.queue.ExcelDBReadTaskEventQueue;
import io.github.patternknife.pxb.domain.exceldbwritetask.dao.ExcelDBWriteTaskRepository;
import io.github.patternknife.pxb.domain.exceldbwritetask.entity.ExcelDBWriteTask;
import io.github.patternknife.pxb.domain.exceldbwritetask.queue.ExcelDBWriteEventPublisher;
import io.github.patternknife.pxb.domain.exceldbwritetask.queue.ExcelDBWriteTaskEventDomain;
import io.github.patternknife.pxb.domain.excelgrouptask.bo.ExcelGroupTaskBO;
import io.github.patternknife.pxb.domain.excelgrouptask.dao.ExcelGroupTaskRepository;
import io.github.patternknife.pxb.domain.excelgrouptask.dao.ExcelGroupTaskRepositorySupport;
import io.github.patternknife.pxb.domain.excelgrouptask.dto.ExcelGroupTaskReqDTO;
import io.github.patternknife.pxb.domain.excelgrouptask.dto.ExcelGroupTaskResDTO;
import io.github.patternknife.pxb.domain.excelgrouptask.entity.ExcelGroupTask;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ExcelGroupTaskService {


    private final ExcelGroupTaskRepositorySupport excelGroupTaskRepositorySupport;
    private final ExcelGroupTaskRepository excelGroupTaskRepository;

    private final ExcelDBWriteTaskRepository excelDBWriteTaskRepository;
    private final ExcelDBReadTaskRepository excelDBReadTaskRepository;

    private final ExcelDBWriteEventPublisher excelDBWriteEventPublisher;
    private final ExcelDBReadEventPublisher dbReadEventPublisher;

    private final IExcelDBProcessorFactory excelDBProcessorFactory;


    private final ExcelGroupTaskFileService excelGroupTaskFileService;

    private final ExcelDBReadTaskEventQueue eventQueue;

    /* 1. ExcelGroupTask */

    @Transactional(rollbackFor=Exception.class)
    public ExcelGroupTaskResDTO.CreateOrUpdateRes createExcelGroupTask(@NotNull ExcelGroupTaskReqDTO.CreateReq dto) {
        return new ExcelGroupTaskResDTO.CreateOrUpdateRes(excelGroupTaskRepository.save(dto.toEntity()));
    }

    @Transactional( rollbackFor=Exception.class)
    public ExcelGroupTaskResDTO.CreateOrUpdateRes updateExcelGroupTask(@NotNull Long id, @NotNull ExcelGroupTaskReqDTO.UpdateReq dto) {

        ExcelGroupTask excelGroupTask = null;

        try {

            if (!excelGroupTaskRepositorySupport.setExcelGroupTaskStatusInProgress(id)) {
                throw new AlreadyInProgressException("The group process has already started or is being modified. (EXCEL GROUP TASK ID : " + id + ")");
            }

            if (excelDBWriteTaskRepository.existsByGroupIdAndStatusIn(id,
                    Arrays.asList(ExcelDBWriteTaskEventDomain.ExcelTaskStatus.STANDBY.getValue(),
                            ExcelDBWriteTaskEventDomain.ExcelTaskStatus.QUEUE_WAIT.getValue(),
                            ExcelDBWriteTaskEventDomain.ExcelTaskStatus.QUEUE.getValue(), ExcelDBWriteTaskEventDomain.ExcelTaskStatus.PROGRESS.getValue()))) {
                throw new AlreadyInProgressException("There are incomplete tasks among the background individual processes of the hospital group process. (EXCEL GROUP TASK ID : " + id + ")");
            }


            excelGroupTask = excelGroupTaskRepositorySupport.findById(id);
            excelGroupTask.update(dto);

        }finally {
            excelGroupTaskRepositorySupport.setExcelGroupTaskStatusNotInProgress(id);
        }

        return new ExcelGroupTaskResDTO.CreateOrUpdateRes(excelGroupTask);
    }



    /* 2. ExcelDBWriteGroupTask */

    public Page<ExcelGroupTaskResDTO.OneRes> getExcelDBWriteGroupTasks(Boolean skipPagination, Integer pageNum, Integer pageSize,
                                                                       String excelGroupTaskSearchFilter,
                                                                       String sorterValueFilter,
                                                                       String dateRangeFilter)
            throws ResourceNotFoundException, JsonProcessingException {

        return excelGroupTaskRepositorySupport.findExcelDBWriteGroupTasksByPageFilter(skipPagination, pageNum,pageSize,
                excelGroupTaskSearchFilter, sorterValueFilter, dateRangeFilter);

    }

    public Boolean deleteExcelDBWriteGroupTask(Long id) throws IOException {

        try {

            if (!excelGroupTaskRepositorySupport.setExcelGroupTaskStatusInProgress(id)) {
                throw new AlreadyInProgressException("The group process has already started or is being modified.(EXCEL GROUP TASK ID : " + id + ")");
            }

            if (excelDBWriteTaskRepository.existsByGroupIdAndStatusIn(id,
                    Arrays.asList(ExcelDBWriteTaskEventDomain.ExcelTaskStatus.STANDBY.getValue(),
                            ExcelDBWriteTaskEventDomain.ExcelTaskStatus.QUEUE_WAIT.getValue(),
                            ExcelDBWriteTaskEventDomain.ExcelTaskStatus.QUEUE.getValue(), ExcelDBWriteTaskEventDomain.ExcelTaskStatus.PROGRESS.getValue()))) {
                throw new AlreadyInProgressException("There are incomplete tasks among the background individual processes of the hospital group process. (EXCEL GROUP TASK ID : " + id + ")");
            }

            excelDBWriteTaskRepository.deleteByGroupId(id);
            excelGroupTaskRepository.deleteById(id);

        }finally {
            try {
                excelGroupTaskRepositorySupport.setExcelGroupTaskStatusNotInProgress(id);
            }catch (Exception ignored){

            }
        }

        return Boolean.TRUE;

    }
    /*
     *   This includes an asynchronous process. @Transactional was intentionally excluded to allow immediate DB updates.
     * */
    public ExcelGroupTaskResDTO.CreateOrUpdateRes createExcelDBWriteGroupTask(ExcelGroupTaskReqDTO.DBWriteGroupTaskCreateReq dto) throws IOException {

        ExcelGroupTask excelGroupTask = null;
        Workbook workbook = null;

        Resource resource = excelGroupTaskFileService.getExcelGroupTaskExcelBinaryById(dto.getId());

        try {

            if (!excelGroupTaskRepositorySupport.setExcelGroupTaskStatusInProgress(dto.getId())) {
                throw new AlreadyInProgressException("The group process has already started or is being modified.(EXCEL GROUP TASK ID : " + dto.getId() + ")");
            }

            if(excelDBWriteTaskRepository.existsByGroupIdAndStatusIn(dto.getId(),
                    Arrays.asList(ExcelDBWriteTaskEventDomain.ExcelTaskStatus.STANDBY.getValue(),
                            ExcelDBWriteTaskEventDomain.ExcelTaskStatus.QUEUE_WAIT.getValue(),
                            ExcelDBWriteTaskEventDomain.ExcelTaskStatus.QUEUE.getValue(),
                            ExcelDBWriteTaskEventDomain.ExcelTaskStatus.PROGRESS.getValue()))){
                throw new AlreadyInProgressException("There are incomplete tasks among the background individual processes of the hospital group process. (EXCEL GROUP TASK ID : " + dto.getId() + ")");
            }


            excelDBWriteTaskRepository.deleteByGroupId(dto.getId());


            ExcelGroupTaskBO excelGroupTaskBO = new ExcelGroupTaskBO();

            workbook = excelGroupTaskBO.getWorkbookFromResource(resource);

            // Validate the Excel file before starting the task
            ((ExcelDBWriteProcessor)excelDBProcessorFactory.getProcessor(dto.getId())).validateColumnsBeforeDBWrite(workbook);
            //ExcelColumnValidator.validateExcelColumns(workbook, dto.getId());

            Integer totalRowCount = excelGroupTaskBO.getRowCountFromWorkbook(workbook);

            excelGroupTask = excelGroupTaskRepositorySupport.findById(dto.getId());

            excelGroupTask.setTotalRow(totalRowCount);
            // Regardless of the asynchronous processing below, it is excluded from the @Transactional scope here to allow immediate insertion into the DB.
            excelGroupTaskRepository.save(excelGroupTask);


            Integer rowCountPerTask = excelGroupTask.getRowCountPerTask();


            Integer currentRow = 1;

            Boolean isFirstTask = true;
            while (currentRow < totalRowCount - 1) {

                if(!isFirstTask) {
                    currentRow += 1;
                }

                Integer startRow = currentRow;
                Integer endRow = Math.min(currentRow + rowCountPerTask - 1, totalRowCount - 1);

                ExcelDBWriteTask excelDBWriteTask = ExcelDBWriteTask.builder()
                        .status(ExcelDBWriteTaskEventDomain.ExcelTaskStatus.STANDBY.getValue())
                        .excelGroupTask(excelGroupTask).startRow(startRow).endRow(endRow).build();

                // Regardless of the asynchronous processing below, it is excluded from the @Transactional scope here to allow immediate insertion into the DB.
                excelDBWriteTaskRepository.save(excelDBWriteTask);

                // The values inserted here are common to each TaskInQueue (ExcelDBWriteTask).
                ExcelDBWriteTaskEventDomain excelTaskEventDomain = ExcelDBWriteTaskEventDomain.of(excelDBWriteTask.getId(),
                        ExcelDBWriteTaskEventDomain.ExcelTaskStatus.valueOf(excelDBWriteTask.getStatus()), excelGroupTask.getId(),
                        dto.getYellowColorCellUpdate(),
                        dto.getOnlyEmptyColUpdate(),
                        dto.getOnlyNewRowInsert(),
                        workbook, excelDBWriteTask.getStartRow(), excelDBWriteTask.getEndRow(), null, 0);

                // Register in the asynchronous processing queue
                excelDBWriteEventPublisher.publish(excelTaskEventDomain);

                currentRow = endRow;

                isFirstTask = false;
            }

        } finally {

            excelGroupTaskRepositorySupport.setExcelGroupTaskStatusNotInProgress(dto.getId());

            resource.getInputStream().close();

            if(workbook != null){
                workbook.close();
            }

        }

        return new ExcelGroupTaskResDTO.CreateOrUpdateRes(excelGroupTask);
    }



    /* 3. ExcelDBReadGroupTask */

    public Page<ExcelGroupTaskResDTO.OneRes> getExcelDBReadGroupTasks(Boolean skipPagination, Integer pageNum, Integer pageSize,
                                                                      String excelGroupTaskSearchFilter,
                                                                      String sorterValueFilter,
                                                                      String dateRangeFilter)
            throws ResourceNotFoundException, JsonProcessingException {

        return excelGroupTaskRepositorySupport.findExcelDBReadGroupTasksByPageAndFilter(skipPagination, pageNum,pageSize,
                excelGroupTaskSearchFilter, sorterValueFilter, dateRangeFilter);

    }

    public Boolean deleteExcelDBReadGroupTask(Long id) throws IOException {

        try {

            if (!excelGroupTaskRepositorySupport.setExcelGroupTaskStatusInProgress(id)) {
                throw new AlreadyInProgressException("The group process has already started or is being modified.(EXCEL GROUP TASK ID : " + id + ")");
            }

            if (excelDBReadTaskRepository.existsByGroupIdAndStatusIn(id,
                    Arrays.asList(ExcelDBReadTaskEventDomain.ExcelTaskStatus.STANDBY.getValue(),
                            ExcelDBReadTaskEventDomain.ExcelTaskStatus.QUEUE_WAIT.getValue(),
                            ExcelDBReadTaskEventDomain.ExcelTaskStatus.QUEUE.getValue(), ExcelDBReadTaskEventDomain.ExcelTaskStatus.PROGRESS.getValue()))) {
                throw new AlreadyInProgressException("There are incomplete tasks among the background individual processes of the hospital group process. (EXCEL GROUP TASK ID : " + id + ")");
            }


            excelDBReadTaskRepository.deleteByGroupId(id);
            excelGroupTaskRepository.deleteById(id);
        }finally {
            try {
                excelGroupTaskRepositorySupport.setExcelGroupTaskStatusNotInProgress(id);
            }catch (Exception ignored){

            }
        }

        return Boolean.TRUE;

    }
    /*
     *   This includes an asynchronous process. @Transactional was intentionally excluded to allow immediate DB updates
     * */
    public ExcelGroupTaskResDTO.CreateOrUpdateRes createExcelDBReadGroupTask(ExcelGroupTaskReqDTO.DBReadGroupTaskCreateReq dto) throws Exception {

        ExcelGroupTask excelGroupTask = excelGroupTaskRepositorySupport.findById(dto.getId());;

        try {

            if (!excelGroupTaskRepositorySupport.setExcelGroupTaskStatusInProgress(dto.getId())) {
                throw new AlreadyInProgressException("The group process has already started or is being modified.(EXCEL GROUP TASK ID : " + dto.getId() + ")");
            }

            if(excelDBReadTaskRepository.existsByGroupIdAndStatusIn(dto.getId(),
                    Arrays.asList(ExcelDBReadTaskEventDomain.ExcelTaskStatus.STANDBY.getValue(),
                            ExcelDBReadTaskEventDomain.ExcelTaskStatus.QUEUE_WAIT.getValue(),
                            ExcelDBReadTaskEventDomain.ExcelTaskStatus.QUEUE.getValue(),
                            ExcelDBReadTaskEventDomain.ExcelTaskStatus.PROGRESS.getValue()))){
                throw new AlreadyInProgressException("There are incomplete tasks among the background individual processes of the hospital group process. (EXCEL GROUP TASK ID : " + dto.getId() + ")");
            }

            // Delete all existing tasks of the singleton
            eventQueue.initializeQueueForId(dto.getId());

            // Delete all tasks corresponding to the GroupId and start
            excelDBReadTaskRepository.deleteByGroupId(dto.getId());

            // Create an empty Excel file to insert DB values
            excelGroupTaskFileService.createEmptyExcelDBReadGroupTaskExcel(dto.getId());

            // Retrieve the newly created empty Excel file
            Resource resource = excelGroupTaskFileService.getExcelGroupTaskExcelBinaryById(dto.getId());
            if(resource == null || !resource.exists()){
                throw new FileNotFoundException("File NOT found for ID :: " + dto.getId());
            }


            // Get Max ID
            int pageSize = excelGroupTask.getRowCountPerTask();


            // To determine the Max ID (common)
            // Max ID? While asynchronously handling Excel paging and inserting data into Excel, data continues to accumulate. To ensure consistency, exclude data that is accumulated after the current Max ID.


            ExcelDBProcessor processor = excelDBProcessorFactory.getProcessor(dto.getId());

            Page<ExcelDBMaxIdRes> response = (Page<ExcelDBMaxIdRes>) ((ExcelDBReadProcessor)processor).snapshotDBRead(pageSize);

            Long maxId = response.getContent().get(0).getId();
            int totalPages = response.getTotalPages();
            int totalRow = Long.valueOf(response.getTotalElements()).intValue();


            excelGroupTask.setTotalRow(totalRow);
            excelGroupTask.setOriginalFileName(resource.getFilename());
            excelGroupTask.setSavedFileExt(resource.getFilename().substring(resource.getFilename().lastIndexOf(".") + 1));
            excelGroupTask.setExcelUpdatedAt(LocalDateTime.now());


            excelGroupTaskRepository.save(excelGroupTask);


            for(int curPage=1; curPage <= totalPages; curPage++){

                Integer startRow = curPage == 1 ? 0 : (curPage * pageSize - pageSize + 1);
                Integer endRow = curPage * pageSize;
                if(endRow > totalRow){
                    endRow = totalRow;
                }

                ExcelDBReadTask excelDBReadTask = ExcelDBReadTask.builder()
                        .status(ExcelDBReadTaskEventDomain.ExcelTaskStatus.STANDBY.getValue())
                        .excelGroupTask(excelGroupTask).startRow(startRow).endRow(endRow).build();

                // Regardless of the asynchronous processing below, it is excluded from the @Transactional scope here to allow immediate insertion into the DB.
                excelDBReadTaskRepository.save(excelDBReadTask);

                // The values inserted here are common to each TaskInQueue (ExcelDBReadTask)
                ExcelDBReadTaskEventDomain excelTaskEventDomain = ExcelDBReadTaskEventDomain.of(excelDBReadTask.getId(),
                        ExcelDBReadTaskEventDomain.ExcelTaskStatus.valueOf(excelDBReadTask.getStatus()), excelGroupTask.getId(),
                        curPage, pageSize, maxId,
                        null, 0);


                dbReadEventPublisher.publish(excelTaskEventDomain);

            }

            resource.getInputStream().close();

        } finally {
            excelGroupTaskRepositorySupport.setExcelGroupTaskStatusNotInProgress(dto.getId());

        }

        return new ExcelGroupTaskResDTO.CreateOrUpdateRes(excelGroupTask);
    }


}
