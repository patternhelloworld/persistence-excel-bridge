package com.patternknife.pxbsample.domain.role.dao;

import com.patternknife.pxbsample.domain.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {

}