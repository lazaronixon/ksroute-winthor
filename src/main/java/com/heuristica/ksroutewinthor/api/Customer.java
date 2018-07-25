package com.heuristica.ksroutewinthor.api;

import lombok.Data;

@Data
public class Customer {
    
    private Long id;
    private String name;
    private String trade;
    private String state;
    private String city;
    private String neighborhood;
    private String address;
    private String zipcode;
    private Float latitude;
    private Float longitude;
    private String erpId;  
    private Boolean active;
    private Subregion subregion;
    
}
