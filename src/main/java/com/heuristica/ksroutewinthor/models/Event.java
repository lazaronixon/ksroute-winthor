package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "ksr_event")
@NamedQuery(name = "newEvents", query = "SELECT p FROM Event p ORDER BY p.priority, p.id")
public class Event implements Serializable {
    
    @Id
    private Long id; 
    private String eventableId;
    private String eventableType;   
    private String persistAction;
    private Integer priority;
    private LocalDateTime createdAt;
    
}
