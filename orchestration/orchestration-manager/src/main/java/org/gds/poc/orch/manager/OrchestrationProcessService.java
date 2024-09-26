package org.gds.poc.orch.manager;

import org.gds.poc.orch.manager.library.BusinessState;
import org.gds.poc.orch.manager.library.CreateOrchProcessRequest;
import org.gds.poc.orch.manager.library.UpdateOrchProcessRequest;

public interface OrchestrationProcessService {
    void createNewProcess(CreateOrchProcessRequest nuovoProcesso);

    void aggiornaStatoProcesso(UpdateOrchProcessRequest aggiornamentoProcesso);

    BusinessState getBusinessState(String uuid);

    String getResult(String uuid);

}
