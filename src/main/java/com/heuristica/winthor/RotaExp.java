package com.heuristica.winthor;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pcrotaexp")
public class RotaExp implements Serializable {
    
    @Id
    private Long codRota;
    private String descricao;    
    private String situacao;
    private Long ksrId;
    private String ksrEtag;     

}
