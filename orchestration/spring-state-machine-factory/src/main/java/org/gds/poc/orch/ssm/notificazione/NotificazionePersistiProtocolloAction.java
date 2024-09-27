package org.gds.poc.orch.ssm.notificazione;

import org.gds.poc.orch.ssm.libreria.ActionResult;
import org.gds.poc.orch.ssm.libreria.BusinessState;
import org.gds.poc.orch.ssm.libreria.GenericStateMachineAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class NotificazionePersistiProtocolloAction extends GenericStateMachineAction<NotificazioneContext,Void> {
    private final Logger log = LoggerFactory.getLogger(NotificazionePersistiProtocolloAction.class);

    public NotificazionePersistiProtocolloAction() {
        super(NotificazioneContext.class,Void.class);
    }


    @Override
    public ActionResult<NotificazioneContext,Void> execute(String uuid, NotificazioneContext businessCtx, BusinessState bs, StateContext<String, String> stateContext) {

        log.atDebug().setMessage("Inizio consolidamento protocollo per uuid {}, ctx {},  bs: {}")
                .addArgument(uuid)
                .addArgument(businessCtx)
                .addArgument(bs)
                .log();
        if(((int)(Math.random()*10000))%2==0){
            log.atInfo().setMessage("{} Notifica consolidato: {}")
                    .addArgument(uuid)
                    .addArgument(businessCtx.getIdNotifica()).log();
            return new ActionResult<>(uuid, NotificazioneStateMachineConfig.NotificazioneEventEnum.PROTOCOLLO_IMPOSTATO_SU_NOTIFICA.name(),
                    businessCtx, BusinessState.COMPLETED);

        }else{
            log.atWarn().setMessage("{} Errore consolidamento notifica : {}")
                    .addArgument(uuid)
                    .addArgument("...non divisibile per 2").log();
            return new ActionResult<>(uuid, NotificazioneStateMachineConfig.NotificazioneEventEnum.PRTOCOLLO_NON_IMPOSTATO_SU_NOTIFICA.name(),businessCtx, BusinessState.RUNNING);
        }
    }
}
