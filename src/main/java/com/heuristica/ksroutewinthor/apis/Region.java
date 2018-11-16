package com.heuristica.ksroutewinthor.apis;

import com.heuristica.ksroutewinthor.models.Regiao;
import lombok.Data;

@Data
public class Region implements RecordableApi {

    private Long id;
    private String description;
    private String state;
    private String erpId;
    private Boolean active;
    
    @Override
    public String getRecordableType() {
        return Regiao.class.getSimpleName();
    }       

}
