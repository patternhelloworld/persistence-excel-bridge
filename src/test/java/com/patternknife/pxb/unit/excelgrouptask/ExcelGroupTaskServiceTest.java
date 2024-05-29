package com.patternknife.pxb.unit.excelgrouptask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.patternknife.pxb.domain.exceldbprocessor.factory.IExcelDBProcessorFactory;
import com.patternknife.pxb.domain.exceldbprocessor.processor.ExcelDBReadProcessor;
import com.patternknife.pxb.domain.exceldbreadtask.dao.ExcelDBReadTaskRepository;
import com.patternknife.pxb.domain.exceldbreadtask.queue.ExcelDBReadEventPublisher;
import com.patternknife.pxb.domain.exceldbreadtask.queue.ExcelDBReadTaskEventDomain;
import com.patternknife.pxb.domain.exceldbreadtask.queue.ExcelDBReadTaskEventQueue;
import com.patternknife.pxb.domain.exceldbwritetask.dao.ExcelDBWriteTaskRepository;
import com.patternknife.pxb.domain.exceldbwritetask.queue.ExcelDBWriteEventPublisher;
import com.patternknife.pxb.domain.excelgrouptask.dao.ExcelGroupTaskRepository;
import com.patternknife.pxb.domain.excelgrouptask.dao.ExcelGroupTaskRepositorySupport;
import com.patternknife.pxb.domain.excelgrouptask.dto.ExcelGroupTaskReqDTO;
import com.patternknife.pxb.domain.excelgrouptask.dto.ExcelGroupTaskResDTO;
import com.patternknife.pxb.domain.excelgrouptask.entity.ExcelGroupTask;
import com.patternknife.pxb.domain.excelgrouptask.service.ExcelGroupTaskService;
import com.patternknife.pxb.domain.file.service.ExcelGroupTaskFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExcelGroupTaskServiceTest {

    private ExcelGroupTaskService excelGroupTaskService;

    @Mock
    private ExcelGroupTaskRepositorySupport excelGroupTaskRepositorySupport;

    @Mock
    private ExcelGroupTaskRepository excelGroupTaskRepository;

    @Mock
    private ExcelDBWriteTaskRepository excelDBWriteTaskRepository;

    @Mock
    private ExcelDBReadTaskRepository excelDBReadTaskRepository;

    @Mock
    private ExcelDBWriteEventPublisher excelDBWriteEventPublisher;

    @Mock
    private ExcelDBReadEventPublisher dbReadEventPublisher;

    @Mock
    private IExcelDBProcessorFactory excelDBProcessorFactory;

    @Mock
    private ExcelGroupTaskFileService excelGroupTaskFileService;

    @Mock
    private ExcelDBReadTaskEventQueue eventQueue;


    @BeforeTestMethod
    public void beforeMethod() {
    }

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        excelGroupTaskService = new ExcelGroupTaskService(
                excelGroupTaskRepositorySupport,
                excelGroupTaskRepository,
                excelDBWriteTaskRepository,
                excelDBReadTaskRepository,
                excelDBWriteEventPublisher,
                dbReadEventPublisher,
                excelDBProcessorFactory,
                excelGroupTaskFileService,
                eventQueue
        );
    }

    @Test
    public void testCreateExcelGroupTask() {

        when(excelGroupTaskRepository.save(any())).thenReturn(ExcelGroupTask.builder().id(1L).build());

        ExcelGroupTaskResDTO.CreateOrUpdateRes result = excelGroupTaskService.createExcelGroupTask(new ExcelGroupTaskReqDTO.CreateReq(null, null, null));

        assertNotNull(result);
        assertEquals(1L, result.getId(), "id should match.");
        verify(excelGroupTaskRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateExcelGroupTask() throws Exception {

        when(excelGroupTaskRepositorySupport.setExcelGroupTaskStatusInProgress(anyLong())).thenReturn(true);
        when(excelDBWriteTaskRepository.existsByGroupIdAndStatusIn(anyLong(), anyList())).thenReturn(false);
        when(excelGroupTaskRepositorySupport.findById(anyLong())).thenReturn(ExcelGroupTask.builder().id(1L).build());

        ExcelGroupTaskResDTO.CreateOrUpdateRes result = excelGroupTaskService.updateExcelGroupTask(1L, new ExcelGroupTaskReqDTO.UpdateReq(null, null));

        assertNotNull(result);
        assertEquals(1L, result.getId(), "id should match.");

        verify(excelGroupTaskRepositorySupport, times(1)).setExcelGroupTaskStatusInProgress(anyLong());
        verify(excelDBWriteTaskRepository, times(1)).existsByGroupIdAndStatusIn(anyLong(), anyList());
        verify(excelGroupTaskRepositorySupport, times(1)).findById(anyLong());
        verify(excelGroupTaskRepositorySupport, times(1)).setExcelGroupTaskStatusNotInProgress(anyLong());
    }

    @Test
    public void testGetExcelDBWriteGroupTasks() throws JsonProcessingException {
        Page<ExcelGroupTaskResDTO.OneRes> mockPage = new PageImpl<>(Collections.emptyList());
        when(excelGroupTaskRepositorySupport.findExcelDBWriteGroupTasksByPageFilter(anyBoolean(), anyInt(), anyInt(), anyString(), anyString(), anyString())).thenReturn(mockPage);

        Page<ExcelGroupTaskResDTO.OneRes> result = excelGroupTaskService.getExcelDBWriteGroupTasks(true, 1, 10, "", "", "");

        assertNotNull(result);
        assertEquals(mockPage, result);
        verify(excelGroupTaskRepositorySupport, times(1)).findExcelDBWriteGroupTasksByPageFilter(anyBoolean(), anyInt(), anyInt(), anyString(), anyString(), anyString());
    }

    @Test
    public void testDeleteExcelDBWriteGroupTask() throws IOException {
        when(excelGroupTaskRepositorySupport.setExcelGroupTaskStatusInProgress(anyLong())).thenReturn(true);
        when(excelDBWriteTaskRepository.existsByGroupIdAndStatusIn(anyLong(), anyList())).thenReturn(false);

        Boolean result = excelGroupTaskService.deleteExcelDBWriteGroupTask(1L);

        assertTrue(result);
        verify(excelGroupTaskRepositorySupport, times(1)).setExcelGroupTaskStatusInProgress(anyLong());
        verify(excelDBWriteTaskRepository, times(1)).existsByGroupIdAndStatusIn(anyLong(), anyList());
        verify(excelDBWriteTaskRepository, times(1)).deleteByGroupId(anyLong());
        verify(excelGroupTaskRepository, times(1)).deleteById(anyLong());
        verify(excelGroupTaskRepositorySupport, times(1)).setExcelGroupTaskStatusNotInProgress(anyLong());
    }

    @Test
    public void testGetExcelDBReadGroupTasks() throws JsonProcessingException {
        Page<ExcelGroupTaskResDTO.OneRes> mockPage = new PageImpl<>(Collections.emptyList());
        when(excelGroupTaskRepositorySupport.findExcelDBReadGroupTasksByPageAndFilter(anyBoolean(), anyInt(), anyInt(), anyString(), anyString(), anyString())).thenReturn(mockPage);

        Page<ExcelGroupTaskResDTO.OneRes> result = excelGroupTaskService.getExcelDBReadGroupTasks(true, 1, 10, "", "", "");

        assertNotNull(result);
        assertEquals(mockPage, result);
        verify(excelGroupTaskRepositorySupport, times(1)).findExcelDBReadGroupTasksByPageAndFilter(anyBoolean(), anyInt(), anyInt(), anyString(), anyString(), anyString());
    }

    @Test
    public void testDeleteExcelDBReadGroupTask() throws IOException {
        when(excelGroupTaskRepositorySupport.setExcelGroupTaskStatusInProgress(anyLong())).thenReturn(true);
        when(excelDBReadTaskRepository.existsByGroupIdAndStatusIn(anyLong(), anyList())).thenReturn(false);

        Boolean result = excelGroupTaskService.deleteExcelDBReadGroupTask(1L);

        assertTrue(result);
        verify(excelGroupTaskRepositorySupport, times(1)).setExcelGroupTaskStatusInProgress(anyLong());
        verify(excelDBReadTaskRepository, times(1)).existsByGroupIdAndStatusIn(anyLong(), anyList());
        verify(excelDBReadTaskRepository, times(1)).deleteByGroupId(anyLong());
        verify(excelGroupTaskRepository, times(1)).deleteById(anyLong());
        verify(excelGroupTaskRepositorySupport, times(1)).setExcelGroupTaskStatusNotInProgress(anyLong());
    }

    @Test
    public void testCreateExcelDBReadGroupTask() throws Exception {
        Long taskId = 1L;
        ExcelGroupTaskReqDTO.DBReadGroupTaskCreateReq dto = new ExcelGroupTaskReqDTO.DBReadGroupTaskCreateReq(taskId);

        ExcelGroupTask excelGroupTask = ExcelGroupTask.builder()
                .id(taskId)
                .rowCountPerTask(10)
                .build();

        Resource mockResource = mock(Resource.class);
        when(mockResource.getFilename()).thenReturn("testfile.xlsx");
        when(mockResource.exists()).thenReturn(Boolean.TRUE);
        when(mockResource.getInputStream()).thenReturn(new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        });

        ExcelDBMaxIdResExtended excelDBMaxIdResExtended = new ExcelDBMaxIdResExtended();
        excelDBMaxIdResExtended.setId(1L);

        Page mockPage = new PageImpl<>(Collections.singletonList(excelDBMaxIdResExtended), PageRequest.of(0, 10), 10);

        when(excelGroupTaskRepositorySupport.findById(taskId)).thenReturn(excelGroupTask);
        when(excelGroupTaskRepositorySupport.setExcelGroupTaskStatusInProgress(taskId)).thenReturn(true);
        when(excelDBReadTaskRepository.existsByGroupIdAndStatusIn(eq(taskId), anyList())).thenReturn(false);
        when(excelGroupTaskFileService.getExcelGroupTaskExcelBinaryById(taskId)).thenReturn(mockResource);

        ExcelDBReadProcessor mockProcessor = mock(ExcelDBReadProcessor.class);

        when(excelDBProcessorFactory.getProcessor(taskId)).thenReturn(mockProcessor);
        when(mockProcessor.snapshotDBRead(anyInt())).thenReturn(mockPage);


        ExcelGroupTaskResDTO.CreateOrUpdateRes result = excelGroupTaskService.createExcelDBReadGroupTask(dto);

        assertNotNull(result);
        assertEquals(taskId, result.getId(), "id should match.");
        verify(excelGroupTaskRepositorySupport, times(1)).findById(anyLong());
        verify(excelGroupTaskRepositorySupport, times(1)).setExcelGroupTaskStatusInProgress(anyLong());
        verify(excelDBReadTaskRepository, times(1)).existsByGroupIdAndStatusIn(eq(taskId), anyList());
        verify(eventQueue, times(1)).initializeQueueForId(anyLong());
        verify(excelDBReadTaskRepository, times(1)).deleteByGroupId(anyLong());
        verify(excelGroupTaskFileService, times(1)).createEmptyExcelDBReadGroupTaskExcel(anyLong());
        verify(excelGroupTaskFileService, times(1)).getExcelGroupTaskExcelBinaryById(anyLong());
        verify(mockResource, times(3)).getFilename();
        verify(excelGroupTaskRepository, times(1)).save(any(ExcelGroupTask.class));
        verify(dbReadEventPublisher, times(mockPage.getTotalPages())).publish(any(ExcelDBReadTaskEventDomain.class));
        verify(excelGroupTaskRepositorySupport, times(1)).setExcelGroupTaskStatusNotInProgress(anyLong());
    }

}