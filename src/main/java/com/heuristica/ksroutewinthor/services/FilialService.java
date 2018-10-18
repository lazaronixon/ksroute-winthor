package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Branch;
import com.heuristica.ksroutewinthor.models.Event;
import com.heuristica.ksroutewinthor.models.Filial;
import com.heuristica.ksroutewinthor.models.FilialRepository;
import com.heuristica.ksroutewinthor.models.Record;
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
        Record record = recordService.findByEvent(event);        
        return findByIdAndSetRecord(event.getEventableId(), record);
    }
    
    public Filial saveResponse(@Body Branch branch, @Headers Map headers) {
        Record record = recordService.saveResponse(branch, headers);        
        return findByIdAndSetRecord(branch.getErpId(), record);
    }
    
    private Filial findByIdAndSetRecord(String id, Record record) {
        Optional<Filial> filial = filiais.findById(id);
        filial.ifPresent(f -> f.setRecord(record)) ;
        return filial.orElse(null);        
    }
}
