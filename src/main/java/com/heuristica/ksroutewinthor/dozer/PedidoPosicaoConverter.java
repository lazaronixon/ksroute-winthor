package com.heuristica.ksroutewinthor.dozer;

import com.heuristica.ksroutewinthor.api.Order;
import org.dozer.DozerConverter;

public class PedidoPosicaoConverter extends DozerConverter<String, Order.Status> {

    public PedidoPosicaoConverter() {
        super(String.class, Order.Status.class);
    }

    @Override
    public Order.Status convertTo(String source, Order.Status destination) {        
        switch (source) {
            case "L": return Order.Status.available;
            case "F": return Order.Status.billed;
            case "B": return Order.Status.blocked;
            case "M": return Order.Status.mounted;
            default: return Order.Status.available;
        }
    }

    @Override
    public String convertFrom(Order.Status source, String destination) {        
        switch (source.name()) {
            case "available": return "L";
            case "billed": return "F";
            case "blocked": return "B";
            case "mounted": return "M";
            default: return "L";
        }
    }

}
