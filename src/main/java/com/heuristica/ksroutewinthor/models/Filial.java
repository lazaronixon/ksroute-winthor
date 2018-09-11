package com.heuristica.ksroutewinthor.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pcfilial")
public class Filial {
    
    @Id
    private String codigo;
    private String razaosocial;
    private Long ksrId;
    
}
