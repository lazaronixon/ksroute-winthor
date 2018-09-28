package com.heuristica.ksroutewinthor.apis;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Activity {
    
    private Long id;
    private Integer position;
    private List<Order> orders = new ArrayList();
    
}
