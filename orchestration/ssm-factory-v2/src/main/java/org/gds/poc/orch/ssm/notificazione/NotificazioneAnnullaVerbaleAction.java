package org.gds.poc.orch.ssm.notificazione;

import org.gds.packagediverso.orch.ssm.libreria.azione.ActionResult;
import org.gds.packagediverso.orch.ssm.libreria.BusinessState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class NotificazioneAnnullaVerbaleAction  extends AbstractNotificazioneAction {
    private final Logger log = LoggerFactory.getLogger(NotificazioneAnnullaVerbaleAction.class);



    @Override
    public ActionResult<NotificazioneContext,Void> execute(String uuid, NotificazioneContext businessCtx, BusinessState bs, StateContext<String, String> stateContext) {

            log.atInfo().setMessage("{} Notifica consolidato: {}")
                    .addArgument(uuid)
                    .addArgument(businessCtx.getIdNotifica()).log();
        return new ActionResult<>(uuid,
                NotificazioneStateMachineConfig.NotificazioneEventEnum.CAMBIATO_STATO_NOTIFICA.name(),businessCtx, BusinessState.FAILED);
    }
}
