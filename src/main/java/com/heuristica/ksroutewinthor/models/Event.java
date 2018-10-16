package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;


@Data
@Entity
@Table(name = "ksr_event")
@NamedQuery(name = "newEvents", query = "SELECT p FROM Event p ORDER BY p.createdAt")
public class Event implements Serializable {
    
    @Id
    private Long id;
    private Long ksrId;    
    private String eventableId;
    private String eventableType;   
    private String persistAction;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
}
