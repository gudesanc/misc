package org.gds.poc.orch.ssm.notificazione;

import org.gds.poc.orch.ssm.libreria.GenericStateMachineAction;

public abstract class  AbstractNotificazioneAction extends GenericStateMachineAction<NotificazioneContext,Void> {
    public AbstractNotificazioneAction() {
        super(NotificazioneContext.class,Void.class);
    }
}
