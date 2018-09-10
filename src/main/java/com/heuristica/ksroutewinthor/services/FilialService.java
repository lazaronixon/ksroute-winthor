package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.models.Filial;
import com.heuristica.ksroutewinthor.models.FilialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FilialService {

    @Autowired
    private FilialRepository filiais;

    public Filial findFilial(String id) {
        return filiais.findById(id).get();
    }

    public Filial saveFilial(Filial filial) {
        return filiais.save(filial);
    }

}
