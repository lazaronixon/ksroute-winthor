package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Region;
import com.heuristica.ksroutewinthor.models.Event;
import com.heuristica.ksroutewinthor.models.Regiao;
import com.heuristica.ksroutewinthor.repositories.RegiaoRepository;
import java.util.Map;
import java.util.Optional;
import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegiaoService {

    @Autowired private RegiaoRepository regioes;
    @Autowired private RecordService recordService;
    
    public Regiao findByEvent(Event event) {       
        return findByIdAndFetchRecord(Long.parseLong(event.getEventableId()));
    }
    
    public Regiao saveResponse(@Body Region region, @Headers Map headers) {
        recordService.saveResponse(region, headers);        
        return findByIdAndFetchRecord(Long.parseLong(region.getErpId()));
    }
    
    private Regiao findByIdAndFetchRecord(Long id) {
        Optional<Regiao> regiao = regioes.findById(id);
        regiao.ifPresent(r -> recordService.fetchRecord(r));
        return regiao.orElse(null); 
    }
}
