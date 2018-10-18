package com.heuristica.ksroutewinthor.apis;

import lombok.Data;

@Data
public class Branch implements RecordableApi {
    
    private Long id;
    private String description;
    private String erpId;    

    @Override
    public String getEntityName() {
        return "Filial";
    }
    
}
