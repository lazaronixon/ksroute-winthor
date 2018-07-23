package com.heuristica.ksroutewinthor.apis;

import java.time.LocalDate;

public class Order {
    
    private Branch branch;
    private Customer customer;
    private Double amount;
    private Double weight;
    private Double volume;
    private Enum status;
    private String erpId;
    private LocalDate issuedAt;    
    
}
