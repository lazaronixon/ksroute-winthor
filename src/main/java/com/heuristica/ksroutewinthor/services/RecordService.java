package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.models.Event;
import com.heuristica.ksroutewinthor.models.Record;
import com.heuristica.ksroutewinthor.models.RecordRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.heuristica.ksroutewinthor.apis.RecordableApi;

@Service
@Transactional
public class RecordService {

    @Autowired private RecordRepository records;    
    
    public Optional<Record> fetchFromEvent(Event event) {
        return fetch(event.getEventableId(), event.getEventableType());
    }   
    
    public Record saveRecordableApi(RecordableApi recordableApi) {
        Optional<Record> optionalRecord = fetch(recordableApi.getErpId(), recordableApi.getTableName());
        
        Record record = optionalRecord.orElse(new Record());
        record.setRecordableId(recordableApi.getErpId());
        record.setRecordableType(recordableApi.getTableName());
        record.setRemoteId(recordableApi.getId());
        return records.save(record);
    }
    
    private Optional<Record> fetch(String recordableId, String recordableType) {
        return records.findOptionalByRecordableIdAndRecordableType(recordableId, recordableType);
    }       
}
