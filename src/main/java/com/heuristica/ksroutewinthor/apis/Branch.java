package com.heuristica.ksroutewinthor.apis;

import com.heuristica.ksroutewinthor.models.Filial;
import lombok.Data;

@Data
public class Branch implements RecordableApi {
    
    private Long id;
    private String description;
    private String erpId;    

    @Override
    public String getRecordableType() {
        return Filial.class.getSimpleName();
    }
    
}
