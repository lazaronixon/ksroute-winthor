package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import lombok.Data;

@Data
public class PedidoItemPK implements Serializable {    
    private Long pedido;
    private Long codprod;
    private Integer numseq;
}
