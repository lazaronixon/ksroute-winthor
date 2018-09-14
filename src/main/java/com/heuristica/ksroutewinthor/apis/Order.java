package com.heuristica.ksroutewinthor.apis;

import java.util.Date;
import lombok.Data;

@Data
public class Order {
    private Long id;
    private Double amount;
    private Double weight;
    private Double volume;
    private String erpId;
    private Date issuedAt;    
    
    private Long branchId;
    private Long customerId;  
}
