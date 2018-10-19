package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Customer;
import com.heuristica.ksroutewinthor.models.Cliente;
import com.heuristica.ksroutewinthor.repositories.ClienteRepository;
import com.heuristica.ksroutewinthor.models.Event;
import java.util.Map;
import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClienteService {

    @Autowired private ClienteRepository clientes;
    @Autowired private RecordService recordService;
    
    public Cliente findByEvent(Event event) {       
        return clientes.findById(Long.parseLong(event.getEventableId())).orElse(null);
    }
    
    public Cliente saveResponse(@Body Customer customer, @Headers Map headers) {
        recordService.saveResponse(customer, headers);        
        return clientes.findById(Long.parseLong(customer.getErpId())).orElse(null);
    }
}
