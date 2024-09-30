package org.gds.packagediverso.orch.ssm.libreria.notifica;

public interface OrchestrationManagerNotification {
    void notificaNuovoProcesso(CreateOrchProcessRequest nuovoProcesso);
    void notificaCambioStato(UpdateOrchProcessRequest nuovoStato);
}
