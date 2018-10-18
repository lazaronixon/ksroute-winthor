package com.heuristica.ksroutewinthor.apis;

import lombok.Data;


@Data
public class Subregion implements RecordableApi {
    
    private Long id;
    private String description;
    private String erpId;
    
    private Long regionId;    
    private Long lineId;
    
    @Override
    public String getRecordableType() {
        return Subregion.class.getSimpleName();
    }

}
