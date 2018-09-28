package com.heuristica.ksroutewinthor.apis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;


@Data
public class Route {
    
    private Long id;
    private Date mountedAt;
    
    private Vehicle vehicle;
    private Solution solution;
    
    private List<Activity> activities = new ArrayList();
    
}
