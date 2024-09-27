package org.gds.poc.orch.ssm.notificazione;

import org.gds.poc.orch.ssm.client.protocollo.DtoCreaProtocollo;
import org.gds.poc.orch.ssm.client.protocollo.DtoProtocollo;
import org.gds.poc.orch.ssm.client.protocollo.ProtocolloService;
import org.gds.poc.orch.ssm.libreria.ActionResult;
import org.gds.poc.orch.ssm.libreria.BusinessState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class NotificazioneAcquisciProtocolloAction  extends AbstractNotificazioneAction {
    private final Logger log = LoggerFactory.getLogger(NotificazioneAcquisciProtocolloAction.class);
    private final ProtocolloService protocolloService;
//    private final PersistInMemoryHandler handler;

    public NotificazioneAcquisciProtocolloAction(ProtocolloService protocolloService) {
        this.protocolloService = protocolloService;
    }



    @Override
    public ActionResult<NotificazioneContext,Void> execute(String uuid, final NotificazioneContext businessCxt, BusinessState bs, StateContext<String, String> stateContext) {

        log.atDebug().setMessage("Inizio acqusizione protocollo per uuid {}, businessCxt: {}, bs: {}")
                .addArgument(uuid)
                .addArgument(businessCxt)
                .addArgument(bs)
                .log();
        try {
            DtoProtocollo protocollo = protocolloService.protocolla(new DtoCreaProtocollo("oggetto", "mittente", "destinatario"));
            log.atInfo().setMessage("{} Protocollo {} acqusito")
                    .addArgument(uuid)
                    .addArgument(protocollo).log();
            businessCxt.setProtocollo(protocollo);
            return new ActionResult<>(uuid,
                    NotificazioneStateMachineConfig.NotificazioneEventEnum.NOTIFICA_PROTOCOLLO_ACQUISITO.name(),
                    businessCxt,
                    BusinessState.RUNNING);
        }
        catch (Throwable t){
            log.atInfo().setMessage("{} Errore acquisizione Protocollo {} ")
                    .addArgument(uuid)
                    .addArgument(t).log();
            return new ActionResult<>(uuid,
                    NotificazioneStateMachineConfig.NotificazioneEventEnum.NOTIFICA_PROTOCOLLO_NON_ACQUISTO.name(),
                    businessCxt,
                    BusinessState.RUNNING);
        }
    }
}
