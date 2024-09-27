package org.gds.poc.orch.ssm.vebalizzazione;

import org.gds.poc.orch.ssm.client.verbale.DtoVerbale;
import org.gds.poc.orch.ssm.client.verbale.VerbaleService;
import org.gds.poc.orch.ssm.libreria.ActionResult;
import org.gds.poc.orch.ssm.libreria.BusinessState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class PersistiProtocolloAction extends AbstractVerbalizzazioneAction{
    private final Logger log = LoggerFactory.getLogger(PersistiProtocolloAction.class);
    private final VerbaleService verbaleService;

    public PersistiProtocolloAction(VerbaleService verbaleService) {
        this.verbaleService = verbaleService;

    }


    @Override
    public ActionResult<VerbalizzazioneContext,VerbalizzazioneResult> execute(String uuid, VerbalizzazioneContext businessCtx, BusinessState bs, StateContext<String, String> stateContext) {

        log.atDebug().setMessage("Inizio consolidamento protocollo per uuid {}, ctx {},  bs: {}")
                .addArgument(uuid)
                .addArgument(businessCtx)
                .addArgument(bs)
                .log();
        try{
        DtoVerbale verbale = verbaleService.consolidaVerbale(businessCtx.getIdVerbale(), businessCtx.getProtocollo());
        log.atInfo().setMessage("{} Verbale consolidato: {}")
                .addArgument(uuid)
                .addArgument(verbale).log();
        return new ActionResult<>(uuid,
                VerbalizzazioneStateMachineConfig.VerbalizzazioneEventEnum.PROTOCOLLO_IMPOSTATO_SU_VERBALE.name(),
                businessCtx, BusinessState.COMPLETED,
                new VerbalizzazioneResult(businessCtx.getProtocollo()));
        }catch (Throwable t){
            log.atWarn().setMessage("{} Errore consolidamento verbale : {}")
                    .addArgument(uuid)
                    .addArgument(t).log();
            return new ActionResult<>(uuid, VerbalizzazioneStateMachineConfig.VerbalizzazioneEventEnum.PRTOCOLLO_NON_IMPOSTATO_SU_VERBALE.name(),businessCtx, BusinessState.RUNNING);
        }
    }
}
