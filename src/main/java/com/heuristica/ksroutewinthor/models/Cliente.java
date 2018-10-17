package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "pcclient")
public class Cliente implements Serializable {

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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codpraca")
    private Praca praca;
    
    @Transient
    private Record record;    

}
