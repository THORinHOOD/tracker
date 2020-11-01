package com.lada.tracker.entities;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "request_status", schema = "tracker")
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
public class RequestStatus {

    @Id
    private int id;
    private String name;
    private String description;
    @Type(type = "list-array")
    @Column(name = "request_type_ids", columnDefinition = "int[]")
    private List<Integer> requestTypeIds;
    private Boolean start;
}
