package com.heuristica.ksroutewinthor.dozer.mappings;

import com.heuristica.ksroutewinthor.apis.Branch;
import com.heuristica.ksroutewinthor.models.Filial;
import com.github.dozermapper.core.loader.api.BeanMappingBuilder;

public class BranchMapping extends BeanMappingBuilder {

    @Override
    protected void configure() {
        mapping(Filial.class, Branch.class)
                .fields("codigo", "erpId")
                .fields("razaosocial", "description")
                .fields("record.remoteId", "id");
    }

}
