package com.patternknife.pxbsample.domain.customer.dao;

import com.patternknife.pxbsample.domain.customer.entity.CustomerRole;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRoleRepository extends JpaRepository<CustomerRole, Long> {

}
