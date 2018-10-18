package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "pcrotaexp")
public class Rota implements Recordable, Serializable {    
    
    @Id
    private Long codrota;
    private String descricao;
    
    // <editor-fold defaultstate="collapsed" desc="Recordable">   
    @Transient
    private Record record;
    
    @Override
    public String getRecordableId() { return String.valueOf(codrota); }
    // </editor-fold>          

}
