package com.heuristica.ksroutewinthor.models;

import com.heuristica.ksroutewinthor.ApplicationContextHolder;
import com.heuristica.ksroutewinthor.services.RecordService;
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
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "pcpedc")
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
    
    @Temporal(TemporalType.DATE)
    private Date data;  
    
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
    
    // <editor-fold defaultstate="collapsed" desc="Recordable">   
    @Transient
    private Record record;
    
    @Override
    public String getRecordableId() { return String.valueOf(numped); }
    
    @Override
    public String getRecordableType() { return Pedido.class.getSimpleName(); }  

    @PostLoad
    private void fetchRecord() {
        RecordService recordService = ApplicationContextHolder.getBean(RecordService.class);
        this.record = recordService.findByRecordable(this).orElse(null);
    }    
    // </editor-fold> 
    
}
