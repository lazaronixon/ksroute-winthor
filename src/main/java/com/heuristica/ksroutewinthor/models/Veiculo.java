package com.heuristica.ksroutewinthor.models;

import com.heuristica.ksroutewinthor.ApplicationContextHolder;
import com.heuristica.ksroutewinthor.services.VeiculoService;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "pcveicul")
@EntityListeners( RecordableListener.class )
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
      
    @Transient
    private Record record;
    
    @Override
    public String getRecordableId() { return String.valueOf(codveiculo); }
    
    @Override
    public String getRecordableType() { return Veiculo.class.getSimpleName(); }
    
    @PostLoad
    private void postLoadCallback() {
        VeiculoService veiculoService = ApplicationContextHolder.getBean(VeiculoService.class);
        veiculoService.setFromEnviromentValues(this);
    } 
    
}
