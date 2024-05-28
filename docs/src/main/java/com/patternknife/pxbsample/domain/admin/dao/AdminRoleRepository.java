package com.patternknife.pxbsample.domain.admin.dao;

import com.patternknife.pxbsample.domain.role.entity.Role;
import com.patternknife.pxbsample.domain.admin.entity.Admin;
import com.patternknife.pxbsample.domain.admin.entity.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRoleRepository extends JpaRepository<AdminRole, Long> {
    Optional<AdminRole> findByAdminAndRole(Admin admin, Role role);

    @Modifying
    @Query("DELETE FROM AdminRole a WHERE a.admin = :admin")
    void deleteByAdmin(Admin admin);


    Optional<List<AdminRole>> findByAdmin(Admin admin);
}
