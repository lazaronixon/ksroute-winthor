package com.heuristica.ksroutewinthor.dozer.mappings;

import com.heuristica.ksroutewinthor.apis.Line;
import com.heuristica.ksroutewinthor.dozer.converters.SituacaoBooleanConverter;
import com.heuristica.ksroutewinthor.models.Rota;
import org.dozer.loader.api.BeanMappingBuilder;
import static org.dozer.loader.api.FieldsMappingOptions.customConverter;

public class LineMapping extends BeanMappingBuilder {

    @Override
    protected void configure() {
        mapping(Rota.class, Line.class)
                .fields("codrota", "erpId")
                .fields("descricao", "description")
                .fields("ksrId", "id")
                .fields("situacao", "active", customConverter(SituacaoBooleanConverter.class));
    }

}
