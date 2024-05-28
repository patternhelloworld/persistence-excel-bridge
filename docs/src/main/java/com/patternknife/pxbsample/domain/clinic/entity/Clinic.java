package com.patternknife.pxbsample.domain.clinic.entity;

import com.patternknife.pxbsample.domain.clinic.dto.ClinicReqDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Table(name="clinic")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@DynamicUpdate
public class Clinic implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private Integer status;


    @Column(name = "phone_number")
    private String phoneNumber;


    @Column(name="created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;


    public void updateClinic(ClinicReqDTO.CreateOrUpdateOne dto) {
        this.name = dto.getName();
        this.phoneNumber = dto.getPhoneNumber();
    }


}

