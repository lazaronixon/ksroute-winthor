package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Customer;
import com.heuristica.ksroutewinthor.models.Cliente;
import com.heuristica.ksroutewinthor.models.ClienteRepository;
import com.heuristica.ksroutewinthor.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClienteService {

    @Autowired private ClienteRepository clientes;
    
    public Cliente saveApiResponse(Customer customer) {
        Cliente cliente = clientes.findById(Long.parseLong(customer.getErpId())).get();
        cliente.setKsrId(customer.getId());
        return clientes.save(cliente);
    }
    
    public Cliente getEventable(Event event) {
        return clientes.findById(Long.parseLong(event.getEventableId())).orElse(null);
    }

}
