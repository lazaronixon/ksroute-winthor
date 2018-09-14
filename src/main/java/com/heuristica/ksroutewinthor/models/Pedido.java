package com.heuristica.ksroutewinthor.models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.apache.camel.component.jpa.Consumed;

@Data
@Entity
@Table(name = "pcpedc")
@NamedQuery(name = "newOrders", query = "SELECT p FROM Pedido p WHERE p.ksrProcessedAt IS NULL AND p.posicao = 'L' AND p.fretedespacho = 'C'")
public class Pedido {

    @Id
    private Long numped;
    private Double vlatend;
    private String posicao;
    private Double totpeso;
    private Double totvolume;
    private String fretedespacho;
    private Integer numseqmontagem;
    private Integer numordemcarga;
    private Long numseqentrega;
    private Long ksrId;
    
    @Temporal(TemporalType.DATE)
    private Date data;    
    @Temporal(TemporalType.TIMESTAMP)
    private Date ksrProcessedAt;

    @ManyToOne
    @JoinColumn(name = "codcli")
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "codfilial", referencedColumnName = "codigo")
    private Filial filial;    

    @Consumed
    public void setProcessed() { ksrProcessedAt = new Date(); }
}
