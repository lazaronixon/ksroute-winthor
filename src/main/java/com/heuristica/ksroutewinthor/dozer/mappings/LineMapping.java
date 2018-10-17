package com.heuristica.ksroutewinthor.dozer.mappings;

import com.heuristica.ksroutewinthor.apis.Line;
import com.heuristica.ksroutewinthor.models.Rota;
import org.dozer.loader.api.BeanMappingBuilder;

public class LineMapping extends BeanMappingBuilder {

    @Override
    protected void configure() {
        mapping(Rota.class, Line.class)
                .fields("codrota", "erpId")
                .fields("descricao", "description")
                .fields("record.remoteId", "id");
    }

}
