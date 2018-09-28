package com.heuristica.ksroutewinthor.models;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pcconsum")
public class Consum implements Serializable {
    
    @Id
    private String cgc;
    private Long proxnumcar;
    
}
