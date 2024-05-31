package io.github.patternknife.pxb.domain.exceldbreadtask.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.patternknife.pxb.config.response.error.exception.data.ResourceNotFoundException;
import io.github.patternknife.pxb.domain.common.dto.DateRangeFilter;
import io.github.patternknife.pxb.domain.common.dto.SorterValueFilter;
import io.github.patternknife.pxb.domain.excelcommontask.dto.ExcelCommonTaskResDTO;
import io.github.patternknife.pxb.domain.excelcommontask.dto.QExcelCommonTaskResDTO_OneRes;
import io.github.patternknife.pxb.domain.excelcommontask.dto.QExcelCommonTaskResDTO_StartEndTimestampRes;
import io.github.patternknife.pxb.domain.exceldbreadtask.dto.ExcelDBReadTaskSearchFilter;
import io.github.patternknife.pxb.domain.exceldbreadtask.entity.ExcelDBReadTask;
import io.github.patternknife.pxb.domain.exceldbreadtask.entity.QExcelDBReadTask;
import io.github.patternknife.pxb.domain.exceldbreadtask.queue.ExcelDBReadTaskEventDomain;
import io.github.patternknife.pxb.domain.exceldbwritetask.queue.ExcelDBWriteTaskEventDomain;
import io.github.patternknife.pxb.util.CustomUtils;
import io.github.patternknife.pxb.util.PaginationUtil;
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
public class ExcelDBReadTaskRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    private final ExcelDBReadTaskRepository excelDBReadTaskRepository;


    public ExcelDBReadTaskRepositorySupport(JPAQueryFactory jpaQueryFactory,
                                            ExcelDBReadTaskRepository excelDBReadTaskRepository) {

        super(ExcelDBReadTask.class);
        this.excelDBReadTaskRepository = excelDBReadTaskRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public ExcelDBReadTask findById(Long id) throws ResourceNotFoundException {
        return excelDBReadTaskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Excel Group ID Not Found. :: " + id));
    }

    @Transactional
    public ExcelDBReadTaskEventDomain save(ExcelDBReadTaskEventDomain excelTaskEventDomain) {
        excelDBReadTaskRepository.save(ExcelDBReadTask.from(excelTaskEventDomain));
        return excelTaskEventDomain;
    }

    @Transactional
    public ExcelDBReadTask update(ExcelDBReadTaskEventDomain excelTaskEventDomain) {
        return excelDBReadTaskRepository.findById(excelTaskEventDomain.getId())
                .orElseThrow(() -> new ResourceNotFoundException("id (" + excelTaskEventDomain.getId() + ") NOT found."))
                .update(excelTaskEventDomain);
    }


    @Transactional( readOnly = true)
    public Page<ExcelCommonTaskResDTO.OneRes> findExcelDBReadTasks(Boolean skipPagination,
                                                                   Integer pageNum,
                                                                   Integer pageSize,
                                                                   String excelDBReadTaskSearchFilter,
                                                                   String sorterValueFilter,
                                                                   String dateRangeFilter, Long groupId) throws JsonProcessingException, ResourceNotFoundException {

        final QExcelDBReadTask qExcelDBReadTask = QExcelDBReadTask.excelDBReadTask;

        JPQLQuery<ExcelCommonTaskResDTO.OneRes> query = jpaQueryFactory
                .select(new QExcelCommonTaskResDTO_OneRes(qExcelDBReadTask.id, qExcelDBReadTask.groupId,
                        qExcelDBReadTask.startRow, qExcelDBReadTask.endRow, qExcelDBReadTask.description,
                        qExcelDBReadTask.status, qExcelDBReadTask.errorMessage, qExcelDBReadTask.errorCount,
                        qExcelDBReadTask.createdAt, qExcelDBReadTask.updatedAt))
                .from(qExcelDBReadTask);

        if(groupId != null){
            query.where(qExcelDBReadTask.groupId.eq(groupId));
        }

        ObjectMapper objectMapper = new ObjectMapper();

        if(!CustomUtils.isEmpty(excelDBReadTaskSearchFilter)) {

            ExcelDBReadTaskSearchFilter deserializedExcelDBReadTaskSearchFilter = (ExcelDBReadTaskSearchFilter) objectMapper.readValue(excelDBReadTaskSearchFilter, ExcelDBReadTaskSearchFilter.class);

            if (!CustomUtils.isEmpty(deserializedExcelDBReadTaskSearchFilter.getStatus())) {
                query.where(qExcelDBReadTask.status.eq(deserializedExcelDBReadTaskSearchFilter.getStatus()));
            }
        }


        if(!CustomUtils.isEmpty(dateRangeFilter)) {
            DateRangeFilter deserializedDateRangeFilter = (DateRangeFilter) objectMapper.readValue(dateRangeFilter, DateRangeFilter.class);
            if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getColumn())) {
                if ("createdAt".equals(deserializedDateRangeFilter.getColumn())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();

                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        Timestamp startTimestamp = Timestamp.valueOf(deserializedDateRangeFilter.getStartDate() + " 00:00:00");
                        booleanBuilder.and(qExcelDBReadTask.createdAt.after(startTimestamp));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        Timestamp endTimestamp = Timestamp.valueOf(deserializedDateRangeFilter.getEndDate() + " 23:59:59");
                        booleanBuilder.and(qExcelDBReadTask.createdAt.before(endTimestamp));
                    }

                    query.where(booleanBuilder);

                }else if ("updatedAt".equals(deserializedDateRangeFilter.getColumn())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();

                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        Timestamp startTimestamp = Timestamp.valueOf(deserializedDateRangeFilter.getStartDate() + " 00:00:00");
                        booleanBuilder.and(qExcelDBReadTask.updatedAt.after(startTimestamp));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        Timestamp endTimestamp = Timestamp.valueOf(deserializedDateRangeFilter.getEndDate() + " 23:59:59");
                        booleanBuilder.and(qExcelDBReadTask.updatedAt.before(endTimestamp));
                    }

                    query.where(booleanBuilder);

                } else {
                    throw new IllegalStateException("Invalid Date range");
                }
            }

        }


        if(!CustomUtils.isEmpty(sorterValueFilter)) {
            SorterValueFilter deserializedSorterValueFilter = (SorterValueFilter) objectMapper.readValue(sorterValueFilter, SorterValueFilter.class);

            String sortedColumn = deserializedSorterValueFilter.getColumn();

            switch (sortedColumn) {
                case "id":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBReadTask.id.asc() : qExcelDBReadTask.id.desc());
                    break;
                case "groupId":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBReadTask.groupId.asc() : qExcelDBReadTask.groupId.desc());
                    break;
                case "startRow":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBReadTask.startRow.asc() : qExcelDBReadTask.startRow.desc());
                    break;
                case "endRow":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBReadTask.endRow.asc() : qExcelDBReadTask.endRow.desc());
                    break;
                case "description":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBReadTask.description.asc() : qExcelDBReadTask.description.desc());
                    break;
                case "status":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBReadTask.status.asc() : qExcelDBReadTask.status.desc());
                    break;
                case "errorMessage":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBReadTask.errorMessage.asc() : qExcelDBReadTask.errorMessage.desc());
                    break;
                case "errorCount":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBReadTask.errorCount.asc() : qExcelDBReadTask.errorCount.desc());
                    break;
                case "createdAt":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBReadTask.createdAt.asc() : qExcelDBReadTask.createdAt.desc());
                    break;
                case "updatedAt":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qExcelDBReadTask.updatedAt.asc() : qExcelDBReadTask.updatedAt.desc());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sorting column :: " + sortedColumn);
            }
        }


        PaginationUtil paginationUtil = new PaginationUtil(getQuerydsl());
        return paginationUtil.applyPagination(query, pageNum, pageSize, skipPagination);
    }


    @Transactional( readOnly = true)
    public ExcelCommonTaskResDTO.StatusRes findExcelDBReadTaskCountsByStatus(Long groupId) throws ResourceNotFoundException {
        return new ExcelCommonTaskResDTO.StatusRes(
                excelDBReadTaskRepository.countByStatusAndGroupId(ExcelDBWriteTaskEventDomain.ExcelTaskStatus.SUCCESS.getValue(), groupId),
                excelDBReadTaskRepository.countByStatusAndGroupId(ExcelDBWriteTaskEventDomain.ExcelTaskStatus.FAILURE.getValue(), groupId),
                excelDBReadTaskRepository.countByStatusAndGroupId(ExcelDBWriteTaskEventDomain.ExcelTaskStatus.PROGRESS.getValue(), groupId),
                excelDBReadTaskRepository.countByStatusesAndGroupId(
                        Arrays.asList(ExcelDBWriteTaskEventDomain.ExcelTaskStatus.QUEUE.getValue(),
                                ExcelDBWriteTaskEventDomain.ExcelTaskStatus.QUEUE_WAIT.getValue(),
                                ExcelDBWriteTaskEventDomain.ExcelTaskStatus.STANDBY.getValue()),groupId)
        );
    }


    @Transactional( readOnly = true)
    public ExcelCommonTaskResDTO.StartEndTimestampRes findStartEndTimestampsByGroupId(Long groupId){
        final QExcelDBReadTask qExcelDBReadTask = QExcelDBReadTask.excelDBReadTask;

        JPQLQuery<ExcelCommonTaskResDTO.StartEndTimestampRes> query = jpaQueryFactory
                .select(new QExcelCommonTaskResDTO_StartEndTimestampRes(qExcelDBReadTask.createdAt.min(), qExcelDBReadTask.updatedAt.max()))
                .from(qExcelDBReadTask).where(qExcelDBReadTask.groupId.eq(groupId));

        return query.fetchOne();

    }

}
