package com.heuristica.ksroutewinthor.apis;

import lombok.Data;
    
@Data
public class Vehicle implements RecordableApi {
    
    private Long id;
    private String description;
    private String erpId;  

    private Long startAddressId;    
    private Long vehicleTypeId;
    
    @Override
    public String getRecordableType() {
        return Subregion.class.getSimpleName();
    }    
    
}