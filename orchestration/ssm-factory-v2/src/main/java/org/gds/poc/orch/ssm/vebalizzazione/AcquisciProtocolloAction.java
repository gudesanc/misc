package org.gds.poc.orch.ssm.vebalizzazione;

import org.gds.poc.orch.ssm.client.protocollo.DtoCreaProtocollo;
import org.gds.poc.orch.ssm.client.protocollo.DtoProtocollo;
import org.gds.poc.orch.ssm.client.protocollo.ProtocolloService;
import org.gds.packagediverso.orch.ssm.libreria.azione.ActionResult;
import org.gds.packagediverso.orch.ssm.libreria.BusinessState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class AcquisciProtocolloAction  extends AbstractVerbalizzazioneAction{
    private final Logger log = LoggerFactory.getLogger(AcquisciProtocolloAction.class);
    private final ProtocolloService protocolloService;
//    private final PersistInMemoryHandler handler;

    public AcquisciProtocolloAction(ProtocolloService protocolloService) {
        this.protocolloService = protocolloService;

    }



    @Override
    public ActionResult<VerbalizzazioneContext,VerbalizzazioneResult> execute(String uuid, final VerbalizzazioneContext businessCxt, BusinessState bs, StateContext<String, String> stateContext) {

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
                    VerbalizzazioneStateMachineConfig.VerbalizzazioneEventEnum.PROTOCOLLO_ACQUISITO.name(),
                    businessCxt,
                    BusinessState.RUNNING);
        }
        catch (Throwable t){
            log.atInfo().setMessage("{} Errore acquisizione Protocollo {} ")
                    .addArgument(uuid)
                    .addArgument(t).log();
            return new ActionResult<>(uuid,
                    VerbalizzazioneStateMachineConfig.VerbalizzazioneEventEnum.PROTOCOLLO_NON_ACQUISTO.name(),
                    businessCxt,
                    BusinessState.RUNNING);
        }
    }
}
