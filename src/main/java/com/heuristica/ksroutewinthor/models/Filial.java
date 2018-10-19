package com.heuristica.ksroutewinthor.models;

import com.heuristica.ksroutewinthor.ApplicationContextHolder;
import com.heuristica.ksroutewinthor.services.RecordService;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "pcfilial")
public class Filial implements Recordable, Serializable {
    
    @Id
    private String codigo;
    private String razaosocial;    

    // <editor-fold defaultstate="collapsed" desc="Recordable">   
    @Transient
    private Record record;
    
    @Override
    public String getRecordableId() { return codigo; }
    
    @Override
    public String getRecordableType() { return Filial.class.getSimpleName(); } 
        
    @PostLoad
    public void fetchRecord() {
        RecordService recordService = ApplicationContextHolder.getBean(RecordService.class);
        this.record = recordService.findByRecordable(this).orElse(null);
    }   
    // </editor-fold> 
}
