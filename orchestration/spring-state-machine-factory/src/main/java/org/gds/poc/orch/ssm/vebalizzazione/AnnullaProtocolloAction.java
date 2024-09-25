package org.gds.poc.orch.ssm.vebalizzazione;

import org.gds.poc.orch.ssm.client.protocollo.DtoCreaProtocollo;
import org.gds.poc.orch.ssm.client.protocollo.ProtocolloService;
import org.gds.poc.orch.ssm.libreria.ActionResult;
import org.gds.poc.orch.ssm.libreria.BusinessStatus;
import org.gds.poc.orch.ssm.libreria.GenericStateMachineAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
public class AnnullaProtocolloAction extends GenericStateMachineAction<VerbalizzazioneContext> {
    private final Logger log = LoggerFactory.getLogger(AnnullaProtocolloAction.class);
    private final ProtocolloService protocolloService;

    public AnnullaProtocolloAction( ProtocolloService protocolloService) {
        super(VerbalizzazioneContext.class);
        this.protocolloService = protocolloService;

    }



    @Override
    public ActionResult<VerbalizzazioneContext> execute(String uuid, final VerbalizzazioneContext businessCxt, BusinessStatus bs, StateContext<String, String> stateContext) {

        log.atDebug().setMessage("Inizio annullamento protocollo per uuid {}, businessCxt: {}, bs: {}")
                .addArgument(uuid)
                .addArgument(businessCxt)
                .addArgument(bs)
                .log();
        ActionResult<VerbalizzazioneContext> result;
        try {
            protocolloService.annullaProtocollo(businessCxt.getProtocollo());
            log.atInfo().setMessage("{} Protocollo {} annullato")
                    .addArgument(uuid).addArgument(businessCxt.getProtocollo()).log();
        }catch (Throwable t){
            log.atInfo().setMessage("{} Errore annullamento protocollo {} ")
                    .addArgument(uuid).addArgument(t).log();

        }
        return new ActionResult<>(uuid,VerbalizzazioneStateMachineConfig.VerbalizzazioneChangeEventEnum.CAMBIATO_STATO_PROTOCOLLO.name(),
                businessCxt,BusinessStatus.FAILED);

    }
}
