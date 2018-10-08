package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "pcpedc")
@NamedQuery(name = "newOrders", query = "SELECT p FROM Pedido p WHERE p.ksrProcessedAt IS NULL AND p.posicao = 'L' ORDER BY p.cliente.praca.regiao.numregiao, p.cliente.praca.codpraca, p.numped")
public class Pedido implements Serializable {

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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codfilial", referencedColumnName = "codigo")
    private Filial filial;       
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numcar")
    private Carregamento carregamento;        

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codcli")
    private Cliente cliente;    
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItem> pedidoItemList = new ArrayList();      

}
