package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "pcpedc")
@EntityListeners( RecordableListener.class )
public class Pedido implements Recordable, Serializable {

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
    
    private LocalDate data;  
    
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
    
    @Transient
    private Record record;
    
    @Override
    public String getRecordableId() { return String.valueOf(numped); }
    
    @Override
    public String getRecordableType() { return Pedido.class.getSimpleName(); }
    
}
