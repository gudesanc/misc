package org.gds.poc.orch.ssm.vebalizzazione;

import org.gds.packagediverso.orch.ssm.libreria.azione.GenericStateMachineAction;
import org.gds.poc.orch.ssm.notificazione.NotificazioneContext;

public abstract class AbstractVerbalizzazioneAction extends GenericStateMachineAction<VerbalizzazioneContext,VerbalizzazioneResult> {
    public AbstractVerbalizzazioneAction() {
        super(VerbalizzazioneContext.class,VerbalizzazioneResult.class);
    }
}
