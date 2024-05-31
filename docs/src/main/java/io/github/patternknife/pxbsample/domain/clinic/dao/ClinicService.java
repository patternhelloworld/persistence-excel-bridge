package io.github.patternknife.pxbsample.domain.clinic.dao;


import io.github.patternknife.pxbsample.config.database.CommonQuerydslRepositorySupport;
import io.github.patternknife.pxbsample.domain.clinic.entity.Clinic;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class ClinicService extends CommonQuerydslRepositorySupport {


    private final JPAQueryFactory jpaQueryFactory;

    private final ClinicRepository clinicRepository;
    private final ClinicRepositorySupport clinicRepositorySupport;

    private EntityManager entityManager;

    public ClinicService(ClinicRepository clinicRepository, ClinicRepositorySupport clinicRepositorySupport,
                      @Qualifier("authJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(Clinic.class);
        this.clinicRepository = clinicRepository;
        this.clinicRepositorySupport = clinicRepositorySupport;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    @PersistenceContext(unitName = "commonEntityManager")
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
        this.entityManager = entityManager;
    }


}
