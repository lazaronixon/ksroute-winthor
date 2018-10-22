package com.heuristica.ksroutewinthor.repositories;

import com.heuristica.ksroutewinthor.models.Pedido;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PedidoRepository extends CrudRepository<Pedido, Long>  {
    
    @Query("SELECT v FROM Pedido v WHERE v.posicao = 'L' AND v.data >= (TRUNC(CURRENT_DATE) -90) ORDER BY v.data DESC, v.id")
    List<Pedido> findAllDisponible();    
    
}
