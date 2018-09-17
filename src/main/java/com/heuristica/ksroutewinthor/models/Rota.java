package com.heuristica.ksroutewinthor.models;

import javax.persistence.Column;
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
    private Long ksrId; 
    
    @Column(insertable = false, updatable = false)
    private String oraRowscn;  

}
