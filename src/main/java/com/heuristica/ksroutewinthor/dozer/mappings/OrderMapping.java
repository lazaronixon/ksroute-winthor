package com.heuristica.ksroutewinthor.dozer.mappings;

import com.heuristica.ksroutewinthor.apis.Order;
import com.heuristica.ksroutewinthor.dozer.converters.PedidoPosicaoConverter;
import com.heuristica.ksroutewinthor.models.Pedido;
import org.dozer.loader.api.BeanMappingBuilder;
import static org.dozer.loader.api.FieldsMappingOptions.copyByReference;
import static org.dozer.loader.api.FieldsMappingOptions.customConverter;

public class OrderMapping extends BeanMappingBuilder {

    @Override
    protected void configure() {
        mapping(Pedido.class, Order.class)
                .fields("numped", "erpId")                
                .fields("vlatend", "amount")
                .fields("totpeso", "weight")
                .fields("totvolume", "volume")
                .fields("record.remoteId", "id")
                .fields("filial.record.remoteId", "branchId")
                .fields("cliente.record.remoteId", "customerId")
                .fields("data", "issuedAt", copyByReference())
                .fields("posicao", "status", customConverter(PedidoPosicaoConverter.class));
    }

}
