package com.heuristica.winthor;

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
@NamedQuery(name = "newOrders", query = "SELECT p FROM Pedido p WHERE p.ksrProcessed = false AND p.posicao = 'L' AND p.freteDespacho = 'C'")
public class Pedido implements Serializable {

    @Id
    private Long numPed;
    private LocalDate data;    
    private Double vlAtend;
    private String posicao;
    private Double totPeso;
    private Double totVolume;
    private String freteDespacho;
    private Integer numSeqMontagem;
    private Integer numOrdemCarga;
    private Long numSeqEntrega;    
    private Long ksrId;
    private String ksrEtag;
    private Boolean ksrProcessed;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codcli")
    private Cliente cliente;

}
