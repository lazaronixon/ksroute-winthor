package com.heuristica.ksroutewinthor.dozer.mapping;

import com.heuristica.ksroutewinthor.api.Line;
import com.heuristica.ksroutewinthor.dozer.converters.SituacaoBooleanConverter;
import com.heuristica.ksroutewinthor.model.RotaExp;
import org.dozer.loader.api.BeanMappingBuilder;
import static org.dozer.loader.api.FieldsMappingOptions.customConverter;

public class LineMapping extends BeanMappingBuilder {

    @Override
    protected void configure() {
        mapping(RotaExp.class, Line.class)
                .fields("codrota", "erpId")
                .fields("descricao", "description")
                .fields("ksrId", "id")
                .fields("situacao", "active", customConverter(SituacaoBooleanConverter.class));
    }

}
