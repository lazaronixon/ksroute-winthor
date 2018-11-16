package com.heuristica.ksroutewinthor.apis;

import com.heuristica.ksroutewinthor.models.Cliente;
import lombok.Data;

@Data
public class Customer implements RecordableApi {
    
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
    
    private Long subregionId;
    
    @Override
    public String getRecordableType() {
        return Cliente.class.getSimpleName();
    }    
    
}
