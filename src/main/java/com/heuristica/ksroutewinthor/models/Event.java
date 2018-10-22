package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ksr_event")
@SequenceGenerator(name = "ksr_event_seq", allocationSize = 1)
@NamedQuery(name = "newEvents", query = "SELECT p FROM Event p ORDER BY p.priority, p.id")
public class Event implements Serializable {
    
    @Id
    @GeneratedValue(generator = "ksr_event_seq")
    private Long id; 
    private String eventableId;
    private String eventableType;   
    private String persistAction;
    private Integer priority;
     
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_record_id")
    private Record deletedRecord;
    
    @CreatedDate
    private LocalDateTime createdAt;        
}
