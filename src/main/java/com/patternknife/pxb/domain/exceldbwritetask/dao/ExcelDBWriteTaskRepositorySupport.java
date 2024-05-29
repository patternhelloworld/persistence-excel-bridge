package com.patternknife.pxb.domain.exceldbwritetask.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternknife.pxb.config.response.error.exception.data.ResourceNotFoundException;
import com.patternknife.pxb.domain.common.dto.DateRangeFilter;
import com.patternknife.pxb.domain.common.dto.SorterValueFilter;
import com.patternknife.pxb.domain.excelcommontask.dto.ExcelCommonTaskResDTO;
import com.patternknife.pxb.domain.excelcommontask.dto.QExcelCommonTaskResDTO_OneRes;
import com.patternknife.pxb.domain.excelcommontask.dto.QExcelCommonTaskResDTO_StartEndTimestampRes;
import com.patternknife.pxb.domain.exceldbwritetask.dto.ExcelDBWriteTaskSearchFilter;
import com.patternknife.pxb.domain.exceldbwritetask.entity.ExcelDBWriteTask;
import com.patternknife.pxb.domain.exceldbwritetask.entity.QExcelDBWriteTask;
import com.patternknife.pxb.domain.exceldbwritetask.queue.ExcelDBWriteTaskEventDomain;
import com.patternknife.pxb.util.CustomUtils;
import com.patternknife.pxb.util.PaginationUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Arrays;


