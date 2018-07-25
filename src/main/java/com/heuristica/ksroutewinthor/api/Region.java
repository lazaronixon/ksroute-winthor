package com.heuristica.ksroutewinthor.api;

import lombok.Data;

@Data
public class Region {

    private Long id;
    private String description;
    private String state;
    private String erpId;
    private Boolean active;

}
