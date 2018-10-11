package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Order;
import com.heuristica.ksroutewinthor.models.Pedido;
import com.heuristica.ksroutewinthor.models.PedidoRepository;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PedidoService {

    @Autowired private PedidoRepository pedidos;
    
    public Pedido findPedido(Long id) {
        return pedidos.findById(id).get();
    }    
    
    public Pedido saveApiResponse(Order order) {
        Pedido pedido = findPedido(Long.parseLong(order.getErpId()));
        pedido.setKsrId(order.getId());
        return pedidos.save(pedido);
    }

}
