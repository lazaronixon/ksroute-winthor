package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Branch;
import com.heuristica.ksroutewinthor.models.Event;
import com.heuristica.ksroutewinthor.models.Filial;
import com.heuristica.ksroutewinthor.repositories.FilialRepository;
import java.util.Map;
import java.util.Optional;
import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FilialService {
    
    @Autowired private FilialRepository filiais;
    @Autowired private RecordService recordService;
    
    public Filial findByEvent(Event event) {       
        return findByIdAndFetchRecord(event.getEventableId());
    }
    
    public Filial saveResponse(@Body Branch branch, @Headers Map headers) {
        recordService.saveResponse(branch, headers);        
        return findByIdAndFetchRecord(branch.getErpId());
    }
    
    private Filial findByIdAndFetchRecord(String id) {
        Optional<Filial> filial = filiais.findById(id);
        filial.ifPresent(f -> recordService.fetchRecord(f));
        return filial.orElse(null); 
    }
}
