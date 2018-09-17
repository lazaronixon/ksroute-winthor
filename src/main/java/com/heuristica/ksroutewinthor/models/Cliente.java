package com.heuristica.ksroutewinthor.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pcclient")
public class Cliente {

    @Id
    private Long codcli;
    private String cliente;
    private String fantasia;
    private String estent;
    private String municent;
    private String bairroent;
    private String enderent;
    private String cepent;
    private String latitude;
    private String longitude;
    private Long ksrId;
    
    @ManyToOne
    @JoinColumn(name = "CODPRACA")
    private Praca praca;
    
    @Column(insertable = false, updatable = false)
    private String oraRowscn;    

}
