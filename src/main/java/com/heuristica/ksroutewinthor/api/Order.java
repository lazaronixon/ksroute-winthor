package com.heuristica.ksroutewinthor.api;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Order {
    
    public static enum Status { available, blocked, mounted, billed }    
    
    private Long id;
    private Branch branch;
    private Customer customer;
    private Double amount;
    private Double weight;
    private Double volume;
    private Status status;
    private String erpId;
    private LocalDate issuedAt;    
    
}
