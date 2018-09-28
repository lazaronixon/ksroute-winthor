package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Cacheable(false)
@Table(name = "pcpraca")
public class Praca implements Serializable {

    @Id
    private Long codpraca;
    private String praca;
    private Long ksrId;   
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numregiao")
    private Regiao regiao;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rota", referencedColumnName = "codrota")
    private Rota rota; 

    @Column(insertable = false, updatable = false)
    private String oraRowscn;

}
