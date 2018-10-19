package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Vehicle;
import com.heuristica.ksroutewinthor.models.Event;
import com.heuristica.ksroutewinthor.models.Veiculo;
import com.heuristica.ksroutewinthor.models.VeiculoRepository;
import java.util.Map;
import java.util.Optional;
import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VeiculoService {
    
    @Autowired private Environment env;
    
    @Autowired private VeiculoRepository veiculos;
    @Autowired private RecordService recordService;

    public Veiculo findByEvent(Event event) {           
        return findByIdAndFetchRecord(Long.parseLong(event.getEventableId()));
    }
    
    public Veiculo saveResponse(@Body Vehicle vehicle, @Headers Map headers) {
        recordService.saveResponse(vehicle, headers);        
        return findByIdAndFetchRecord(Long.parseLong(vehicle.getErpId()));
    }
    
    private Veiculo findByIdAndFetchRecord(Long id) {
        Optional<Veiculo> veiculo = veiculos.findById(id);
        veiculo.ifPresent(r -> recordService.fetchRecord(r));
        return veiculo.orElse(null); 
    }
    
    private void setFromEnviromentValues(Veiculo veiculo) {
        if (veiculo != null) {
            veiculo.setStartAddressId(env.getProperty("ksroute.veiculo.startAddressId"));

            String vehicleTypeId;
            switch (String.valueOf(veiculo.getTipoveiculo())) {
                case "L":
                    vehicleTypeId = env.getProperty("ksroute.veiculo.leve.vehicleTypeId");
                    break;
                case "M":
                    vehicleTypeId = env.getProperty("ksroute.veiculo.medio.vehicleTypeId");
                    break;
                case "P":
                    vehicleTypeId = env.getProperty("ksroute.veiculo.pesado.vehicleTypeId");
                    break;
                case "E":
                    vehicleTypeId = env.getProperty("ksroute.veiculo.extrapesado.vehicleTypeId");
                    break;
                default:
                    vehicleTypeId = env.getProperty("ksroute.veiculo.leve.vehicleTypeId");
            }
            veiculo.setVehicleTypeId(vehicleTypeId);
        }
    }
    
}
