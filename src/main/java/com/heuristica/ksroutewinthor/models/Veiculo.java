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
@Table(name = "pcveicul")
public class Veiculo implements Recordable, Serializable {
    
    @Id
    private Long codveiculo;
    private String descricao;
    private String tipoveiculo;
    private String situacao;   
    
    @Transient
    private String startAddressId;
    
    @Transient
    private String vehicleTypeId;
    
    // <editor-fold defaultstate="collapsed" desc="Recordable">   
    @Transient
    private Record record;
    
    @Override
    public String getRecordableId() { return String.valueOf(codveiculo); }
    
    @Override
    public String getRecordableType() { return Veiculo.class.getSimpleName(); }
    
    @PostLoad
    public void fetchRecord() {
        RecordService recordService = ApplicationContextHolder.getBean(RecordService.class);
        this.record = recordService.findByRecordable(this).orElse(null);
    }      
    // </editor-fold>    
    
}
