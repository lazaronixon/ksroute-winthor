package com.heuristica.ksroutewinthor.dozer.mappings;

import com.heuristica.ksroutewinthor.apis.Subregion;
import com.heuristica.ksroutewinthor.dozer.converters.SituacaoBooleanConverter;
import com.heuristica.ksroutewinthor.models.Praca;
import org.dozer.loader.api.BeanMappingBuilder;
import static org.dozer.loader.api.FieldsMappingOptions.customConverter;

public class SubregionMapping extends BeanMappingBuilder {

    @Override
    protected void configure() {
        mapping(Praca.class, Subregion.class)
                .fields("codpraca", "erpId")
                .fields("praca", "description")
                .fields("record.remoteId", "id")
                .fields("regiao.record.remoteId", "regionId")
                .fields("rota.record.remoteId", "lineId")
                .fields("situacao", "active", customConverter(SituacaoBooleanConverter.class));
    }

}
