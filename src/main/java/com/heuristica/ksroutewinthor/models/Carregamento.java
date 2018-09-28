package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pccarreg")
public class Carregamento implements Serializable {
    
    @Id
    private Long numcar;
    private LocalDate dtsaida;
    
    private Long codmotorista;        
    private Long codveiculo;    
    private Double totpeso;
    private Double totvolume;
    private Double vltotal;
    private String destino;
    private String obsdestino;
    private Long numnotas;
    private Long nument;
    private Long numcid;
    private LocalDate datamon;
        
    private Long codfuncmon;    
    private Integer numviasmapa = 0;
    private Long qtitens;
    private Integer kminicial = 0;
    private Integer kmfinal = 0; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codrotaprinc", referencedColumnName = "codrota")
    private Rota rotaPrinc;
    
    private Integer numdiarias = 0;
    private Integer codfuncajud = 0;
    private Double vlfrete = 0.;
    private Integer minutomon;
    private Integer horamon;
    private Integer segundomon;
    private String cargasecundaria = "N";
    private String lancardespdescfinautomatic = "N";
    private String numcarmanifconcluidofv = "N";        
    
    @OneToMany(mappedBy = "carregamento", cascade = CascadeType.ALL, orphanRemoval = true)    
    private List<Pedido> pedidoList = new ArrayList();
    
    public void addToPedidoList(Pedido pedido) {
        pedidoList.add(pedido);
        pedido.setCarregamento(this);
        pedido.getPedidoItemList().forEach(pi -> {
            pi.setCarregamento(pedido.getCarregamento());
            pi.setPosicao(pedido.getPosicao());
        });
    }
    
}
