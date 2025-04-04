package com.user_management.dao;

import com.user_management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleDao extends JpaRepository<Role,Long> {
    Optional<Role> findByRoles(String roles);

}
