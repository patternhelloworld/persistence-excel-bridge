package com.patternknife.pxbsample.domain.clinic.dao;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patternknife.pxbsample.config.database.CommonQuerydslRepositorySupport;
import com.patternknife.pxbsample.config.response.error.exception.data.ResourceNotFoundException;
import com.patternknife.pxbsample.domain.clinic.dto.*;
import com.patternknife.pxbsample.domain.clinic.entity.Clinic;
import com.patternknife.pxbsample.domain.clinic.entity.QClinic;
import com.patternknife.pxbsample.domain.common.dto.DateRangeFilter;
import com.patternknife.pxbsample.domain.common.dto.SorterValueFilter;
import com.patternknife.pxbsample.util.CustomUtils;
import com.patternknife.pxbsample.util.PaginationUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

/*
*
*   QueryDsl 을 써야하는 경우 = 다른 엔터티들 Join, Group by, having... + 동적 where
*
*   RepositorySupport 에서는 다른 Repository 호출 가능
* */
@Repository
public class ClinicRepositorySupport extends CommonQuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    private final ClinicRepository clinicRepository;

    private final String dbDialect;
    private final JdbcTemplate jdbcTemplate;

    public ClinicRepositorySupport(@Qualifier("authJpaQueryFactory") JPAQueryFactory jpaQueryFactory, ClinicRepository clinicRepository,
                                   @Value("${spring.jpa.properties.hibernate.dialect}") String dbDialect,
                                   JdbcTemplate jdbcTemplate) {

        super(Clinic.class);
        this.clinicRepository = clinicRepository;
        this.jpaQueryFactory = jpaQueryFactory;
        this.dbDialect = dbDialect;
        this.jdbcTemplate = jdbcTemplate;
    }



    public Page<ClinicResDTO.OneDetails> findDetailsByPageAndFilter(Boolean skipPagination,
                                                                    Integer pageNum,
                                                                    Integer pageSize,
                                                                    String clinicSearchFilter,
                                                                    String sorterValueFilter,
                                                                    String dateRangeFilter) throws JsonProcessingException, ResourceNotFoundException {

        final QClinic qClinic = QClinic.clinic;


        JPQLQuery<ClinicResDTO.OneDetails> query = jpaQueryFactory
                .select(new QClinicResDTO_OneDetails(qClinic.id, qClinic.name, qClinic.phoneNumber,
                        qClinic.createdAt, qClinic.updatedAt, qClinic.deletedAt))
                .from(qClinic);

        JPQLQuery<Long> countQuery = jpaQueryFactory
                .select(qClinic.id)
                .from(qClinic);

        ObjectMapper objectMapper = new ObjectMapper();

        if(!CustomUtils.isEmpty(clinicSearchFilter)) {

            ClinicSearchFilter deserializedClinicSearchFilter = (ClinicSearchFilter) objectMapper.readValue(clinicSearchFilter, ClinicSearchFilter.class);

            if (!CustomUtils.isEmpty(deserializedClinicSearchFilter.getName())) {
                query.where(qClinic.name.likeIgnoreCase("%" + deserializedClinicSearchFilter.getName() + "%"));
                countQuery.where(qClinic.name.likeIgnoreCase("%" + deserializedClinicSearchFilter.getName() + "%"));
            }
            if (!CustomUtils.isEmpty(deserializedClinicSearchFilter.getPhoneNumber())) {
                query.where(qClinic.phoneNumber.likeIgnoreCase("%" + deserializedClinicSearchFilter.getPhoneNumber() + "%"));
                countQuery.where(qClinic.phoneNumber.likeIgnoreCase("%" + deserializedClinicSearchFilter.getPhoneNumber() + "%"));
            }


            if (!CustomUtils.isEmpty(deserializedClinicSearchFilter.getMaxId())) {
                query.where(qClinic.id.loe(deserializedClinicSearchFilter.getMaxId()));
                countQuery.where(qClinic.id.loe(deserializedClinicSearchFilter.getMaxId()));
            }

        }


        if(!CustomUtils.isEmpty(dateRangeFilter)) {
            DateRangeFilter deserializedDateRangeFilter = (DateRangeFilter) objectMapper.readValue(dateRangeFilter, DateRangeFilter.class);
            if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getColumn())) {
                if ("createdAt".equals(deserializedDateRangeFilter.getColumn())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();

                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        Timestamp startTimestamp = Timestamp.valueOf(deserializedDateRangeFilter.getStartDate() + " 00:00:00");
                        booleanBuilder.and(qClinic.createdAt.after(startTimestamp));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        Timestamp endTimestamp = Timestamp.valueOf(deserializedDateRangeFilter.getEndDate() + " 23:59:59");
                        booleanBuilder.and(qClinic.createdAt.before(endTimestamp));
                    }

                    query.where(booleanBuilder);
                    countQuery.where(booleanBuilder);

                }else if ("updatedAt".equals(deserializedDateRangeFilter.getColumn())) {

                    BooleanBuilder booleanBuilder = new BooleanBuilder();

                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getStartDate())) {
                        Timestamp startTimestamp = Timestamp.valueOf(deserializedDateRangeFilter.getStartDate() + " 00:00:00");
                        booleanBuilder.and(qClinic.updatedAt.after(startTimestamp));
                    }
                    if (!CustomUtils.isEmpty(deserializedDateRangeFilter.getEndDate())) {
                        Timestamp endTimestamp = Timestamp.valueOf(deserializedDateRangeFilter.getEndDate() + " 23:59:59");
                        booleanBuilder.and(qClinic.updatedAt.before(endTimestamp));
                    }

                    query.where(booleanBuilder);
                    countQuery.where(booleanBuilder);

                } else {
                    throw new IllegalStateException("유효하지 않은 Date range 검색 대상입니다.");
                }
            }

        }


        if(!CustomUtils.isEmpty(sorterValueFilter)) {
            SorterValueFilter deserializedSorterValueFilter = (SorterValueFilter) objectMapper.readValue(sorterValueFilter, SorterValueFilter.class);

            String sortedColumn = deserializedSorterValueFilter.getColumn();

            switch (sortedColumn) {
                case "id":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qClinic.id.asc() : qClinic.id.desc());
                    break;
                case "name":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qClinic.name.asc() : qClinic.name.desc());
                    break;
                case "phoneNumber":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qClinic.phoneNumber.asc() : qClinic.phoneNumber.desc());
                    break;
                case "createdAt":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qClinic.createdAt.asc() : qClinic.createdAt.desc());
                    break;
                case "updatedAt":
                    query.orderBy(deserializedSorterValueFilter.getAsc() ? qClinic.updatedAt.asc() : qClinic.updatedAt.desc());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sorting column : " + sortedColumn);
            }
        }

        // Pagination
        PaginationUtil paginationUtil = new PaginationUtil(getQuerydsl());
        return paginationUtil.applyPagination(query, pageNum, pageSize, skipPagination, countQuery.fetchCount());

    }

    /*
        2. Create
    * */
    public Clinic create(Clinic clinic){
        return clinicRepository.save(clinic);
    }


    public ClinicResDTO.Id update(Long id, ClinicReqDTO.CreateOrUpdateOne dto) {

        final Clinic clinic = clinicRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("NOT found Clinic (ID : '" + id + "')"));

        clinic.updateClinic(dto);

        return new ClinicResDTO.Id(clinic);
    }



}
