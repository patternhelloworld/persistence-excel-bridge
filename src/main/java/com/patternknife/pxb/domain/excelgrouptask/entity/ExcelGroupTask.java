package com.patternknife.pxb.domain.excelgrouptask.entity;

import com.patternknife.pxb.domain.excelgrouptask.dto.ExcelGroupTaskReqDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name="excel_group_task")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExcelGroupTask {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "read_or_write", nullable = false)
    private Integer readOrWrite;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Setter
    @Column(name = "row_count_per_task", nullable = false)
    private Integer rowCountPerTask;

    @Setter
    @Column(name = "total_row")
    private Integer totalRow;

    @Column(name = "description", length = 255)
    private String description;

    @Setter
    @Column(name = "saved_file_ext", length = 255)
    private String savedFileExt;

    @Setter
    @Column(name = "original_file_name", length = 255)
    private String originalFileName;

    @Setter
    @Column(name = "excel_updated_at")
    private LocalDateTime excelUpdatedAt;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;


    @Builder
    public ExcelGroupTask(Long id, Integer readOrWrite, Integer status, Integer rowCountPerTask, Integer totalRow, String description, String savedFileExt, String originalFileName, LocalDateTime excelUpdatedAt, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.readOrWrite = readOrWrite;
        this.status = status;
        this.rowCountPerTask = rowCountPerTask;
        this.totalRow = totalRow;
        this.description = description;
        this.savedFileExt = savedFileExt;
        this.originalFileName = originalFileName;
        this.excelUpdatedAt = excelUpdatedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public void updateStatus(ExcelGroupTaskReqDTO.UpdateStatusReq dto){
        this.status = dto.getStatus();
    }

    public void update(ExcelGroupTaskReqDTO.UpdateReq dto){
        this.rowCountPerTask = dto.getRowCountPerTask();
        this.description = dto.getDescription();
    }


}
