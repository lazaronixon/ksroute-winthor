package com.heuristica.ksroutewinthor.dozer.mappings;

import com.heuristica.ksroutewinthor.apis.Region;
import com.heuristica.ksroutewinthor.models.Regiao;
import org.dozer.loader.api.BeanMappingBuilder;

public class RegionMapping extends BeanMappingBuilder {

    @Override
    protected void configure() {
        mapping(Regiao.class, Region.class)
                .fields("numregiao", "erpId")
                .fields("regiao", "description")
                .fields("uf", "state")
                .fields("ksrId", "id");
    }

}
