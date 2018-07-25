package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.api.Order;
import com.heuristica.ksroutewinthor.model.Pedido;
import static com.heuristica.ksroutewinthor.util.JavaUtil.defaultJsonConfig;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderService {

    @Autowired
    DozerBeanMapper dozerBeanMapper;

    public String processPedido(Pedido pedido) {
        Order order = dozerBeanMapper.map(pedido, Order.class);
        Jsonb jsonb = JsonbBuilder.create(defaultJsonConfig());
        if (pedido.getKsrId() == null) {
            return jsonb.toJson(order);
        } else {
            return jsonb.toJson(order);
        }
    }
}
