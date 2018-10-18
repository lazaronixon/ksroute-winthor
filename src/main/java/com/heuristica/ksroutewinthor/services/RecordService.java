package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.models.Event;
import com.heuristica.ksroutewinthor.models.Record;
import com.heuristica.ksroutewinthor.models.RecordRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.heuristica.ksroutewinthor.apis.Recordable;
import java.util.Map;

@Service
@Transactional
public class RecordService {

    @Autowired private RecordRepository records;    
    
    public Record findByEvent(Event event) {
        return findByRecordable(event.getEventableId(), event.getEventableType()).orElse(null);
    }   
    
    public Record saveResponse(Recordable recordable, Map<String, String> headers) {
        Optional<Record> optionalRecord = findByRecordable(recordable.getErpId(), recordable.getTableName());
        
        Record record = optionalRecord.orElse(new Record());
        record.setRecordableId(recordable.getErpId());
        record.setRecordableType(recordable.getTableName());
        record.setRemoteId(recordable.getId());
        record.setRequestId(headers.get("X-Request-Id"));
        record.setEtag(headers.get("Etag"));
        return records.save(record);
    }
    
    public void delete(Record record) {
        records.delete(record);
    }
    
    private Optional<Record> findByRecordable(String recordableId, String recordableType) {
        return records.findOptionalByRecordableIdAndRecordableType(recordableId, recordableType);
    }       
}
