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
@Table(name = "request_transaction", schema = "tracker")
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
public class RequestTransaction {
    @Id
    private int id;
    private int from;
    private int to;
    @Type(type = "list-array")
    @Column(name = "roles", columnDefinition = "int[]")
    private List<Integer> roles;
    @Type(type = "list-array")
    @Column(name = "request_type_ids", columnDefinition = "int[]")
    private List<Integer> requestTypeIds;
}
