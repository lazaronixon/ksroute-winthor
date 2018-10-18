package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
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
    // </editor-fold> 
}
