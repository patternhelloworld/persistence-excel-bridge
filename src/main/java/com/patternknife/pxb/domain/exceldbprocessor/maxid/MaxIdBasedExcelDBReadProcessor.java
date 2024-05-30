package com.patternknife.pxb.domain.exceldbprocessor.maxid;

import com.patternknife.pxb.domain.common.dto.SorterValueFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class MaxIdBasedExcelDBReadProcessor {
    protected String serializedSorterValueFilter;

    public MaxIdBasedExcelDBReadProcessor() throws JsonProcessingException {
        initializeSerializedSorterValueFilter();
    }

    private void initializeSerializedSorterValueFilter() throws JsonProcessingException {
        SorterValueFilter sorterValueFilter = new SorterValueFilter();
        sorterValueFilter.setColumn("id");
        sorterValueFilter.setAsc(false);
        ObjectMapper objectMapper = new ObjectMapper();
        serializedSorterValueFilter = objectMapper.writeValueAsString(sorterValueFilter);
    }
}
