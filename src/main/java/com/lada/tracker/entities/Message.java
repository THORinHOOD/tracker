package com.lada.tracker.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Builder
@Entity
@Table(name = "message", schema = "tracker")
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Timestamp creation;
    String message;
    @Column(name = "employee_id")
    Long employeeId;

}
