package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Order;
import com.heuristica.ksroutewinthor.models.Pedido;
import com.heuristica.ksroutewinthor.models.PedidoRepository;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PedidoService {
    
    @PersistenceContext private EntityManager entityManager; 

    @Autowired private PedidoRepository pedidos;
    
    public Pedido findPedido(Long id) {
        return pedidos.findById(id).get();
    }

    public Optional<Pedido> findPedidoById(Long id) {
        return pedidos.findById(id);
    }    
    
    public Pedido fetchPedido(Long id) {
        entityManager.clear();
        return entityManager.find(Pedido.class, id);
    }
    
    public Pedido saveApiResponse(Order order) {
        Pedido pedido = findPedido(Long.parseLong(order.getErpId()));
        pedido.setKsrId(order.getId());
        return pedidos.save(pedido);
    }

}
