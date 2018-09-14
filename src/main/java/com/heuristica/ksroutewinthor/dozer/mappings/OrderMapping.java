package com.heuristica.ksroutewinthor.dozer.mappings;

import com.heuristica.ksroutewinthor.apis.Order;
import com.heuristica.ksroutewinthor.models.Pedido;
import org.dozer.loader.api.BeanMappingBuilder;

public class OrderMapping extends BeanMappingBuilder {

    @Override
    protected void configure() {
        mapping(Pedido.class, Order.class)
                .fields("numped", "erpId")
                .fields("data", "issuedAt")
                .fields("vlatend", "amount")
                .fields("totpeso", "weight")
                .fields("totvolume", "volume")
                .fields("ksrId", "id")
                .fields("filial.ksrId", "branchId")
                .fields("cliente.ksrId", "customerId");
    }

}
