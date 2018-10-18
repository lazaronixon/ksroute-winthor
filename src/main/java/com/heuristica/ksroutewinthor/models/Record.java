package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(name = "ksr_record_seq", allocationSize = 1)
@Table(name = "ksr_record")
public class Record implements Serializable {
    
    @Id
    @GeneratedValue(generator = "ksr_record_seq")
    private Long id;    
    private String recordableId;
    private String recordableType;
    private Long remoteId;
    private String requestId;
    private String etag; 
    
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;     
    
}
