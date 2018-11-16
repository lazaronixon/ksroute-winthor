package com.heuristica.ksroutewinthor.apis;

import com.heuristica.ksroutewinthor.models.Veiculo;
import lombok.Data;
    
@Data
public class Vehicle implements RecordableApi {
    
    public static enum Status { available, traveling, blocked, returning, inactive }
    
    private Long id;
    private String description;
    private String erpId;  
    private Status status;

    private Long startAddressId;    
    private Long vehicleTypeId;
    
    @Override
    public String getRecordableType() {
        return Veiculo.class.getSimpleName();
    }    
    
}