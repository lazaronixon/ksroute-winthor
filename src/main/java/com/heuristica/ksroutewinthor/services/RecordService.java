package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.models.Record;
import com.heuristica.ksroutewinthor.repositories.RecordRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.heuristica.ksroutewinthor.apis.RecordableApi;
import com.heuristica.ksroutewinthor.models.Recordable;
import java.util.Map;
import org.apache.camel.ExchangeProperty;

@Service
@Transactional
public class RecordService {

    @Autowired private RecordRepository records;    

    public Optional<Record> findByRecordable(Recordable recordable) {
        return records.findOptionalByRecordableIdAndRecordableType(recordable.getRecordableId(), recordable.getRecordableType());
    }   
    
    public void saveResponse(RecordableApi recordable, Map<String, String> headers) {
        Optional<Record> optionalRecord = findByRecordableApi(recordable);
        
        Record record = optionalRecord.orElse(new Record());
        record.setRecordableId(recordable.getErpId());
        record.setRecordableType(recordable.getRecordableType());
        record.setRemoteId(recordable.getId());
        record.setRequestId(headers.get("X-Request-Id"));
        record.setEtag(headers.get("Etag"));
        records.save(record);
    }    
    
    public void deleteByRecordId(@ExchangeProperty("recordId") Long id) {
        records.deleteById(id);
    }    
    
    private Optional<Record> findByRecordableApi(RecordableApi recordable) {
        return records.findOptionalByRecordableIdAndRecordableType(recordable.getErpId(), recordable.getRecordableType());
    }    
}
