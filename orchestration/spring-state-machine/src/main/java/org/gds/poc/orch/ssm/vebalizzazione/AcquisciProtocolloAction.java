package org.gds.poc.orch.ssm.vebalizzazione;

import org.gds.poc.orch.ssm.client.protocollo.ProtocolloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class AcquisciProtocolloAction implements Action<String,String> {
    private final Logger log = LoggerFactory.getLogger(AcquisciProtocolloAction.class);
    private final ProtocolloService protocolloService;

    public AcquisciProtocolloAction(ProtocolloService protocolloService) {
        this.protocolloService = protocolloService;
    }


    @Override
    public void execute(StateContext<String, String> stateContext) {
        log.atDebug().setMessage("Inizio acqusizione protocollo per????").log();
    }
}
