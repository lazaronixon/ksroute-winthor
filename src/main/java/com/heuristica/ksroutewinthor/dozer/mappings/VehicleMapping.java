package com.heuristica.ksroutewinthor.dozer.mappings;

import com.heuristica.ksroutewinthor.apis.Vehicle;
import com.heuristica.ksroutewinthor.dozer.converters.VeiculoStatusConverter;
import com.heuristica.ksroutewinthor.models.Veiculo;
import org.dozer.loader.api.BeanMappingBuilder;
import static org.dozer.loader.api.FieldsMappingOptions.customConverter;

public class VehicleMapping  extends BeanMappingBuilder{
    
    @Override
    protected void configure() {
        mapping(Veiculo.class, Vehicle.class)
                .fields("codveiculo", "erpId")
                .fields("descricao", "description")
                .fields("record.remoteId", "id")
                .fields("situacao", "status", customConverter(VeiculoStatusConverter.class));
    }
    
}
