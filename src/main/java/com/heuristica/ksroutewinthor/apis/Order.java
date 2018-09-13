package com.heuristica.ksroutewinthor.apis;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Order {
    private Long id;
    private Double amount;
    private Double weight;
    private Double volume;
    private String erpId;
    private LocalDate issuedAt;    
    
    private Long branchId;
    private Long customerId;  
}
