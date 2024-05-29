package com.patternknife.pxb.domain.exceldbwritetask.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.patternknife.pxb.config.response.error.exception.data.ResourceNotFoundException;
import com.patternknife.pxb.domain.excelcommontask.dto.ExcelCommonTaskResDTO;
import com.patternknife.pxb.domain.exceldbwritetask.dao.ExcelDBWriteTaskRepository;
import com.patternknife.pxb.domain.exceldbwritetask.dao.ExcelDBWriteTaskRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;



@RequiredArgsConstructor
@Service
public class ExcelDBWriteTaskService {


    private final ExcelDBWriteTaskRepository excelDBWriteTaskRepository;
    private final ExcelDBWriteTaskRepositorySupport excelDBWriteTaskRepositorySupport;

    public Page<ExcelCommonTaskResDTO.OneRes> findExcelDBWriteTasks(Boolean skipPagination,
                                                                    Integer pageNum,
                                                                    Integer pageSize,
                                                                    String excelDBWriteTaskSearchFilter,
                                                                    String sorterValueFilter,
                                                                    String dateRangeFilter, Long groupId) throws JsonProcessingException, ResourceNotFoundException {

        return excelDBWriteTaskRepositorySupport.findExcelDBWriteTasksByPageAndFilterAndGroupId(skipPagination, pageNum, pageSize, excelDBWriteTaskSearchFilter, sorterValueFilter, dateRangeFilter, groupId);

    }

    public ExcelCommonTaskResDTO.StatusRes findExcelDBWriteTaskCountsByStatus(Long groupId){
        return excelDBWriteTaskRepositorySupport.findExcelDBWriteTaskCountsByStatus(groupId);
    }

    public ExcelCommonTaskResDTO.StartEndTimestampRes findExcelDBWriteTaskStartEndTimestamps(Long groupId){
        return excelDBWriteTaskRepositorySupport.findStartEndTimestampsByGroupId(groupId);
    }
}
