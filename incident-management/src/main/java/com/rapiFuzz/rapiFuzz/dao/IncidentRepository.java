package com.rapiFuzz.rapiFuzz.dao;

import com.rapiFuzz.rapiFuzz.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IncidentRepository extends JpaRepository<Incident , Long> {

    Optional<Incident> findByIncidentId(String incidentId);

}
