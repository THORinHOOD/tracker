package com.lada.tracker.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(schema = "auth", name = "roles")
public class RequestRole {
    @Id
    String name;
    @Column(name = "rus_name")
    String rusName;

}
