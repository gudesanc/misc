package org.gds.poc.orch.ssm.vebalizzazione;

import org.gds.poc.orch.ssm.client.protocollo.DtoCreaProtocollo;
import org.gds.poc.orch.ssm.client.protocollo.DtoProtocollo;
import org.gds.poc.orch.ssm.client.protocollo.ProtocolloService;
import org.gds.poc.orch.ssm.libreria.ActionResult;
import org.gds.poc.orch.ssm.libreria.BusinessStatus;
import org.gds.poc.orch.ssm.libreria.GenericStateMachineAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class AcquisciProtocolloAction  extends GenericStateMachineAction<VerbalizzazioneContext> {
    private final Logger log = LoggerFactory.getLogger(AcquisciProtocolloAction.class);
    private final ProtocolloService protocolloService;
//    private final PersistInMemoryHandler handler;

    public AcquisciProtocolloAction(ProtocolloService protocolloService) {
        super(VerbalizzazioneContext.class);
        this.protocolloService = protocolloService;

    }



    @Override
    public ActionResult<VerbalizzazioneContext> execute(String uuid, final VerbalizzazioneContext businessCxt, BusinessStatus bs, StateContext<String, String> stateContext) {

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
                    VerbalizzazioneStateMachineConfig.VerbalizzazioneChangeEventEnum.PROTOCOLLO_ACQUISITO.name(),
                    businessCxt,
                    BusinessStatus.RUNNING);
        }
        catch (Throwable t){
            log.atInfo().setMessage("{} Errore acquisizione Protocollo {} ")
                    .addArgument(uuid)
                    .addArgument(t).log();
            return new ActionResult<>(uuid,
                    VerbalizzazioneStateMachineConfig.VerbalizzazioneChangeEventEnum.PROTOCOLLO_NON_ACQUISTO.name(),
                    businessCxt,
                    BusinessStatus.RUNNING);
        }
    }
}
