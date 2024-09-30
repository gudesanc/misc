package org.gds.poc.orch.ssm.notificazione;


import org.gds.packagediverso.orch.ssm.libreria.azione.GenericStateMachineAction;

public abstract class  AbstractNotificazioneAction extends GenericStateMachineAction<NotificazioneContext,Void> {
    public AbstractNotificazioneAction() {
        super(NotificazioneContext.class,Void.class);
    }
}
