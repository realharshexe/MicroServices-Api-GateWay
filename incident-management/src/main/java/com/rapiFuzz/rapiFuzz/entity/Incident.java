package com.rapiFuzz.rapiFuzz.entity;

import com.rapiFuzz.rapiFuzz.enums.Priority;
import com.rapiFuzz.rapiFuzz.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "incidents")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String incidentId;

    @Column(nullable = false)
    private String reporterName;

    @Column(nullable = false)
    private String details;

    @Column(nullable = false)
    private LocalDateTime reportedDateTime;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;
}
