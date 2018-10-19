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
@Table(name = "pcclient")
public class Cliente implements Recordable, Serializable {

    @Id
    private Long codcli;
    private String cliente;
    private String fantasia;
    private String estent;
    private String municent;
    private String bairroent;
    private String enderent;
    private String cepent;
    private String latitude;
    private String longitude;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codpraca")
    private Praca praca;
    
    // <editor-fold defaultstate="collapsed" desc="Recordable">   
    @Transient
    private Record record;
    
    @Override
    public String getRecordableId() { return String.valueOf(codcli); }
    
    @Override
    public String getRecordableType() { return Cliente.class.getSimpleName(); }

    @PostLoad
    public void fetchRecord() {
        RecordService recordService = ApplicationContextHolder.getBean(RecordService.class);
        this.record = recordService.findByRecordable(this).orElse(null);
    }      
    // </editor-fold>     

}
