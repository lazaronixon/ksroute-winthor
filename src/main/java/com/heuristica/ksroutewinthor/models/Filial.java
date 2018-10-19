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
@Table(name = "pcfilial")
@EntityListeners( RecordableListener.class )
public class Filial implements Recordable, Serializable {
    
    @Id
    private String codigo;
    private String razaosocial;    

    @Transient
    private Record record;
    
    @Override
    public String getRecordableId() { return codigo; }
    
    @Override
    public String getRecordableType() { return Filial.class.getSimpleName(); }
}
