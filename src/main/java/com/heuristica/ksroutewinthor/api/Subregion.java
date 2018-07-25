package com.heuristica.ksroutewinthor.api;

import lombok.Data;


@Data
public class Subregion {
    
    private Long id;
    private String description;
    private String erpId;
    private Boolean active;
    
    private Long regionId;    
    private Region region;
    
    private Long lineId;    
    private Line line;
    
}
