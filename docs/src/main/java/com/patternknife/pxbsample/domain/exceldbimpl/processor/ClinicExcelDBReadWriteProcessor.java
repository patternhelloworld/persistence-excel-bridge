package com.patternknife.pxbsample.domain.exceldbimpl.processor;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.patternknife.pxb.domain.exceldbprocessor.cache.IExcelDBReadInMemoryData;
import com.patternknife.pxb.domain.exceldbprocessor.maxid.MaxIdBasedExcelDBReadProcessor;
import com.patternknife.pxb.domain.exceldbprocessor.processor.ExcelDBReadProcessor;
import com.patternknife.pxb.domain.exceldbprocessor.processor.ExcelDBWriteProcessor;
import com.patternknife.pxb.domain.exceldbreadtask.queue.ExcelDBReadTaskEventDomain;
import com.patternknife.pxb.domain.exceldbwritetask.queue.ExcelDBWriteTaskEventDomain;
import com.patternknife.pxb.domain.file.bo.ExcelGroupTaskFileBO;
import com.patternknife.pxbsample.config.logger.module.ExcelAsyncUploadErrorLogConfig;
import com.patternknife.pxbsample.config.response.error.exception.data.PreconditionFailedException;
import com.patternknife.pxbsample.domain.clinic.dao.ClinicRepository;
import com.patternknife.pxbsample.domain.clinic.dao.ClinicRepositorySupport;
import com.patternknife.pxbsample.domain.clinic.dto.ClinicResDTO;
import com.patternknife.pxbsample.domain.clinic.dto.ClinicSearchFilter;
import com.patternknife.pxbsample.domain.clinic.entity.Clinic;
import com.patternknife.pxbsample.domain.exceldbimpl.cache.ExcelDBReadInMemoryData;
import com.patternknife.pxbsample.util.CustomUtils;
import com.patternknife.pxbsample.util.ReflectionUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class ClinicExcelDBReadWriteProcessor extends MaxIdBasedExcelDBReadProcessor
        implements ExcelDBReadProcessor, ExcelDBWriteProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ExcelAsyncUploadErrorLogConfig.class);

    private final ClinicRepositorySupport clinicRepositorySupport;
    private final ClinicRepository clinicRepository;
    private final ExcelGroupTaskFileBO fileBO;

    public ClinicExcelDBReadWriteProcessor(ClinicRepositorySupport clinicRepositorySupport, ClinicRepository clinicRepository, ExcelGroupTaskFileBO fileBO) throws IOException {
        this.clinicRepositorySupport = clinicRepositorySupport;
        this.clinicRepository = clinicRepository;
        this.fileBO = fileBO;

    }

    @Override
    public Page<ClinicResDTO.OneDetails> snapshotDBRead(int pageSize) throws JsonProcessingException {
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

    @Override
    public ExcelDBWriteTaskEventDomain updateTableFromExcel(ExcelDBWriteTaskEventDomain excelDBWriteTaskEventDomain) {
        Sheet sheet = excelDBWriteTaskEventDomain.getWorkbook().getSheetAt(0);
        int endRow = excelDBWriteTaskEventDomain.getEndRow() != null ? excelDBWriteTaskEventDomain.getEndRow() : sheet.getLastRowNum();

        for (int rowIndex = excelDBWriteTaskEventDomain.getStartRow(); rowIndex <= endRow; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue; // Skip empty rows
            }

            String phoneNumber = getCellValue(row, ExcelClinicTaskColumnMapping.PHONE.getColumnNumber(), true);
            if(CustomUtils.isEmpty(phoneNumber)) {
                excelDBWriteTaskEventDomain = logNotUpdatedInfo(excelDBWriteTaskEventDomain,  "Rows in the Excel file without a PHONE column cannot be updated.", rowIndex, true);
                continue;
            }

            Clinic clinic = clinicRepository.findClinicByPhoneNumber(phoneNumber).orElse(null);
            if(clinic == null){
                // 신규
                clinic = new Clinic();
            }else {
                if(excelDBWriteTaskEventDomain.getOnlyNewRowInsert()){
                    continue;
                }
            }

            for (ExcelClinicTaskColumnMapping mapping : ExcelClinicTaskColumnMapping.values()) {
                excelDBWriteTaskEventDomain = updateClinicField(clinic, row, mapping, excelDBWriteTaskEventDomain);
            }


            try {
                clinicRepository.save(clinic);
            }catch (Exception e){
                excelDBWriteTaskEventDomain = logNotUpdatedInfo(excelDBWriteTaskEventDomain,  "The next row (ID : "  + clinic.getId()  + ", Phone number : " + clinic.getPhoneNumber() + ") was not updated due to an unhandled error. : " + e.getMessage(), rowIndex, true);
            }

        }

        return excelDBWriteTaskEventDomain;
    }

    @Override
    public ExcelDBWriteTaskEventDomain logNotUpdatedInfo(ExcelDBWriteTaskEventDomain excelDBWriteTaskEventDomain, String msg, int rowIndex, Boolean isWholeRowFailed) {
        logger.trace(msg);
        return excelDBWriteTaskEventDomain.updateErrorMessage(rowIndex +  " (액셀 행 번호) : " + msg + " " + (isWholeRowFailed ? "(행 단위 업데이트 실패)" : "(열 단위 업데이트 실패)"));
    }


    private Boolean isUpdatable(Clinic clinic, Row row, ExcelClinicTaskColumnMapping excelClinicTaskColumnMapping, ExcelDBWriteTaskEventDomain excelTaskEventDomain){

        boolean updateField = true;

        if (excelTaskEventDomain.getYellowColorCellUpdate() != null && excelTaskEventDomain.getYellowColorCellUpdate()) {
            return isCellColorYellow(row, excelClinicTaskColumnMapping.getColumnNumber());
        }

        if (excelTaskEventDomain.getOnlyEmptyColUpdate() != null && excelTaskEventDomain.getOnlyEmptyColUpdate()) {
            return CustomUtils.isEmpty(ReflectionUtils.getFieldValue(clinic, excelClinicTaskColumnMapping.getEntityColumnName()));
        }

        if (excelTaskEventDomain.getOnlyNewRowInsert() != null && excelTaskEventDomain.getOnlyNewRowInsert()) {
            return false;
        }
        return updateField;
    }

    // Clinic 엔터티 필드에 Set
    private ExcelDBWriteTaskEventDomain updateClinicField(Clinic clinic, Row row, ExcelClinicTaskColumnMapping mapping, ExcelDBWriteTaskEventDomain excelTaskEventDomain) {

        Boolean forceNumToStr = mapping.equals(ExcelClinicTaskColumnMapping.PHONE);

        String newCellValue = getCellValue(row, mapping.getColumnNumber(),
                forceNumToStr);

        Boolean updateField = isUpdatable(clinic, row, mapping, excelTaskEventDomain);

        if(updateField) {
            if (mapping.equals(ExcelClinicTaskColumnMapping.NAME)) {

                //clinic.setName(CellValueReplacer.removeStatusFromName(newCellValue));
                clinic.setName(newCellValue);
            } else {
                try {
                    ReflectionUtils.setFieldValue(clinic, mapping.getEntityColumnName(), newCellValue);
                }catch (Exception e){
                    excelTaskEventDomain = logNotUpdatedInfo(excelTaskEventDomain, " : " + mapping.getEntityColumnName() + " : "
                            + " : " + newCellValue + " : "+ e.getMessage(), row.getRowNum(), false);
                }
            }
        }

        return excelTaskEventDomain;

    }

    // 엑셀 라이브러리를 통해 Cell 에 접근
    private String getCellValue(Row row, int cellNumber, Boolean forceNumToStr) {
        Cell cell = row.getCell(cellNumber);
        if (cell != null) {
            CellType cellType = cell.getCellType();
            switch (cellType) {
                case STRING:
                    return cell.getStringCellValue().trim();
                case NUMERIC:
                    // Use a DecimalFormat to avoid scientific notation
                    if(forceNumToStr) {
                        cell.setCellType(CellType.STRING);
                        return cell.getStringCellValue().trim();
                    }else {
                        if(HSSFDateUtil.isCellDateFormatted(cell))
                        {
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = cell.getDateCellValue();
                            return df.format(date);
                        }
                        return String.valueOf(cell.getNumericCellValue()).trim();
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue()).trim();
                case FORMULA:
                    // Depending on the formula result type, you might need further handling
                    return cell.getCellFormula().trim();
                default:
                    return "";
            }
        }
        return "";
    }
    private Boolean isCellColorYellow(Row row, int cellNumber) {
        Cell cell = row.getCell(cellNumber);
        CellStyle cellStyle = cell.getCellStyle();
        if (cellStyle != null) {
            short bgColor = cellStyle.getFillForegroundColor();
            if (bgColor == 0) {
                return true;
            }
        }
        return false;
    }


    private static Map<Integer, String> getClinicColumnMapping() {
        Map<Integer, String> map = new HashMap<>();
        for (ExcelClinicTaskColumnMapping column : ExcelClinicTaskColumnMapping.values()) {
            map.put(column.getColumnNumber(), column.getExcelColumnName());
        }
        return map;
    }


}
