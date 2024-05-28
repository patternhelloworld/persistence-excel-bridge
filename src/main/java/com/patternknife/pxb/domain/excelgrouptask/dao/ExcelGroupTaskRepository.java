package com.patternknife.pxb.domain.excelgrouptask.dao;

import com.patternknife.pxb.domain.excelgrouptask.entity.ExcelGroupTask;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcelGroupTaskRepository extends JpaRepository<ExcelGroupTask, Long>, QuerydslPredicateExecutor<ExcelGroupTask> {

}