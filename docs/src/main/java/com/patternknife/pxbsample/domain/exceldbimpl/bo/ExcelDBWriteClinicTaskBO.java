package com.patternknife.pxbsample.domain.exceldbimpl.bo;


import com.patternknife.pxb.domain.exceldbwritetask.bo.IExcelDBWriteTaskBO;
import com.patternknife.pxb.domain.exceldbwritetask.queue.ExcelDBWriteTaskEventDomain;
import com.patternknife.pxbsample.config.logger.module.ExcelAsyncUploadErrorLogConfig;
import com.patternknife.pxbsample.domain.clinic.dao.ClinicRepository;
import com.patternknife.pxbsample.domain.clinic.entity.Clinic;
import com.patternknife.pxbsample.domain.clinic.enums.ClinicStatusConst;
import com.patternknife.pxbsample.domain.exceldbimpl.enums.clinic.ExcelClinicTaskColumnMapping;
import com.patternknife.pxbsample.util.CustomUtils;
import com.patternknife.pxbsample.util.ReflectionUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RequiredArgsConstructor
@Service
public class ExcelDBWriteClinicTaskBO implements IExcelDBWriteTaskBO {

    private static final Logger logger = LoggerFactory.getLogger(ExcelAsyncUploadErrorLogConfig.class);

    private static class CellValueReplacer {
        // 사용 시 엑셀 스트링에 공백이 있을 수 있으므로, 대상에 trim() 을 항상 해준다.
        private final static String NAME_STATUS_RX = "(?:-|[\\n\\r\\t\\s])+(운영|폐업|중지)$";

        public static String removeStatusFromName(String name){
            Pattern pattern = Pattern.compile(NAME_STATUS_RX);
            return name.trim().replaceAll(NAME_STATUS_RX, "");
        }

        public static ClinicStatusConst nameStatus(String name) {
            Pattern pattern = Pattern.compile(NAME_STATUS_RX);
            Matcher matcher = pattern.matcher(name.trim());

            if (matcher.find()) {
                String status = matcher.group(1);
                switch (status) {
                    case "폐업":
                        return ClinicStatusConst.폐업;
                    case "중지":
                        return ClinicStatusConst.중지;
                    default:
                        return ClinicStatusConst.운영;
                }
            } else {
                return ClinicStatusConst.운영;
            }
        }
    }

    private final ClinicRepository clinicRepository;

    @Override
    public ExcelDBWriteTaskEventDomain logNotUpdatedInfo(ExcelDBWriteTaskEventDomain excelDBWriteTaskEventDomain,
                                                         String msg, int rowIndex, Boolean isWholeRowFailed){
        logger.trace(msg);
        return excelDBWriteTaskEventDomain.updateErrorMessage(rowIndex +  " (액셀 행 번호) : " + msg + " " + (isWholeRowFailed ? "(행 단위 업데이트 실패)" : "(열 단위 업데이트 실패)"));
    }


    // 메인 함수
    @Override
    public synchronized ExcelDBWriteTaskEventDomain updateTableFromExcel(ExcelDBWriteTaskEventDomain excelDBWriteTaskEventDomain)  {

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

                clinic.setStatus(CellValueReplacer.nameStatus(newCellValue).getValue());
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
}
