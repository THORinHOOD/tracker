package com.lada.tracker.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@Entity
@Table(name = "request_from_ward_status", schema = "tracker")
@NoArgsConstructor
@AllArgsConstructor
public class RequestFromWardStatus {

    @Id
    private int id;
    @Column(name = "rus_name")
    private String rusName;
    private String description;

}