@Repository
public class ExcelDBWriteTaskRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    private final ExcelDBWriteTaskRepository excelDBWriteTaskRepository;


    public ExcelDBWriteTaskRepositorySupport(JPAQueryFactory jpaQueryFactory,
                                             ExcelDBWriteTaskRepository excelDBWriteTaskRepository) {

        super(ExcelDBWriteTask.class);
        this.excelDBWriteTaskRepository = excelDBWriteTaskRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public ExcelDBWriteTask findById(Long id) throws ResourceNotFoundException {
        return excelDBWriteTaskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("NOT found Excel Group ID :: " + id));
    }

    @Transactional
    public ExcelDBWriteTaskEventDomain save(ExcelDBWriteTaskEventDomain excelTaskEventDomain) {
        excelDBWriteTaskRepository.save(ExcelDBWriteTask.from(excelTaskEventDomain));
        return excelTaskEventDomain;
    }

    @Transactional
    public ExcelDBWriteTask update(ExcelDBWriteTaskEventDomain excelTaskEventDomain) {
        return excelDBWriteTaskRepository.findById(excelTaskEventDomain.getId())
                .orElseThrow(() -> new ResourceNotFoundException("id (" + excelTaskEventDomain.getId() + ") NOT found."))
                .update(excelTaskEventDomain);
    }


    @Transactional( readOnly = true)
    public Page<ExcelCommonTaskResDTO.OneRes> findExcelDBWriteTasksByPageAndFilterAndGroupId(Boolean skipPagination,
                                                                                             Integer pageNum,
                                                                                             Integer pageSize,
                                                                                             String excelDBWriteTaskSearchFilter,
                                                                                             String sorterValueFilter,
                                                                                             String dateRangeFilter, Long groupId) throws JsonProcessingException, ResourceNotFoundException {

        final QExcelDBWriteTask qExcelDBWriteTask = QExcelDBWriteTask.excelDBWriteTask;

        JPQLQuery<ExcelCommonTaskResDTO.OneRes> query = jpaQueryFactory
                .select(new QExcelCommonTaskResDTO_OneRes(qExcelDBWriteTask.id, qExcelDBWriteTask.groupId,
                        qExcelDBWriteTask.startRow, qExcelDBWriteTask.endRow, qExcelDBWriteTask.description,
                        qExcelDBWriteTask.status, qExcelDBWriteTask.errorMessage, qExcelDBWriteTask.errorCount,
                        qExcelDBWriteTask.createdAt, qExcelDBWriteTask.updatedAt))
                .from(qExcelDBWriteTask);

        if(groupId != null){
            query.where(qExcelDBWriteTask.groupId.eq(groupId));
        }

        ObjectMapper objectMapper = new ObjectMapper();

        if(!CustomUtils.isEmpty(excelDBWriteTaskSearchFilter)) {

            ExcelDBWriteTaskSearchFilter deserializedExcelDBWriteTaskSearchFilter = (ExcelDBWriteTaskSearchFilter) objectMapper.readValue(excelDBWriteTaskSearchFilter, ExcelDBWriteTaskSearchFilter.class);

            if (!CustomUtils.isEmpty(deserializedExcelDBWriteTaskSearchFilter.getStatus())) {
                query.where(qExcelDBWriteTask.status.eq(deserializedExcelDBWriteTaskSearchFilter.getStatus()));
            }
        }


        if(!CustomUtils.isEmpty(dateRangeFilter)) {
            DateRangeFilter deserializedDateRangeFilter = (DateRangeFilter) objectMapper.readValue(dateRangeFilter, DateRangeFilter.class);
            if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getColumn())) {
                if ("createdAt".equals(deserializedDateRangeFilter.getColumn())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();

                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        Timestamp startTimestamp = Timestamp.valueOf(deserializedDateRangeFilter.getStartDate() + " 00:00:00");
                        booleanBuilder.and(qExcelDBWriteTask.createdAt.after(startTimestamp));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        Timestamp endTimestamp = Timestamp.valueOf(deserializedDateRangeFilter.getEndDate() + " 23:59:59");
                        booleanBuilder.and(qExcelDBWriteTask.createdAt.before(endTimestamp));
                    }

                    query.where(booleanBuilder);

                }else if ("updatedAt".equals(deserializedDateRangeFilter.getColumn())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();

                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        Timestamp startTimestamp = Timestamp.valueOf(deserializedDateRangeFilter.getStartDate() + " 00:00:00");
                        booleanBuilder.and(qExcelDBWriteTask.updatedAt.after(startTimestamp));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        Timestamp endTimestamp = Timestamp.valueOf(deserializedDateRangeFilter.getEndDate() + " 23:59:59");
                        booleanBuilder.and(qExcelDBWriteTask.updatedAt.before(endTimestamp));
                    }

                    query.where(booleanBuilder);

                } else {
                    throw new IllegalStateException("Invalid date range.");
                }
            }

        }


        if(!CustomUtils.isEmpty(sorterValueFilter)) {
            SorterValueFilter deserializedSorterValueFilter = (SorterValueFilter) objectMapper.readValue(sorterValueFilter, SorterValueFilter.class);

            String sortedColumn = deserializedSorterValueFilter.getColumn();

            switch (sortedColumn) {
                case "id":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBWriteTask.id.asc() : qExcelDBWriteTask.id.desc());
                    break;
                case "groupId":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBWriteTask.groupId.asc() : qExcelDBWriteTask.groupId.desc());
                    break;
                case "startRow":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBWriteTask.startRow.asc() : qExcelDBWriteTask.startRow.desc());
                    break;
                case "endRow":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBWriteTask.endRow.asc() : qExcelDBWriteTask.endRow.desc());
                    break;
                case "description":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBWriteTask.description.asc() : qExcelDBWriteTask.description.desc());
                    break;
                case "status":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBWriteTask.status.asc() : qExcelDBWriteTask.status.desc());
                    break;
                case "errorMessage":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBWriteTask.errorMessage.asc() : qExcelDBWriteTask.errorMessage.desc());
                    break;
                case "errorCount":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBWriteTask.errorCount.asc() : qExcelDBWriteTask.errorCount.desc());
                    break;
                case "createdAt":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBWriteTask.createdAt.asc() : qExcelDBWriteTask.createdAt.desc());
                    break;
                case "updatedAt":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBWriteTask.updatedAt.asc() : qExcelDBWriteTask.updatedAt.desc());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sorting column :: " + sortedColumn);
            }
        }


        PaginationUtil paginationUtil = new PaginationUtil(getQuerydsl());
        return paginationUtil.applyPagination(query, pageNum, pageSize, skipPagination);
    }


    @Transactional( readOnly = true)
    public ExcelCommonTaskResDTO.StatusRes findExcelDBWriteTaskCountsByStatus(Long groupId) throws ResourceNotFoundException {
        return new ExcelCommonTaskResDTO.StatusRes(
                excelDBWriteTaskRepository.countByStatusAndGroupId(ExcelDBWriteTaskEventDomain.ExcelTaskStatus.SUCCESS.getValue(), groupId),
                excelDBWriteTaskRepository.countByStatusAndGroupId(ExcelDBWriteTaskEventDomain.ExcelTaskStatus.FAILURE.getValue(), groupId),
                excelDBWriteTaskRepository.countByStatusAndGroupId(ExcelDBWriteTaskEventDomain.ExcelTaskStatus.PROGRESS.getValue(), groupId),
                excelDBWriteTaskRepository.countByStatusesAndGroupId(
                        Arrays.asList(ExcelDBWriteTaskEventDomain.ExcelTaskStatus.QUEUE.getValue(),
                                ExcelDBWriteTaskEventDomain.ExcelTaskStatus.QUEUE_WAIT.getValue(),
                                ExcelDBWriteTaskEventDomain.ExcelTaskStatus.STANDBY.getValue()),groupId)
        );
    }


    @Transactional( readOnly = true)
    public ExcelCommonTaskResDTO.StartEndTimestampRes findStartEndTimestampsByGroupId(Long groupId){
        final QExcelDBWriteTask qExcelDBWriteTask = QExcelDBWriteTask.excelDBWriteTask;

        JPQLQuery<ExcelCommonTaskResDTO.StartEndTimestampRes> query = jpaQueryFactory
                .select(new QExcelCommonTaskResDTO_StartEndTimestampRes(qExcelDBWriteTask.createdAt.min(), qExcelDBWriteTask.updatedAt.max()))
                .from(qExcelDBWriteTask).where(qExcelDBWriteTask.groupId.eq(groupId));

        return query.fetchOne();

    }

}
