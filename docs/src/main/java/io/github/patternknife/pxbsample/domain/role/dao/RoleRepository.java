package io.github.patternknife.pxbsample.domain.role.dao;

import io.github.patternknife.pxbsample.domain.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {

}