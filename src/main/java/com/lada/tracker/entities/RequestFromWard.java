package com.lada.tracker.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Builder
@Entity
@Table(name = "request_from_ward", schema = "tracker")
@NoArgsConstructor
@AllArgsConstructor
public class RequestFromWard {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "registration_date")
    private Timestamp registrationDate;

    private String name;
    private String phone;
    private String email;
    private String body;

    @Column(name = "last_transaction")
    private Timestamp lastTransaction;
    private String trafic;
    private Integer status;

    @Column(name = "message_ids")
    @Type(type = "com.lada.tracker.utils.LongArrayUserType")
    private Long[] messageIds;

}
