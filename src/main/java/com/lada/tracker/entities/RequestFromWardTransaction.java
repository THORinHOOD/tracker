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
@Table(name = "request_from_ward_transaction", schema = "tracker")
@NoArgsConstructor
@AllArgsConstructor
public class RequestFromWardTransaction {
    @Id
    private int id;
    private int from;
    private int to;
    private int role;
    private String condition;
}
