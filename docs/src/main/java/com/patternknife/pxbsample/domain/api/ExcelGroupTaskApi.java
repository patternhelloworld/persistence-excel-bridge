package com.patternknife.pxbsample.domain.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.patternknife.pxb.domain.excelgrouptask.dto.ExcelGroupTaskReqDTO;
import com.patternknife.pxb.domain.excelgrouptask.dto.ExcelGroupTaskResDTO;
import com.patternknife.pxb.domain.excelgrouptask.service.ExcelGroupTaskService;
import com.patternknife.pxb.util.CommonConstant;
import com.patternknife.pxbsample.config.response.GlobalSuccessPayload;
import com.patternknife.pxbsample.config.response.error.exception.data.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ExcelGroupTaskApi {

    private final ExcelGroupTaskService excelGroupTaskService;

    /*
    *   DB WRITE Group
    * */
    @PreAuthorize("@authorityService.hasRole('DB_WRITE_ADMIN')")
    @GetMapping("/excel-group-tasks/db-writes")
    public GlobalSuccessPayload<Page<ExcelGroupTaskResDTO.OneRes>> getExcelDBWriteGroupTasks(@RequestParam(value = "skipPagination", required = false, defaultValue = "false") final Boolean skipPagination,
                                                                                             @RequestParam(value = "pageNum", required = false, defaultValue = CommonConstant.COMMON_PAGE_NUM) final Integer pageNum,
                                                                                             @RequestParam(value = "pageSize", required = false, defaultValue = CommonConstant.COMMON_PAGE_SIZE) final Integer pageSize,
                                                                                             @RequestParam(value = "excelGroupTaskSearchFilter", required = false) final String excelGroupTaskSearchFilter,
                                                                                             @RequestParam(value = "sorterValueFilter", required = false) final String sorterValueFilter,
                                                                                             @RequestParam(value = "dateRangeFilter", required = false) String dateRangeFilter)
            throws ResourceNotFoundException, JsonProcessingException {
        return new GlobalSuccessPayload<>(excelGroupTaskService.getExcelDBWriteGroupTasks(skipPagination, pageNum, pageSize, excelGroupTaskSearchFilter, sorterValueFilter, dateRangeFilter));
    }

    @PreAuthorize("@authorityService.hasRole('DB_WRITE_ADMIN')")
    @PostMapping("/excel-group-tasks/db-writes/queues")
    public GlobalSuccessPayload<ExcelGroupTaskResDTO.CreateOrUpdateRes> runExcelDBWriteGroupTask(@Valid @RequestBody final ExcelGroupTaskReqDTO.DBWriteGroupTaskCreateReq dto)
            throws DataIntegrityViolationException, IOException {
        return new GlobalSuccessPayload<>(excelGroupTaskService.createExcelDBWriteGroupTask(dto));
    }

    @PreAuthorize("@authorityService.hasRole('DB_WRITE_ADMIN')")
    @DeleteMapping("/excel-group-tasks/{id}/db-writes")
    public GlobalSuccessPayload<Boolean> deleteExcelDBWriteGroupTask(@PathVariable final Long id)
            throws DataIntegrityViolationException, IOException {
        return new GlobalSuccessPayload<>(excelGroupTaskService.deleteExcelDBWriteGroupTask(id));
    }

    @PreAuthorize("@authorityService.hasRole('DB_WRITE_ADMIN')")
    @PatchMapping("/excel-group-tasks/{id}/db-writes")
    public GlobalSuccessPayload<ExcelGroupTaskResDTO.CreateOrUpdateRes> updateExcelGroupTask(@PathVariable final long id,
                                                                                  @Valid @RequestBody final ExcelGroupTaskReqDTO.UpdateReq dto) {

        return new GlobalSuccessPayload<>(excelGroupTaskService.updateExcelGroupTask(id, dto));
    }

    @PreAuthorize("@authorityService.hasRole('DB_WRITE_ADMIN')")
    @PostMapping("/excel-group-tasks/db-writes")
    public GlobalSuccessPayload<ExcelGroupTaskResDTO.CreateOrUpdateRes> createExcelGroupTask(@Valid @RequestBody final ExcelGroupTaskReqDTO.CreateReq dto) {

        return new GlobalSuccessPayload<>(excelGroupTaskService.createExcelGroupTask(dto));
    }



    /*
     *   DB READ Group
     * */
    @PreAuthorize("@authorityService.hasRole('DB_READ_ADMIN')")
    @GetMapping("/excel-group-tasks/db-reads")
    public GlobalSuccessPayload<Page<ExcelGroupTaskResDTO.OneRes>> getExcelDBReadGroupTasks(@RequestParam(value = "skipPagination", required = false, defaultValue = "false") final Boolean skipPagination,
                                                                                            @RequestParam(value = "pageNum", required = false, defaultValue = CommonConstant.COMMON_PAGE_NUM) final Integer pageNum,
                                                                                            @RequestParam(value = "pageSize", required = false, defaultValue = CommonConstant.COMMON_PAGE_SIZE) final Integer pageSize,
                                                                                            @RequestParam(value = "excelGroupTaskSearchFilter", required = false) final String excelGroupTaskSearchFilter,
                                                                                            @RequestParam(value = "sorterValueFilter", required = false) final String sorterValueFilter,
                                                                                            @RequestParam(value = "dateRangeFilter", required = false) String dateRangeFilter)
            throws ResourceNotFoundException, JsonProcessingException {
        return new GlobalSuccessPayload<>(excelGroupTaskService.getExcelDBReadGroupTasks(skipPagination, pageNum, pageSize, excelGroupTaskSearchFilter, sorterValueFilter, dateRangeFilter));
    }

    @PreAuthorize("@authorityService.hasRole('DB_READ_ADMIN')")
    @PostMapping("/excel-group-tasks/db-reads/queues")
    public GlobalSuccessPayload<ExcelGroupTaskResDTO.CreateOrUpdateRes> runExcelDBReadGroupTask(@Valid @RequestBody final ExcelGroupTaskReqDTO.DBReadGroupTaskCreateReq dto)
            throws Exception {
        return new GlobalSuccessPayload<>(excelGroupTaskService.createExcelDBReadGroupTask(dto));
    }

    @PreAuthorize("@authorityService.hasRole('DB_READ_ADMIN')")
    @DeleteMapping("/excel-group-tasks/{id}/db-reads")
    public GlobalSuccessPayload<Boolean> deleteExcelDBReadGroupTask(@PathVariable final Long id)
            throws DataIntegrityViolationException, IOException {
        return new GlobalSuccessPayload<>(excelGroupTaskService.deleteExcelDBReadGroupTask(id));
    }

    @PreAuthorize("@authorityService.hasRole('DB_READ_ADMIN')")
    @PatchMapping("/excel-group-tasks/{id}/db-reads")
    public GlobalSuccessPayload<ExcelGroupTaskResDTO.CreateOrUpdateRes> updateExcelDBReadGroupTask(@PathVariable final long id,
                                                                                             @Valid @RequestBody final ExcelGroupTaskReqDTO.UpdateReq dto) {

        return new GlobalSuccessPayload<>(excelGroupTaskService.updateExcelGroupTask(id, dto));
    }

    @PreAuthorize("@authorityService.hasRole('DB_READ_ADMIN')")
    @PostMapping("/excel-group-tasks/db-reads")
    public GlobalSuccessPayload<ExcelGroupTaskResDTO.CreateOrUpdateRes> createExcelDBReadGroupTask(@Valid @RequestBody final ExcelGroupTaskReqDTO.CreateReq dto) {

        return new GlobalSuccessPayload<>(excelGroupTaskService.createExcelGroupTask(dto));
    }


}
