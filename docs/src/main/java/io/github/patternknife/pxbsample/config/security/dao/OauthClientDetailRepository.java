package io.github.patternknife.pxbsample.config.security.dao;

import io.github.patternknife.pxbsample.config.security.entity.OauthClientDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface OauthClientDetailRepository extends JpaRepository<OauthClientDetail, String> {

    Optional<OauthClientDetail> findByClientIdAndResourceIds(String clientId, String resourceIds);

}