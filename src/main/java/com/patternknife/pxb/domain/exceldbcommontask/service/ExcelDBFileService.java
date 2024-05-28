package com.patternknife.pxb.domain.exceldbcommontask.service;


import com.patternknife.pxb.domain.exceldbprocessor.factory.IExcelDBProcessorFactory;
import com.patternknife.pxb.domain.exceldbprocessor.processor.ExcelDBWriteProcessor;
import com.patternknife.pxb.domain.excelgrouptask.dao.ExcelGroupTaskRepositorySupport;
import com.patternknife.pxb.domain.excelgrouptask.entity.ExcelGroupTask;
import com.patternknife.pxb.domain.file.bo.FileSystemStorageImpl;
import com.patternknife.pxb.domain.file.util.FileUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ExcelDBFileService {


    private final String relativeExcelGroupTaskDirectoryPath;
    private final ExcelGroupTaskRepositorySupport excelGroupTaskRepositorySupport;

    private final IExcelDBProcessorFactory excelDBProcessorFactory;

    public ExcelDBFileService(@Value("${com.patternknife.pxb.dir.root.excel-group-tasks}") String relativeExcelGroupTaskDirectoryPath, ExcelGroupTaskRepositorySupport excelGroupTaskRepositorySupport, IExcelDBProcessorFactory excelDBProcessorFactory) {
        this.relativeExcelGroupTaskDirectoryPath = relativeExcelGroupTaskDirectoryPath;
        this.excelGroupTaskRepositorySupport = excelGroupTaskRepositorySupport;
        this.excelDBProcessorFactory = excelDBProcessorFactory;
    }

    @Transactional( rollbackFor=Exception.class)
    public Boolean uploadExcelGroupTaskExcel(Long id, MultipartFile file) throws IOException {

        ExcelGroupTask excelGroupTask = excelGroupTaskRepositorySupport.findById(id);


        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            ((ExcelDBWriteProcessor)excelDBProcessorFactory.getProcessor(excelGroupTask.getId())).validateColumnsBeforeDBWrite(workbook);

        } catch (IOException e) {
            throw e;
        }

        FileSystemStorageImpl fileSystemStorageImpl = new FileSystemStorageImpl(relativeExcelGroupTaskDirectoryPath);

        fileSystemStorageImpl.saveFile(file, String.valueOf(id));

        excelGroupTask.setSavedFileExt(FileUtil.getFileExtension(file));
        excelGroupTask.setOriginalFileName(file.getOriginalFilename());

        return true;
    }
    public Resource getExcelGroupTaskExcelBinaryById(Long id) throws IOException {

        FileSystemStorageImpl fileSystemStorageImpl = new FileSystemStorageImpl(relativeExcelGroupTaskDirectoryPath);

        ExcelGroupTask excelGroupTask = excelGroupTaskRepositorySupport.findById(id);

        String fileExt = excelGroupTask.getSavedFileExt();

        if ("xls".equals(fileExt) || "xlsx".equals(fileExt)) {
        } else {
            fileExt = "xlsx";
        }

        String fileName = id + "." + fileExt;

        return fileSystemStorageImpl.loadFile(fileName);

    }

    public Resource getExcelGroupTaskExcelDBReadBinaryById(Long id) throws IOException {

        FileSystemStorageImpl fileSystemStorageImpl = new FileSystemStorageImpl(relativeExcelGroupTaskDirectoryPath);

        ExcelGroupTask excelGroupTask = excelGroupTaskRepositorySupport.findById(id);

        String fileExt = excelGroupTask.getSavedFileExt();

        if ("xls".equals(fileExt) || "xlsx".equals(fileExt)) {
        } else {
            fileExt = "xlsx";
        }

        String fileName = id + "." + fileExt;

        return fileSystemStorageImpl.loadFile(fileName);

    }



    @Transactional( rollbackFor=Exception.class)
    public void createEmptyExcelDBReadGroupTaskExcel(Long id) throws IOException {

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Sheet1");

        String fileName = id + ".xlsx";
        String fullFilePath = relativeExcelGroupTaskDirectoryPath + "/" + fileName;

        try (FileOutputStream fileOut = new FileOutputStream(fullFilePath)) {
            workbook.write(fileOut);
        }

        ExcelGroupTask excelGroupTask = excelGroupTaskRepositorySupport.findById(id);

        excelGroupTask.setSavedFileExt("xlsx");
        excelGroupTask.setOriginalFileName(fileName);

    }


    public ResponseEntity<Resource> downloadExcelGroupTaskExcelForDBRead(Long excelGroupId) throws IOException {

        FileSystemStorageImpl fileSystemStorageImpl = new FileSystemStorageImpl(relativeExcelGroupTaskDirectoryPath);

        ExcelGroupTask excelGroupTask = excelGroupTaskRepositorySupport.findById(excelGroupId);

        // Determine the content type based on the file extension
        String fileExt = excelGroupTask.getSavedFileExt();
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


}
