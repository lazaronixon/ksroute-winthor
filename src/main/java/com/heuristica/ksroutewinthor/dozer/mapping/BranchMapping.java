package com.heuristica.ksroutewinthor.dozer.mapping;

import com.heuristica.ksroutewinthor.apis.Branch;
import com.heuristica.ksroutewinthor.models.Filial;
import org.dozer.loader.api.BeanMappingBuilder;

public class BranchMapping extends BeanMappingBuilder {

    @Override
    protected void configure() {
        mapping(Filial.class, Branch.class)
                .fields("codigo", "erpId")
                .fields("razaosocial", "description")
                .fields("ksrId", "id");
    }

}
