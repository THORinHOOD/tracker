package com.lada.tracker.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@Entity
@Table(name = "request_type", schema = "tracker")
@NoArgsConstructor
@AllArgsConstructor
public class RequestType {

    @Id
    private Long id;
    private String name;

}
