package com.heuristica.ksroutewinthor.models;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RecordRepository extends CrudRepository<Record, Long>  {
    
    Optional<Record> findOptionalByRecordableIdAndRecordableType(String recordableId, String recordableType);
    
}
