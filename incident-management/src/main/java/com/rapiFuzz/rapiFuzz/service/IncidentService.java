package com.rapiFuzz.rapiFuzz.service;

import com.rapiFuzz.rapiFuzz.dao.IncidentRepository;
import com.rapiFuzz.rapiFuzz.entity.Incident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class IncidentService {
    @Autowired
    private IncidentRepository incidentRepository;

    public Incident createIncident(Incident incident) {
        String incidentId = "RMG" + new Random().nextInt(90000) + 10000 + LocalDateTime.now().getYear();
        incident.setIncidentId(incidentId);
        incident.setReportedDateTime(LocalDateTime.now());
        return incidentRepository.save(incident);    }

    public Incident getIncidentById(Long id) {
        return incidentRepository.findById(id).orElseThrow(() -> new RuntimeException("Incident not found"));
    }

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    public void deleteIncident(Long id) {
        incidentRepository.deleteById(id);
    }

}
