package com.heuristica.ksroutewinthor.apis;

import com.heuristica.ksroutewinthor.models.Rota;
import lombok.Data;

@Data
public class Line implements RecordableApi {
    
    private Long id;
    private String description;
    private String erpId;
    private Boolean active;
    
    @Override
    public String getRecordableType() {
        return Rota.class.getSimpleName();
    }       
    
}
