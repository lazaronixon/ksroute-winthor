package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pcpedi")
@IdClass(PedidoItemPK.class)
public class PedidoItem implements Serializable {
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numped")
    private Pedido pedido;
    
    @Id
    private Long codprod;

    @Id
    private Integer numseq;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numcar")
    private Carregamento carregamento;
    
    private String posicao;    
            
}
