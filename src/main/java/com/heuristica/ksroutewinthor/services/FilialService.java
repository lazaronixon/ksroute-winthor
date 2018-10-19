package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Branch;
import com.heuristica.ksroutewinthor.models.Event;
import com.heuristica.ksroutewinthor.models.Filial;
import com.heuristica.ksroutewinthor.repositories.FilialRepository;
import java.util.Map;
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
        return filiais.findById(event.getEventableId()).orElse(null);
    }
    
    public Filial saveResponse(@Body Branch branch, @Headers Map headers) {
        recordService.saveResponse(branch, headers);        
        return filiais.findById(branch.getErpId()).orElse(null);
    }
}
