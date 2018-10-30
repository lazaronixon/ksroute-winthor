package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Order;
import com.heuristica.ksroutewinthor.models.Event;
import com.heuristica.ksroutewinthor.models.Pedido;
import com.heuristica.ksroutewinthor.repositories.PedidoRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PedidoService {

    @Autowired private PedidoRepository pedidos;
    @Autowired private RecordService recordService; 
    
    public Pedido findByEvent(Event event) {       
        return pedidos.findById(Long.parseLong(event.getEventableId())).orElse(null);
    }
    
    public Optional<Pedido> findById(Long id) {
        return pedidos.findById(id);
    } 
    
    public Pedido saveResponse(@Body Order order, @Headers Map headers) {
        recordService.saveResponse(order, headers);        
        return pedidos.findById(Long.parseLong(order.getErpId())).orElse(null);
    }
}
