package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.Data;
import org.apache.camel.component.jpa.Consumed;

@Data
@Entity
@Table(name = "pcpedc")
@NamedQuery(name = "newOrders", query = "SELECT p FROM Pedido p WHERE p.ksrProcessedAt IS NULL AND p.posicao = 'L' AND p.fretedespacho = 'C'")
public class Pedido implements Serializable {

    @Id
    private Long numped;
    private OffsetDateTime data;
    private Double vlatend;
    private String posicao;
    private Double totpeso;
    private Double totvolume;
    private String fretedespacho;
    private Integer numseqmontagem;
    private Integer numordemcarga;
    private Long numseqentrega;
    private Long ksrId;
    private String ksrEtag;
    private OffsetDateTime ksrProcessedAt;

    @ManyToOne
    @JoinColumn(name = "codcli")
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "codfilial", referencedColumnName = "codigo")
    private Filial filial;    

    @Consumed
    public void process() { ksrProcessedAt = OffsetDateTime.now(); }
}
