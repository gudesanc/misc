package org.gds.poc.orch.ssm.notificazione;

import org.gds.poc.orch.ssm.client.protocollo.ProtocolloService;
import org.gds.poc.orch.ssm.libreria.ActionResult;
import org.gds.poc.orch.ssm.libreria.BusinessStatus;
import org.gds.poc.orch.ssm.libreria.GenericStateMachineAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;


@Component
public class NotificazioneAnnullaProtocolloAction extends GenericStateMachineAction<NotificazioneContext> {
    private final Logger log = LoggerFactory.getLogger(NotificazioneAnnullaProtocolloAction.class);
    private final ProtocolloService protocolloService;

    public NotificazioneAnnullaProtocolloAction( ProtocolloService protocolloService) {
        super(NotificazioneContext.class);
        this.protocolloService = protocolloService;

    }



    @Override
    public ActionResult<NotificazioneContext> execute(String uuid, final NotificazioneContext businessCxt, BusinessStatus bs, StateContext<String, String> stateContext) {

        log.atDebug().setMessage("Inizio annullamento protocollo per uuid {}, businessCxt: {}, bs: {}")
                .addArgument(uuid)
                .addArgument(businessCxt)
                .addArgument(bs)
                .log();
        ActionResult<NotificazioneContext> result;
        try {
            protocolloService.annullaProtocollo(businessCxt.getProtocollo());
            log.atInfo().setMessage("{} Protocollo {} annullato")
                    .addArgument(uuid).addArgument(businessCxt.getProtocollo()).log();
        }catch (Throwable t){
            log.atInfo().setMessage("{} Errore annullamento protocollo {} ")
                    .addArgument(uuid).addArgument(t).log();

        }
        return new ActionResult<>(uuid, NotificazioneStateMachineConfig.NotificazioneChangeEventEnum.NOTIFICA_CAMBIATO_STATO_PROTOCOLLO.name(),
                businessCxt,BusinessStatus.FAILED);

    }
}
