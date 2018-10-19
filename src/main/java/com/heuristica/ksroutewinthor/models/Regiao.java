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
@Table(name = "pcregiao")
@EntityListeners( RecordableListener.class )
public class Regiao implements Recordable, Serializable {

    @Id
    private Long numregiao;
    private String regiao;
    private String uf; 
    
    @Transient
    private Record record;
    
    @Override
    public String getRecordableId() { return String.valueOf(numregiao); }
    
    @Override
    public String getRecordableType() { return Regiao.class.getSimpleName(); }       
    
}
