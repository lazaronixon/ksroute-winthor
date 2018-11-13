package com.heuristica.ksroutewinthor.models;

import com.heuristica.ksroutewinthor.utils.ApplicationContextHolder;
import com.heuristica.ksroutewinthor.services.RecordService;
import javax.persistence.PostLoad;

public class RecordableListener {
    
    @PostLoad
    public void fetchRecord(Recordable recordable) {
        RecordService recordService = ApplicationContextHolder.getBean(RecordService.class);
        Record record = recordService.findByRecordable(recordable).orElse(null);
        recordable.setRecord(record);
    }   
    
}
