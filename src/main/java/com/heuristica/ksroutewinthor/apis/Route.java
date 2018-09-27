package com.heuristica.ksroutewinthor.apis;

import java.util.Date;
import lombok.Data;


@Data
public class Route {
    
    private Long id;
    private Date mountedAt;
    
    private Vehicle vehicle;
    private Solution solution;
    
}
