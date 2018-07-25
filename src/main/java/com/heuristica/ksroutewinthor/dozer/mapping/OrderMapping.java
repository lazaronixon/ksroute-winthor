package com.heuristica.ksroutewinthor.dozer.mapping;

import com.heuristica.ksroutewinthor.api.Order;
import com.heuristica.ksroutewinthor.dozer.converters.PedidoPosicaoConverter;
import com.heuristica.ksroutewinthor.model.Pedido;
import org.dozer.loader.api.BeanMappingBuilder;
import static org.dozer.loader.api.FieldsMappingOptions.copyByReference;
import static org.dozer.loader.api.FieldsMappingOptions.customConverter;

public class OrderMapping extends BeanMappingBuilder {

    @Override
    protected void configure() {
        mapping(Pedido.class, Order.class)
                .fields("numped", "erpId")
                .fields("data", "issuedAt", copyByReference())
                .fields("vlatend", "amount")
                .fields("totpeso", "weight")
                .fields("totvolume", "volume")
                .fields("ksrId", "id")
                .fields("filial.ksrId", "branch_id")
                .fields("cliente.ksrId", "customer_id")
                .fields("posicao", "status", customConverter(PedidoPosicaoConverter.class));
    }

}
