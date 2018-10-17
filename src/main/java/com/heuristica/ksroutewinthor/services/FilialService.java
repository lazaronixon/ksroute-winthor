package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Branch;
import com.heuristica.ksroutewinthor.models.Event;
import com.heuristica.ksroutewinthor.models.Filial;
import com.heuristica.ksroutewinthor.models.FilialRepository;
import com.heuristica.ksroutewinthor.models.Record;
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
    
    public Filial saveResponseApi(Branch branch) {
        Record record = recordService.saveRecordableApi(branch);        
        return findByIdAndSetRecord(branch.getErpId(), record);
    }
    
    private Filial findByIdAndSetRecord(String id, Record record) {
        Filial filial = filiais.findById(id).get();
        filial.setRecord(record);
        return filial;        
    }
}
