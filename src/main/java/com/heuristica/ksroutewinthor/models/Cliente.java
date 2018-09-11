package com.heuristica.ksroutewinthor.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    private Float latitude;
    private Float longitude;
    private String bloqueio;
    private Long ksrId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CODPRACA")
    private Praca praca;

}
