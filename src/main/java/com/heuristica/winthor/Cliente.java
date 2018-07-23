package com.heuristica.winthor;

import java.io.Serializable;
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
public class Cliente implements Serializable {

    @Id
    private Long codCli;
    private String cliente;
    private String fantasia;
    private String ufEnt;
    private String municEnt;
    private String bairroEnt;
    private String enderEnt;
    private String cepEnt;
    private Float latitude;
    private Float longitude;
    private String bloqueio;
    private Long ksrId;
    private String ksrEtag;    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CODPRACA")
    private Praca praca;

}
