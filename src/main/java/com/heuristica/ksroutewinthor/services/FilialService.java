package com.heuristica.ksroutewinthor.services;

import com.heuristica.ksroutewinthor.apis.Branch;
import com.heuristica.ksroutewinthor.models.Filial;
import com.heuristica.ksroutewinthor.models.FilialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FilialService {

    @Autowired
    private FilialRepository filiais;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Filial saveBranch(Branch branch) {
        Filial filial = filiais.findById(branch.getErpId()).get();
        filial.setKsrId(branch.getId());
        return filiais.save(filial);
    }

}
