package com.patternknife.pxb.domain.exceldbreadtask.entity;

import com.patternknife.pxb.domain.exceldbreadtask.queue.ExcelDBReadTaskEventDomain;
import com.patternknife.pxb.domain.excelgrouptask.entity.ExcelGroupTask;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@DynamicUpdate
@DynamicInsert
@Table(name = "excel_db_read_task")
public class ExcelDBReadTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private ExcelGroupTask excelGroupTask;

    @Column(name = "group_id", insertable = false, updatable = false)
    private Long groupId;

    @Getter
    @Column(name = "startRow")
    private Integer startRow;

    @Getter
    @Column(name = "endRow")
    private Integer endRow;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "status")
    @Getter
    private Integer status;

    @Column(name = "error_reason")
    private Byte errorReason;

    @Column(name = "warning_reason")
    private Byte warningReason;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "error_count")
    private int errorCount;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    public ExcelDBReadTask(Long id, Integer status) {
        this.id = id;
        this.status = status;
    }

    public static ExcelDBReadTask from(ExcelDBReadTaskEventDomain excelTaskEventDomain) {
        return new ExcelDBReadTask(excelTaskEventDomain.getId(), excelTaskEventDomain.getStatus().getValue());
    }

    public ExcelDBReadTask update(ExcelDBReadTaskEventDomain excelTaskEventDomain) {
        this.status = excelTaskEventDomain.getStatus().getValue();
        if(excelTaskEventDomain.getErrorMessage() != null) {
            this.errorMessage = excelTaskEventDomain.getErrorMessage();
            this.errorCount = excelTaskEventDomain.getErrorCount();
        }
        return this;
    }
}