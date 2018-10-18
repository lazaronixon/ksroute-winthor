package com.heuristica.ksroutewinthor.apis;

import lombok.Data;

@Data
public class Branch implements Recordable {
    
    private Long id;
    private String description;
    private String erpId;    

    @Override
    public String getTableName() {
        return "PCFILIAL";
    }
    
}
