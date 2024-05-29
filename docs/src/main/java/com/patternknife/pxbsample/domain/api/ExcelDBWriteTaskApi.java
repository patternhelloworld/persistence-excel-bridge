package com.patternknife.pxbsample.domain.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.patternknife.pxb.domain.excelcommontask.dto.ExcelCommonTaskResDTO;
import com.patternknife.pxb.domain.exceldbwritetask.service.ExcelDBWriteTaskService;
import com.patternknife.pxb.util.CommonConstant;
import com.patternknife.pxbsample.config.response.GlobalSuccessPayload;
import com.patternknife.pxbsample.config.response.error.exception.data.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ExcelDBWriteTaskApi {

    private final ExcelDBWriteTaskService excelDBWriteTaskService;

    @PreAuthorize("@authorityService.hasRole('DB_WRITE_ADMIN')")
    @GetMapping("/excel-db-write-tasks")
    public GlobalSuccessPayload<Page<ExcelCommonTaskResDTO.OneRes>> getExcelDBWriteTasks(@RequestParam(value = "skipPagination", required = false, defaultValue = "false") final Boolean skipPagination,
                                                                                         @RequestParam(value = "pageNum", required = false, defaultValue = CommonConstant.COMMON_PAGE_NUM) final Integer pageNum,
                                                                                         @RequestParam(value = "pageSize", required = false, defaultValue = CommonConstant.COMMON_PAGE_SIZE) final Integer pageSize,
                                                                                         @RequestParam(value = "excelDBWriteTaskSearchFilter", required = false) final String excelDBWriteTaskSearchFilter,
                                                                                         @RequestParam(value = "sorterValueFilter", required = false) final String sorterValueFilter,
                                                                                         @RequestParam(value = "dateRangeFilter", required = false) String dateRangeFilter,
                                                                                         @RequestParam(value = "groupId", required = false) Long groupId)
            throws ResourceNotFoundException, JsonProcessingException {
        return new GlobalSuccessPayload<>(excelDBWriteTaskService.findExcelDBWriteTasks(skipPagination, pageNum, pageSize,
                excelDBWriteTaskSearchFilter, sorterValueFilter, dateRangeFilter, groupId));
    }


    @PreAuthorize("@authorityService.hasRole('DB_READ_ADMIN')")
    @GetMapping("/excel-db-write-tasks/counts-by-status")
    public GlobalSuccessPayload<ExcelCommonTaskResDTO.StatusRes> getExcelDBWriteTaskCountsByStatus(@RequestParam(value = "groupId", required = false) Long groupId)
            throws ResourceNotFoundException {
        return new GlobalSuccessPayload<>(excelDBWriteTaskService.findExcelDBWriteTaskCountsByStatus(groupId));
    }

    @PreAuthorize("@authorityService.hasRole('DB_READ_ADMIN')")
    @GetMapping("/excel-db-write-tasks/start-end-times")
    public GlobalSuccessPayload<ExcelCommonTaskResDTO.StartEndTimestampRes> getExcelDBWriteTaskTimeStamps(@RequestParam(value = "groupId", required = false) Long groupId)
            throws ResourceNotFoundException {
        return new GlobalSuccessPayload<>(excelDBWriteTaskService.findExcelDBWriteTaskStartEndTimestamps(groupId));
    }



}
