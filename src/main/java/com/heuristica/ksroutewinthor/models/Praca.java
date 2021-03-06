package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "pcpraca")
@EntityListeners( RecordableListener.class )
public class Praca implements Recordable, Serializable {

    @Id
    private Long codpraca;
    private String praca;
    private String situacao;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numregiao")
    private Regiao regiao;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rota", referencedColumnName = "codrota")
    private Rota rota;
     
    @Transient
    private Record record;
    
    @Override
    public String getRecordableId() { return String.valueOf(codpraca); }
    
    @Override
    public String getRecordableType() { return Praca.class.getSimpleName(); }
    
}
