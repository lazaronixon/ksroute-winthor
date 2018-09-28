package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Line;
import com.heuristica.ksroutewinthor.models.Rota;
import com.heuristica.ksroutewinthor.models.RotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RotaService {

    @Autowired
    private RotaRepository rotas;

    @Transactional(propagation = Propagation.REQUIRES_NEW) 
    public Rota saveLine(Line line) {
        Rota rota = rotas.findById(Long.parseLong(line.getErpId())).get();
        rota.setKsrId(line.getId());
        return rotas.save(rota);
    }

}
