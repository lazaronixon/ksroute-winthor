package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.models.Cliente;
import com.heuristica.ksroutewinthor.models.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClienteService {

    @Autowired
    private ClienteRepository clientes;

    public Cliente findCliente(Long id) {
        return clientes.findById(id).get();
    }

    public Cliente saveCliente(Cliente cliente) {
        return clientes.save(cliente);
    }

}