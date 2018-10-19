package com.heuristica.ksroutewinthor.models;

import com.heuristica.ksroutewinthor.ApplicationContextHolder;
import com.heuristica.ksroutewinthor.services.RecordService;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;


@Data
@Entity
@Table(name = "ksr_event")
@NamedQuery(name = "newEvents", query = "SELECT p FROM Event p ORDER BY p.priority, p.id")
public class Event implements Recordable, Serializable {
    
    @Id
    private Long id; 
    private String eventableId;
    private String eventableType;   
    private String persistAction;
    private Integer priority;
    private LocalDateTime createdAt;
    
    // <editor-fold defaultstate="collapsed" desc="Recordable">   
    @Transient
    private Record record;
    
    @Override
    public String getRecordableId() { return eventableId; }
    
    @Override
    public String getRecordableType() { return eventableType; }

    @PostLoad
    private void fetchRecord() {
        RecordService recordService = ApplicationContextHolder.getBean(RecordService.class);
        this.record = recordService.findByRecordable(this).orElse(null);
    }
    // </editor-fold> 
}
