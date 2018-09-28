package com.heuristica.ksroutewinthor.services;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsumService {

    @PersistenceContext private EntityManager entityManager;      

    @Transactional
    public Long getNextSequence(String field) {
        String jpqlSelect = "SELECT o." + field + " FROM Consum o";
        Query query = entityManager.createQuery(jpqlSelect);
        query.setLockMode(LockModeType.PESSIMISTIC_READ);
        Long nextNumber = (Long) query.getSingleResult();
        String jpqlUpdate = "UPDATE Consum c SET c." + field + " = "+ (nextNumber.intValue() + 1);
        entityManager.createQuery(jpqlUpdate).executeUpdate();
        return nextNumber;
    }

}
