package com.patternknife.pxbsample.domain.clinic.dao;


import com.patternknife.pxbsample.domain.clinic.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface ClinicRepository extends JpaRepository<Clinic, Long>, QuerydslPredicateExecutor<Clinic> {
        Optional<Clinic> findClinicByPhoneNumber(String phoneNumber);
        Optional<List<Clinic>> findClinicsByName(String clinicName);
        Long countByName(String clinicName);
}