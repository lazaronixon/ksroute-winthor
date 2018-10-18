 package com.heuristica.ksroutewinthor.dozer.mappings;

import com.heuristica.ksroutewinthor.apis.Customer;
import com.heuristica.ksroutewinthor.models.Cliente;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingOptions;

public class CustomerMapping extends BeanMappingBuilder {

    @Override
    protected void configure() {
        mapping(Cliente.class, Customer.class)
                .fields("codcli", "erpId")
                .fields("cliente", "name")
                .fields("fantasia", "trade")
                .fields("estent", "state")
                .fields("municent", "city")
                .fields("bairroent", "neighborhood")
                .fields("enderent", "address")
                .fields("cepent", "zipcode")
                .fields("record.remoteId", "id")
                .fields("praca.record.remoteId", "subregionId");
    }

}
