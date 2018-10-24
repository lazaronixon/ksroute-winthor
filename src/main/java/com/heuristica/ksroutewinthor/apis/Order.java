package com.heuristica.ksroutewinthor.apis;

import com.heuristica.ksroutewinthor.models.Pedido;
import java.time.LocalDate;
import lombok.Data;

@Data
public class Order implements RecordableApi  {
    private Long id;
    private Double amount;
    private Double weight;
    private Double volume;
    private String erpId;
    private LocalDate issuedAt;    
    
    private Long branchId;
    private Long customerId;
    
    @Override
    public String getRecordableType() {
        return Pedido.class.getSimpleName();
    } 
}
