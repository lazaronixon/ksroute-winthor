package com.heuristica.ksroutewinthor.repositories;

import com.heuristica.ksroutewinthor.models.Cliente;
import org.springframework.data.repository.CrudRepository;

public interface ClienteRepository extends CrudRepository<Cliente, Long>  {
    
}
