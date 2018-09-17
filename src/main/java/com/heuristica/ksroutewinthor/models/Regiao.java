package com.heuristica.ksroutewinthor.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pcregiao")
public class Regiao {

    @Id
    private Long numregiao;
    private String regiao;
    private String uf;
    private Long ksrId;

    @Column(insertable = false, updatable = false)
    private String oraRowscn;    
    
}
