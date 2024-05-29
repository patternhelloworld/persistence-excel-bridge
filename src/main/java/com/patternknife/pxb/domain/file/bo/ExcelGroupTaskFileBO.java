package com.patternknife.pxb.domain.file.bo;


import jakarta.validation.constraints.NotEmpty;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ExcelGroupTaskFileBO {

    private final String relativeExcelGroupTaskDirectoryPath;

    public ExcelGroupTaskFileBO(@NotEmpty @Value("${com.patternknife.pxb.dir.root.excel-group-tasks}") String relativeExcelGroupTaskDirectoryPath) {
        this.relativeExcelGroupTaskDirectoryPath = relativeExcelGroupTaskDirectoryPath;
    }

    public Resource getExcelGroupTaskExcelFile(Long excelGroupId, String fileExt) throws IOException {

        FileSystemStorageImpl fileSystemStorageImpl = new FileSystemStorageImpl(relativeExcelGroupTaskDirectoryPath);

        if ("xls".equals(fileExt) || "xlsx".equals(fileExt)) {
        } else {
            fileExt = "xlsx";
        }

        String fileName = excelGroupId + "." + fileExt;

        return fileSystemStorageImpl.loadFile(fileName);
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

    public void createExcelDBReadGroupTaskExcel(String fileName, Workbook workbook) throws IOException {

        String fullFilePath = relativeExcelGroupTaskDirectoryPath + "/" + fileName;

        try (FileOutputStream fileOut = new FileOutputStream(fullFilePath)) {
            workbook.write(fileOut);
            workbook.close();
        }

    }

    public ResponseEntity<Resource> downloadExcelGroupTaskExcelForDBRead(Long excelGroupId, String fileExt) throws IOException {

        FileSystemStorageImpl fileSystemStorageImpl = new FileSystemStorageImpl(relativeExcelGroupTaskDirectoryPath);



        // Determine the content type based on the file extension

        MediaType contentType;
        if ("xls".equals(fileExt)) {
            contentType = new MediaType("application", "vnd.ms-excel");
        } else if ("xlsx".equals(fileExt)) {
            contentType = new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        } else {
            // Fallback in case the extension is neither xls nor xlsx
            contentType = MediaType.APPLICATION_OCTET_STREAM;
            fileExt = "xlsx";
        }

        String fileName = excelGroupId + "." + fileExt;

        Resource resource = fileSystemStorageImpl.loadFile(fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(contentType)
                .body(resource);
    }

    public void uploadExcelGroupTaskExcel(Long id, MultipartFile file) throws IOException {
        FileSystemStorageImpl fileSystemStorageImpl = new FileSystemStorageImpl(relativeExcelGroupTaskDirectoryPath);
        fileSystemStorageImpl.saveFile(file, String.valueOf(id));

    }
}
