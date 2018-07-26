package com.heuristica.ksroutewinthor.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pcregiao")
public class Regiao implements Serializable {

    @Id
    private Long numregiao;
    private String regiao;
    private String uf;
    private String status;
    private Long ksrId;
    private String ksrEtag;      

}
