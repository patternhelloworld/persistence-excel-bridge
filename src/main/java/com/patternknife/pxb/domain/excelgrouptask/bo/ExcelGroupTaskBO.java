package com.patternknife.pxb.domain.excelgrouptask.bo;

import com.patternknife.pxb.domain.file.util.FileUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

public class ExcelGroupTaskBO {

    public Workbook getWorkbookFromResource(Resource resource) throws IOException {

        try (InputStream is = resource.getInputStream()) {
            Workbook workbook;
            if ("xls".equals(FileUtil.getFileExtension(resource.getFilename()))) {
                workbook = new HSSFWorkbook(is);
            } else if ("xlsx".equals(FileUtil.getFileExtension(resource.getFilename()))) {
                // xlsx
                workbook = new XSSFWorkbook(is);
            }else{
                throw new IllegalStateException("The extension (" + FileUtil.getFileExtension(resource.getFilename()) + ") of the following Excel file (" + resource.getFilename() + ") is not a valid extension (xls or xlsx).");
            }
            return workbook;
        }

    }

    public int getRowCountFromResource(Resource resource) throws IOException {

        Workbook workbook = getWorkbookFromResource(resource);

        // Assuming the data is in the first sheet
        Sheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getPhysicalNumberOfRows(); // This will give you the number of rows that are actually filled with data

        workbook.close(); // Always close the workbook when you're done
        return rowCount;

    }

    public int getRowCountFromWorkbook(Workbook workbook) throws IOException {

        // Assuming the data is in the first sheet
        Sheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getPhysicalNumberOfRows(); // This will give you the number of rows that are actually filled with data

        workbook.close(); // Always close the workbook when you're done
        return rowCount;

    }

}
