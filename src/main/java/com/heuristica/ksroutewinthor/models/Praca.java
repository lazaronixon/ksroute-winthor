package com.heuristica.ksroutewinthor.models;

import com.heuristica.ksroutewinthor.ApplicationContextHolder;
import com.heuristica.ksroutewinthor.services.RecordService;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "pcpraca")
public class Praca implements Recordable, Serializable {

    @Id
    private Long codpraca;
    private String praca; 
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numregiao")
    private Regiao regiao;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rota", referencedColumnName = "codrota")
    private Rota rota;
    
    // <editor-fold defaultstate="collapsed" desc="Recordable">   
    @Transient
    private Record record;
    
    @Override
    public String getRecordableId() { return String.valueOf(codpraca); }
    
    @Override
    public String getRecordableType() { return Praca.class.getSimpleName(); } 
    
    @PostLoad
    public void fetchRecord() {
        RecordService recordService = ApplicationContextHolder.getBean(RecordService.class);
        this.record = recordService.findByRecordable(this).orElse(null);
    }      
    // </editor-fold> 
    
}
