package org.gds.poc.orch.ssm.notificazione;

import org.gds.poc.orch.ssm.libreria.ActionResult;
import org.gds.poc.orch.ssm.libreria.BusinessStatus;
import org.gds.poc.orch.ssm.libreria.GenericStateMachineAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class NotificazioneAnnullaVerbaleAction extends GenericStateMachineAction<NotificazioneContext> {
    private final Logger log = LoggerFactory.getLogger(NotificazioneAnnullaVerbaleAction.class);

    public NotificazioneAnnullaVerbaleAction( ) {
        super(NotificazioneContext.class);
    }


    @Override
    public ActionResult<NotificazioneContext> execute(String uuid, NotificazioneContext businessCtx, BusinessStatus bs, StateContext<String, String> stateContext) {

            log.atInfo().setMessage("{} Notifica consolidato: {}")
                    .addArgument(uuid)
                    .addArgument(businessCtx.getIdNotifica()).log();
        return new ActionResult<>(uuid,
                NotificazioneStateMachineConfig.NotificazioneChangeEventEnum.CAMBIATO_STATO_NOTIFICA.name(),businessCtx,BusinessStatus.FAILED);
    }
}
