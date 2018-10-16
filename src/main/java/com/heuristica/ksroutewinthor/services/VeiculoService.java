package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Vehicle;
import com.heuristica.ksroutewinthor.models.Event;
import com.heuristica.ksroutewinthor.models.Veiculo;
import com.heuristica.ksroutewinthor.models.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VeiculoService {
    
    @Autowired private VeiculoRepository veiculos;
    @Autowired private Environment env;

    public Veiculo saveApiResponse(Vehicle vehicle) {
        Veiculo veiculo = veiculos.findById(Long.parseLong(vehicle.getErpId())).get();
        veiculo.setKsrId(vehicle.getId());
        return veiculos.save(veiculo);
    }
    
    public Veiculo getEventable(Event event) {
        Veiculo veiculo = veiculos.findById(Long.parseLong(event.getEventableId())).orElse(null);
        setFromEnviromentValues(veiculo); 
        return veiculo;
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
