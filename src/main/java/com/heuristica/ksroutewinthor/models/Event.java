package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;


@Data
@Entity
@Table(name = "ksr_event")
@EntityListeners( RecordableListener.class )
@NamedQuery(name = "newEvents", query = "SELECT p FROM Event p ORDER BY p.priority, p.id")
public class Event implements Recordable, Serializable {
    
    @Id
    private Long id; 
    private String eventableId;
    private String eventableType;   
    private String persistAction;
    private Integer priority;
    private LocalDateTime createdAt;
     
    @Transient
    private Record record;
    
    @Override
    public String getRecordableId() { return eventableId; }
    
    @Override
    public String getRecordableType() { return eventableType; }
}
