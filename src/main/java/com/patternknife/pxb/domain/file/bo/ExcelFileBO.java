package com.patternknife.pxb.domain.file.bo;


import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ExcelFileBO {

    private final String relativeExcelGroupTaskDirectoryPath;

    public ExcelFileBO(@Value("${com.patternknife.pxb.dir.root.excel-group-tasks}") String relativeExcelGroupTaskDirectoryPath) {
        this.relativeExcelGroupTaskDirectoryPath = relativeExcelGroupTaskDirectoryPath;
    }

    public Long getFileSizeExcelGroupTaskExcelForDBRead(Long excelGroupId, String fileExt) throws IOException {
        FileSystemStorageImpl fileSystemStorageImpl = new FileSystemStorageImpl(relativeExcelGroupTaskDirectoryPath);

        String fileName = excelGroupId + "." + fileExt;


        return fileSystemStorageImpl.getFileSize(fileName);
    }

    public void createExcelDBReadGroupTaskExcel(Long excelGroupTaskId, Workbook workbook) throws IOException {

        String fileName = excelGroupTaskId + ".xlsx";
        String fullFilePath = relativeExcelGroupTaskDirectoryPath + "/" + fileName;

        try (FileOutputStream fileOut = new FileOutputStream(fullFilePath)) {
            workbook.write(fileOut);
            workbook.close();
        }

    }
}
