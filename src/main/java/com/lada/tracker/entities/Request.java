package com.lada.tracker.entities;

import com.lada.tracker.converters.PgJsonbToMapConverter;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

@Data
@Builder
@Entity
@Table(name = "request", schema = "tracker")
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Request implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "registration_date")
    private Timestamp registrationDate;
    private String body;

    @Column(name = "last_transaction")
    private Timestamp lastTransaction;
    private Integer status;
    @Column(name = "request_type_id")
    private Integer requestTypeId;

    @Type(type = "com.lada.tracker.utils.LongArrayUserType")
    private Long[] comments;

    @Column(name = "additional_info")
    @Type(type = "jsonb")
    private Map<String, Object> additionalInfo;

}
