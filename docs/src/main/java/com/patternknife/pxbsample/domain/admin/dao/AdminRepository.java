package com.patternknife.pxbsample.domain.admin.dao;

import com.patternknife.pxbsample.domain.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;


public interface AdminRepository extends JpaRepository<Admin, Long>, QuerydslPredicateExecutor<Admin> {
    Optional<Admin> findByIdName(String idName);
}