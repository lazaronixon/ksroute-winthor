package com.heuristica.ksroutewinthor.dozer.converters;

import com.heuristica.ksroutewinthor.apis.Vehicle;
import org.dozer.DozerConverter;

public class VeiculoStatusConverter extends DozerConverter<String, Vehicle.Status> {
    
    public VeiculoStatusConverter() {
        super(String.class, Vehicle.Status.class);
    }

    @Override
    public Vehicle.Status convertTo(String source, Vehicle.Status destination) {        
        switch (source) {
            case "L": return Vehicle.Status.available;
            case "B": return Vehicle.Status.blocked;
            case "I": return Vehicle.Status.inactive;
            case "V": return Vehicle.Status.traveling;
            default: return Vehicle.Status.available;
        }
    }

    @Override
    public String convertFrom(Vehicle.Status source, String destination) {        
        switch (source.name()) {
            case "available": return "L";
            case "blocked": return "B";
            case "inactive": return "I";
            case "traveling": return "V";
            default: return "available";
        }
    }    
    
}
