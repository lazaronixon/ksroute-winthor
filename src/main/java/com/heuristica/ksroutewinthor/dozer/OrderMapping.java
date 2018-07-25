package com.heuristica.ksroutewinthor.dozer;

import com.heuristica.ksroutewinthor.api.Order;
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
                .fields("posicao", "status", customConverter(PedidoPosicaoConverter.class))
                
                .fields("filial.codigo", "branch.erpId")
                .fields("filial.razaosocial", "branch.description")
                .fields("filial.ksrId", "branch.id")
                
                .fields("cliente.codcli", "customer.erpId")
                .fields("cliente.cliente", "customer.name")
                .fields("cliente.fantasia", "customer.trade")
                .fields("cliente.ufent", "customer.state")
                .fields("cliente.municent", "customer.city")
                .fields("cliente.bairroent", "customer.neighborhood")
                .fields("cliente.enderent", "customer.address")
                .fields("cliente.cepent", "customer.zipcode")
                .fields("cliente.ksrId", "customer.id")
                .fields("cliente.bloqueio", "customer.active", customConverter(StringBooleanConverter.class))
                
                .fields("cliente.praca.codpraca", "customer.subregion.erpId")
                .fields("cliente.praca.praca", "customer.subregion.description")                
                .fields("cliente.praca.ksrId", "customer.subregion.id")
                .fields("cliente.praca.situacao", "customer.subregion.active", customConverter(SituacaoBooleanConverter.class))
                
                .fields("cliente.praca.regiao.numregiao", "customer.subregion.region.erpId")
                .fields("cliente.praca.regiao.regiao", "customer.subregion.region.description")
                .fields("cliente.praca.regiao.uf", "customer.subregion.region.state")
                .fields("cliente.praca.regiao.ksrId", "customer.subregion.region.id")
                .fields("cliente.praca.regiao.status", "customer.subregion.region.active", customConverter(SituacaoBooleanConverter.class))
                
                .fields("cliente.praca.rotaExp.codrota", "customer.subregion.line.erpId")
                .fields("cliente.praca.rotaExp.descricao", "customer.subregion.line.description")                
                .fields("cliente.praca.rotaExp.ksrId", "customer.subregion.line.id")
                .fields("cliente.praca.rotaExp.situacao", "customer.subregion.line.active", customConverter(SituacaoBooleanConverter.class));
    }

}
