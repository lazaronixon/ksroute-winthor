package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Subregion;
import com.heuristica.ksroutewinthor.models.Event;
import com.heuristica.ksroutewinthor.models.Praca;
import com.heuristica.ksroutewinthor.repositories.PracaRepository;
import java.util.Map;
import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PracaService {

    @Autowired private PracaRepository pracas;
    @Autowired private RecordService recordService;
    
    public Praca findByEvent(Event event) {       
        return pracas.findById(Long.parseLong(event.getEventableId())).orElse(null);
    }
    
    public Praca saveResponse(@Body Subregion subregion, @Headers Map headers) {
        recordService.saveResponse(subregion, headers);        
        return pracas.findById(Long.parseLong(subregion.getErpId())).orElse(null);
    }

}
