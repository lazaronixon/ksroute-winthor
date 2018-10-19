package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Line;
import com.heuristica.ksroutewinthor.models.Event;
import com.heuristica.ksroutewinthor.models.Rota;
import com.heuristica.ksroutewinthor.repositories.RotaRepository;
import java.util.Map;
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
        return rotas.findById(Long.parseLong(event.getEventableId())).orElse(null);
    }
    
    public Rota saveResponse(@Body Line line, @Headers Map headers) {
        recordService.saveResponse(line, headers);        
        return rotas.findById(Long.parseLong(line.getErpId())).orElse(null);
    }

}
