package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pcpedc")
@NamedQuery(name = "newOrders", query = "SELECT p FROM Pedido p WHERE p.ksrProcessed = false AND p.posicao = 'L' AND p.fretedespacho = 'C'")
public class Pedido implements Serializable {

    @Id
    private Long numped;
    private LocalDate data;    
    private Double vlatend;
    private String posicao;
    private Double totpeso;
    private Double totvolume;
    private String fretedespacho;
    private Integer numseqMontagem;
    private Integer numordemCarga;
    private Long numseqEntrega;    
    private Long ksrId;
    private String ksrEtag;
    private Boolean ksrProcessed;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codcli")
    private Cliente cliente;

}
