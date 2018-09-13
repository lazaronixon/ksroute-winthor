package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Order;
import com.heuristica.ksroutewinthor.models.Pedido;
import com.heuristica.ksroutewinthor.models.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PedidoService {

    @Autowired
    private PedidoRepository pedidos;

    public Pedido findPedido(Long id) {
        return pedidos.findById(id).get();
    }

    public Pedido savePedido(Order order) {
        Pedido pedido = findPedido(Long.parseLong(order.getErpId()));
        pedido.setKsrId(order.getId());
        return pedidos.save(pedido);
    }

}
