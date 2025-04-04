package com.user_management.dao;

import com.user_management.entity.Profiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileDao extends JpaRepository<Profiles, Long> {
    @Override
    Optional<Profiles> findById(Long aLong);

}
