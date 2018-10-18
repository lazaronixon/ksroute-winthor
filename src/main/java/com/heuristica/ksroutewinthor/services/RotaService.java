package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Line;
import com.heuristica.ksroutewinthor.models.Event;
import com.heuristica.ksroutewinthor.models.Record;
import com.heuristica.ksroutewinthor.models.Rota;
import com.heuristica.ksroutewinthor.models.repositories.RotaRepository;
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
    
    public Rota fetchRecord(Rota rota) {
        Record record = recordService.findByRecordable(rota).orElse(null);
        rota.setRecord(record);
        return rota;
    }
    
    private Rota findByIdAndFetchRecord(Long id) {
        Optional<Rota> rota = rotas.findById(id);
        rota.ifPresent(f -> fetchRecord(f));
        return rota.orElse(null); 
    }

}
