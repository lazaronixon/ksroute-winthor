package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Order;
import com.heuristica.ksroutewinthor.models.Event;
import com.heuristica.ksroutewinthor.models.Pedido;
import com.heuristica.ksroutewinthor.repositories.PedidoRepository;
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
        return findByIdAndFetchRecord(Long.parseLong(event.getEventableId()));
    }
    
    public Optional<Pedido> findPedidoById(Long id) {
        return pedidos.findById(id);
    }    
    
    public Pedido saveResponse(@Body Order order, @Headers Map headers) {
        recordService.saveResponse(order, headers);        
        return findByIdAndFetchRecord(Long.parseLong(order.getErpId()));
    }
    
    private Pedido findByIdAndFetchRecord(Long id) {
        Optional<Pedido> pedido = pedidos.findById(id);
        pedido.ifPresent(p -> recordService.fetchRecord(p));
        return pedido.orElse(null); 
    }
}
