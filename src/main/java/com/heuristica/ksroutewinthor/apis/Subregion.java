package com.heuristica.ksroutewinthor.apis;

import com.heuristica.ksroutewinthor.models.Praca;
import lombok.Data;


@Data
public class Subregion implements RecordableApi {
    
    private Long id;
    private String description;
    private String erpId;
    private Boolean active;
    
    private Long regionId;    
    private Long lineId;
    
    @Override
    public String getRecordableType() {
        return Praca.class.getSimpleName();
    }

}
