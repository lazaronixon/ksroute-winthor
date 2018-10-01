package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Customer;
import com.heuristica.ksroutewinthor.models.Cliente;
import com.heuristica.ksroutewinthor.models.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClienteService {

    @Autowired private ClienteRepository clientes;
    
    public Cliente saveCustomer(Customer customer) {
        Cliente cliente = clientes.findById(Long.parseLong(customer.getErpId())).get();
        cliente.setLatitude(customer.getLatitude() != null ? String.valueOf(customer.getLatitude()) : null);
        cliente.setLongitude(customer.getLongitude()!= null ? String.valueOf(customer.getLongitude()) : null);
        cliente.setKsrId(customer.getId());
        return clientes.save(cliente);
    }

}
