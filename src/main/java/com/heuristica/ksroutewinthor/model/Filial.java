package com.heuristica.ksroutewinthor.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pcfilial")
public class Filial implements Serializable {
    
    @Id
    private Long codigo;
    private String razaosocial;
    private Long ksrId;
    private String ksrEtag;    
    
}
