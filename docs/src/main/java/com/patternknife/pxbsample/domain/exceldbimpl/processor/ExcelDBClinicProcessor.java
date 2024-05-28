package com.patternknife.pxbsample.domain.exceldbimpl.processor;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.patternknife.pxb.domain.exceldbprocessor.cache.IExcelDBReadInMemoryData;
import com.patternknife.pxb.domain.exceldbprocessor.maxid.MaxIdBasedExcelDBProcessor;
import com.patternknife.pxb.domain.exceldbprocessor.processor.ExcelDBReadProcessor;
import com.patternknife.pxb.domain.exceldbprocessor.processor.ExcelDBWriteProcessor;
import com.patternknife.pxb.domain.exceldbreadtask.queue.ExcelDBReadTaskEventDomain;
import com.patternknife.pxb.domain.file.bo.ExcelFileBO;
import com.patternknife.pxbsample.config.response.error.exception.data.PreconditionFailedException;
import com.patternknife.pxbsample.domain.clinic.dao.ClinicRepositorySupport;
import com.patternknife.pxbsample.domain.clinic.dto.ClinicResDTO;
import com.patternknife.pxbsample.domain.clinic.dto.ClinicSearchFilter;
import com.patternknife.pxbsample.domain.exceldbimpl.cache.ExcelDBReadInMemoryData;
import com.patternknife.pxbsample.domain.exceldbimpl.enums.clinic.ExcelClinicTaskColumnMapping;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Component
public class ExcelDBClinicProcessor extends MaxIdBasedExcelDBProcessor
        implements ExcelDBReadProcessor, ExcelDBWriteProcessor {

    private final ClinicRepositorySupport clinicRepositorySupport;
    private final ExcelFileBO fileBO;

    public ExcelDBClinicProcessor(ClinicRepositorySupport clinicRepositorySupport, ExcelFileBO fileBO) throws IOException {
        super();
        this.clinicRepositorySupport = clinicRepositorySupport;
        this.fileBO = fileBO;

    }

    @Override
    public Page<?> snapshotDBRead(int pageSize) throws JsonProcessingException {
        return clinicRepositorySupport.findDetailsByPageAndFilter(false, 1 ,pageSize, null, serializedSorterValueFilter, null);
    }


    @Override
    public void cacheDBReadToInMemory(ExcelDBReadTaskEventDomain excelDBReadTaskEventDomain, IExcelDBReadInMemoryData excelDBReadInMemoryData) throws Exception {
        ClinicSearchFilter clinicSearchFilter = new ClinicSearchFilter();
        clinicSearchFilter.setMaxId(excelDBReadTaskEventDomain.getMaxId());

        Page<ClinicResDTO.OneDetails> oneDetails = clinicRepositorySupport.findDetailsByPageAndFilter(false, excelDBReadTaskEventDomain.getPageNum(),
                excelDBReadTaskEventDomain.getPageSize(), new ObjectMapper().writeValueAsString(clinicSearchFilter), serializedSorterValueFilter, null);

        ((ExcelDBReadInMemoryData)excelDBReadInMemoryData).addClinicDetails(oneDetails.getContent());
    }

    @Override
    public void flushInMemoryToExcelFile(Long excelGroupId, IExcelDBReadInMemoryData iExcelDBReadInMemoryData) throws IOException {

        ExcelDBReadInMemoryData excelDBReadInMemoryData = (ExcelDBReadInMemoryData) iExcelDBReadInMemoryData;

        if(!(excelDBReadInMemoryData.getClinicDetails().isEmpty())){

            try (SXSSFWorkbook workbook = new SXSSFWorkbook(2000)) {

                Sheet sheet = workbook.createSheet("병원 리스트");

                final int headerRowIndex = 0;

                Row row = sheet.createRow(headerRowIndex);

                createCell(row, 0, "No.");
                createCell(row, 1, "병원명");
                createCell(row, 2, "병원번호");
                createCell(row, 3, "데이터 생성일");


                int rowIndex = headerRowIndex + 1;

                for (ClinicResDTO.OneDetails oneDetails : excelDBReadInMemoryData.getClinicDetails()) {

                    row = sheet.createRow(rowIndex);


                    createCell(row, 0, String.valueOf(oneDetails.getId()));
                    createCell(row, 1, oneDetails.getName());
                    createCell(row, 2, oneDetails.getPhoneNumber());
                    createCell(row, 3, String.valueOf(oneDetails.getCreatedAt()));


                    rowIndex++;

                }

                fileBO.createExcelDBReadGroupTaskExcel(excelGroupId, workbook);
            }
        }
    }


    @Override
    public void validateColumnsBeforeDBWrite(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);
        Row firstRow = sheet.getRow(0);
        Map<Integer, String> columnMapping = getClinicColumnMapping();

        for (Cell cell : firstRow) {
            String excelColumnName = cell.getStringCellValue();
            int columnIndex = cell.getColumnIndex();

            if (!columnMapping.containsKey(columnIndex) || !columnMapping.get(columnIndex).equals(excelColumnName)) {
                throw new PreconditionFailedException("1 행의 셀 값과 열 번호가 일치하지 않음이 발견 되었습니다. (" + columnIndex + " 번째 열은 " + columnMapping.get(columnIndex) + " 값을 가져야 하지만, " + excelColumnName + " 값이 확인 되었습니다.");
            }
        }

    }

    private static Map<Integer, String> getClinicColumnMapping() {
        Map<Integer, String> map = new HashMap<>();
        for (ExcelClinicTaskColumnMapping column : ExcelClinicTaskColumnMapping.values()) {
            map.put(column.getColumnNumber(), column.getExcelColumnName());
        }
        return map;
    }


}
