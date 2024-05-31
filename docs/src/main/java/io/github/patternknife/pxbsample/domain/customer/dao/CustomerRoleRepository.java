package io.github.patternknife.pxbsample.domain.customer.dao;

import io.github.patternknife.pxbsample.domain.customer.entity.CustomerRole;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRoleRepository extends JpaRepository<CustomerRole, Long> {

}
