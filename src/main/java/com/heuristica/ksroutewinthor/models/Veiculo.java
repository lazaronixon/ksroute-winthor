package com.heuristica.ksroutewinthor.models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import lombok.Data;
import org.apache.camel.component.jpa.Consumed;

@Data
@Entity
@Table(name = "pcveicul")
@NamedQuery(name = "newVehicles", query = "SELECT p FROM Veiculo p WHERE p.ksrProcessedAt IS NULL AND p.situacao <> 'I' ORDER BY p.codveiculo")
public class Veiculo {
    
    @Id
    private Long codveiculo;
    private String descricao;
    private String tipoveiculo;
    private String situacao;
    private Long ksrId;
    
    @Transient
    private String startAddressId;
    
    @Transient
    private String vehicleTypeId;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date ksrProcessedAt;
    
}