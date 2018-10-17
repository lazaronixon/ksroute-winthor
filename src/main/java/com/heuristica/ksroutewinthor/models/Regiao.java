package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "pcregiao")
public class Regiao implements Serializable {

    @Id
    private Long numregiao;
    private String regiao;
    private String uf; 
    
    @Transient
    private Record record;    
    
}
