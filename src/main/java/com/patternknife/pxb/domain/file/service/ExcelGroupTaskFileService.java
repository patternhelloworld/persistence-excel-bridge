package com.patternknife.pxb.domain.file.service;


import com.patternknife.pxb.domain.exceldbprocessor.factory.IExcelDBProcessorFactory;
import com.patternknife.pxb.domain.exceldbprocessor.processor.ExcelDBWriteProcessor;
import com.patternknife.pxb.domain.excelgrouptask.dao.ExcelGroupTaskRepositorySupport;
import com.patternknife.pxb.domain.excelgrouptask.entity.ExcelGroupTask;
import com.patternknife.pxb.domain.file.bo.ExcelGroupTaskFileBO;
import com.patternknife.pxb.domain.file.util.FileUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ExcelGroupTaskFileService {

    private final ExcelGroupTaskFileBO excelGroupTaskFileBO;
    private final ExcelGroupTaskRepositorySupport excelGroupTaskRepositorySupport;
    private final IExcelDBProcessorFactory excelDBProcessorFactory;

    public ExcelGroupTaskFileService(ExcelGroupTaskFileBO excelGroupTaskFileBO, ExcelGroupTaskRepositorySupport excelGroupTaskRepositorySupport, IExcelDBProcessorFactory excelDBProcessorFactory) {
        this.excelGroupTaskFileBO = excelGroupTaskFileBO;
        this.excelGroupTaskRepositorySupport = excelGroupTaskRepositorySupport;
        this.excelDBProcessorFactory = excelDBProcessorFactory;
    }


    public Resource getExcelGroupTaskExcelBinaryById(Long excelGroupId) throws IOException {

        ExcelGroupTask excelGroupTask = excelGroupTaskRepositorySupport.findById(excelGroupId);

        return excelGroupTaskFileBO.getExcelGroupTaskExcelFile(excelGroupTask.getId(), excelGroupTask.getSavedFileExt());

    }

    @Transactional(rollbackFor=Exception.class)
    public void createEmptyExcelDBReadGroupTaskExcel(Long id) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        workbook.createSheet("Sheet1");

        String fileName = id + ".xlsx";
        excelGroupTaskFileBO.createExcelDBReadGroupTaskExcel(fileName, workbook);

        // Persist
        ExcelGroupTask excelGroupTask = excelGroupTaskRepositorySupport.findById(id);
        excelGroupTask.setSavedFileExt("xlsx");
        excelGroupTask.setOriginalFileName(fileName);

    }



    @Transactional(rollbackFor=Exception.class)
    public Boolean uploadExcelGroupTaskExcel(Long id, MultipartFile file) throws IOException {

        ExcelGroupTask excelGroupTask = excelGroupTaskRepositorySupport.findById(id);

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            ((ExcelDBWriteProcessor)excelDBProcessorFactory.getProcessor(excelGroupTask.getId())).validateColumnsBeforeDBWrite(workbook);

        } catch (IOException e) {
            throw e;
        }

        excelGroupTaskFileBO.uploadExcelGroupTaskExcel(id, file);

        excelGroupTask.setSavedFileExt(FileUtil.getFileExtension(file));
        excelGroupTask.setOriginalFileName(file.getOriginalFilename());

        return true;
    }

    public ResponseEntity<Resource> downloadExcelGroupTaskExcelForDBRead(Long excelGroupId) throws IOException {

        ExcelGroupTask excelGroupTask = excelGroupTaskRepositorySupport.findById(excelGroupId);

        return excelGroupTaskFileBO.downloadExcelGroupTaskExcelForDBRead(excelGroupId, excelGroupTask.getSavedFileExt());
    }

}
