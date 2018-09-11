package com.heuristica.ksroutewinthor.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pcrotaexp")
public class Rota {    
    
    @Id
    private Long codrota;
    private String descricao;    
    private String situacao;
    private Long ksrId; 

}
