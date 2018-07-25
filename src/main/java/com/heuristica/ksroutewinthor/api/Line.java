package com.heuristica.ksroutewinthor.api;

import lombok.Data;

@Data
public class Line {
    
    private Long id;
    private String description;
    private String erpId;
    private Boolean active;
    
}
