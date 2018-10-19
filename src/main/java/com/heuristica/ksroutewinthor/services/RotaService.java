package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Line;
import com.heuristica.ksroutewinthor.models.Event;
import com.heuristica.ksroutewinthor.models.Record;
import com.heuristica.ksroutewinthor.models.Rota;
import com.heuristica.ksroutewinthor.repositories.RotaRepository;
import java.util.Map;
import java.util.Optional;
import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RotaService {

    @Autowired private RotaRepository rotas;
    @Autowired private RecordService recordService;

    public Rota findByEvent(Event event) {           
        return findByIdAndFetchRecord(Long.parseLong(event.getEventableId()));
    }
    
    public Rota saveResponse(@Body Line line, @Headers Map headers) {
        recordService.saveResponse(line, headers);        
        return findByIdAndFetchRecord(Long.parseLong(line.getErpId()));
    }
    
    private Rota findByIdAndFetchRecord(Long id) {
        Optional<Rota> rota = rotas.findById(id);
        rota.ifPresent(r -> recordService.fetchRecord(r));
        return rota.orElse(null); 
    }

}
