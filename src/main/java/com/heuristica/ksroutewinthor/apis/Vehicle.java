package com.heuristica.ksroutewinthor.apis;

import lombok.Data;
    
@Data
public class Vehicle {
    
    private Long id;
    private String description;
    private String erpId;  

    private Long startAddressId;    
    private Long vehicleTypeId;
    
}