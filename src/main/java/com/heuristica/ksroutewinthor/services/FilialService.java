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

    private static final String TABLE_NAME = "PCFILIAL";
    
    @Autowired private FilialRepository filiais;
    @Autowired private RecordService recordService;
    
    public Filial findByEvent(Event event) {           
        return findByIdAndFetchRecord(event.getEventableId());
    }
    
    public Filial saveResponse(@Body Branch branch, @Headers Map headers) {
        recordService.saveResponse(branch, headers);        
        return findByIdAndFetchRecord(branch.getErpId());
    }
    
    public Filial fetchRecord(Filial filial) {
        Record record = recordService.findByRecordable(filial.getCodigo(), TABLE_NAME).orElse(null);
        filial.setRecord(record);
        return filial;
    }
    
    private Filial findByIdAndFetchRecord(String id) {
        Record record = recordService.findByRecordable(id, TABLE_NAME).orElse(null);
        
        Optional<Filial> filial = filiais.findById(id);
        filial.ifPresent(f -> f.setRecord(record));
        return filial.orElse(null); 
    }
}
