package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "pcregiao")
public class Regiao implements Recordable, Serializable {

    @Id
    private Long numregiao;
    private String regiao;
    private String uf; 
    
    // <editor-fold defaultstate="collapsed" desc="Recordable">   
    @Transient
    private Record record;
    
    @Override
    public String getRecordableId() { return String.valueOf(numregiao); }
    // </editor-fold>   
    
}
