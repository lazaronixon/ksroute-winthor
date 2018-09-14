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

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW) 
    public Cliente findCliente(Long id) {
        return clientes.findById(id).get();
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)    
    public Cliente saveCustomer(Customer customer) {
        Cliente cliente = findCliente(Long.parseLong(customer.getErpId()));
        cliente.setLatitude(customer.getLatitude() != null ? String.valueOf(customer.getLatitude()) : null);
        cliente.setLongitude(customer.getLongitude()!= null ? String.valueOf(customer.getLongitude()) : null);
        cliente.setKsrId(customer.getId());
        return clientes.save(cliente);
    }

}
