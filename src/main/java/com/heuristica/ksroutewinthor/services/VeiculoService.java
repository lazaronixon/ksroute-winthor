package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Vehicle;
import com.heuristica.ksroutewinthor.models.Event;
import com.heuristica.ksroutewinthor.models.Veiculo;
import com.heuristica.ksroutewinthor.repositories.VeiculoRepository;
import java.util.List;
import java.util.Map;
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
    @Autowired private EventService eventService;    

    public Veiculo findByEvent(Event event) {           
        return veiculos.findById(Long.parseLong(event.getEventableId())).orElse(null);
    }
    
    public Veiculo saveResponse(@Body Vehicle vehicle, @Headers Map headers) {
        recordService.saveResponse(vehicle, headers);        
        return veiculos.findById(Long.parseLong(vehicle.getErpId())).orElse(null);
    }    
    
    public void loadEvents() {
        List<Veiculo> result = veiculos.findAllActive();
        result.forEach(v -> eventService.insertRecordable(v));        
    }
    
    public void setFromEnviromentValues(Veiculo veiculo) {
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
        
        veiculo.setStartAddressId(env.getProperty("ksroute.veiculo.startAddressId"));
        veiculo.setVehicleTypeId(vehicleTypeId);
    }
    
}
