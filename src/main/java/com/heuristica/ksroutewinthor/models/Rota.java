package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "pcrotaexp")
@EntityListeners( RecordableListener.class )
public class Rota implements Recordable, Serializable {    
    
    @Id
    private Long codrota;
    private String descricao;
    private String situacao;
      
    @Transient
    private Record record;
    
    @Override
    public String getRecordableId() { return String.valueOf(codrota); }
    
    @Override
    public String getRecordableType() { return Rota.class.getSimpleName(); }     

}
