package com.heuristica.ksroutewinthor.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pcregiao")
public class Regiao {

    @Id
    private Long numregiao;
    private String regiao;
    private String uf;
    private String status;
    private Long ksrId;

}
