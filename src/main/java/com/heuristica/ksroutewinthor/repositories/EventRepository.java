package com.heuristica.ksroutewinthor.repositories;

import com.heuristica.ksroutewinthor.models.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Long>  {
    
}
