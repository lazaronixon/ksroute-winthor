package com.heuristica.ksroutewinthor.dozer.mapping;

import com.heuristica.ksroutewinthor.api.Customer;
import com.heuristica.ksroutewinthor.dozer.converters.StringBooleanConverter;
import com.heuristica.ksroutewinthor.model.Cliente;
import org.dozer.loader.api.BeanMappingBuilder;
import static org.dozer.loader.api.FieldsMappingOptions.customConverter;

public class CustomerMapping extends BeanMappingBuilder {

    @Override
    protected void configure() {
        mapping(Cliente.class, Customer.class)
                .fields("codcli", "erpId")
                .fields("cliente", "name")
                .fields("fantasia", "trade")
                .fields("ufent", "state")
                .fields("municent", "city")
                .fields("bairroent", "neighborhood")
                .fields("enderent", "address")
                .fields("cepent", "zipcode")
                .fields("ksrId", "id")
                .fields("subregion.ksrId", "subregion_id")            
                .fields("bloqueio", "active", customConverter(StringBooleanConverter.class));
    }

}
