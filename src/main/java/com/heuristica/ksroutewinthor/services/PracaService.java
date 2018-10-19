package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Subregion;
import com.heuristica.ksroutewinthor.models.Event;
import com.heuristica.ksroutewinthor.models.Praca;
import com.heuristica.ksroutewinthor.repositories.PracaRepository;
import java.util.Map;
import java.util.Optional;
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
        return findByIdAndFetchRecord(Long.parseLong(event.getEventableId()));
    }
    
    public Praca saveResponse(@Body Subregion subregion, @Headers Map headers) {
        recordService.saveResponse(subregion, headers);        
        return findByIdAndFetchRecord(Long.parseLong(subregion.getErpId()));
    }
    
    private Praca findByIdAndFetchRecord(Long id) {
        Optional<Praca> praca = pracas.findById(id);
        praca.ifPresent(p -> recordService.fetchRecord(p));
        return praca.orElse(null); 
    }

}
