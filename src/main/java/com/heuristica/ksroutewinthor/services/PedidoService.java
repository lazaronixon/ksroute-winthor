package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Order;
import com.heuristica.ksroutewinthor.models.Pedido;
import com.heuristica.ksroutewinthor.models.PedidoRepository;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidos;
    
    public Pedido findPedido(Long id) {
        return pedidos.findById(id).get();
    }
    
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public Pedido findPedidoWithoutTransaction(Long id) {
        return pedidos.findById(id).get();
    }    
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Pedido savePedido(Order order) {
        Pedido pedido = findPedido(Long.parseLong(order.getErpId()));
        pedido.setKsrId(order.getId());
        return pedidos.save(pedido);
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Pedido processPedido(Pedido pedido) {
        pedido.setKsrProcessedAt(new Date());
        return pedidos.save(pedido);
    }  

}
