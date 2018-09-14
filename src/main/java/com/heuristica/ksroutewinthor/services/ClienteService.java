package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Customer;
import com.heuristica.ksroutewinthor.models.Cliente;
import com.heuristica.ksroutewinthor.models.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ClienteService {

    @Autowired
    private ClienteRepository clientes;

    public Cliente findCliente(Long id) {
        return clientes.findById(id).get();
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)    
    public Cliente saveCustomer(Customer customer) {
        Cliente cliente = findCliente(Long.parseLong(customer.getErpId()));
        cliente.setLatitude(String.valueOf(customer.getLatitude()));
        cliente.setLongitude(String.valueOf(customer.getLongitude()));
        cliente.setKsrId(customer.getId());
        return clientes.save(cliente);
    }

}
