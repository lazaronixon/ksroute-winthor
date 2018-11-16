package com.heuristica.ksroutewinthor.dozer.mappings;

import com.heuristica.ksroutewinthor.apis.Region;
import com.heuristica.ksroutewinthor.dozer.converters.SituacaoBooleanConverter;
import com.heuristica.ksroutewinthor.models.Regiao;
import org.dozer.loader.api.BeanMappingBuilder;
import static org.dozer.loader.api.FieldsMappingOptions.customConverter;

public class RegionMapping extends BeanMappingBuilder {

    @Override
    protected void configure() {
        mapping(Regiao.class, Region.class)
                .fields("numregiao", "erpId")
                .fields("regiao", "description")
                .fields("uf", "state")
                .fields("record.remoteId", "id")
                .fields("status", "active", customConverter(SituacaoBooleanConverter.class));
    }

}
