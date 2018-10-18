package com.heuristica.ksroutewinthor.repositories;

import com.heuristica.ksroutewinthor.models.Carregamento;
import org.springframework.data.repository.CrudRepository;
import java.lang.Long;

public interface CarregamentoRepository extends CrudRepository<Carregamento, Long>  {
    
}
