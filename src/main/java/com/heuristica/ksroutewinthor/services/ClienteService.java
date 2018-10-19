package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Customer;
import com.heuristica.ksroutewinthor.models.Cliente;
import com.heuristica.ksroutewinthor.repositories.ClienteRepository;
import com.heuristica.ksroutewinthor.models.Event;
import java.util.Map;
import java.util.Optional;
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
        return findByIdAndFetchRecord(Long.parseLong(event.getEventableId()));
    }
    
    public Cliente saveResponse(@Body Customer customer, @Headers Map headers) {
        recordService.saveResponse(customer, headers);        
        return findByIdAndFetchRecord(Long.parseLong(customer.getErpId()));
    }
    
    private Cliente findByIdAndFetchRecord(Long id) {
        Optional<Cliente> cliente = clientes.findById(id);
        cliente.ifPresent(c -> recordService.fetchRecord(c));
        return cliente.orElse(null); 
    }

}
