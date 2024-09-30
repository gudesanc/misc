package org.gds.poc.orch.ssm.vebalizzazione;

import org.gds.poc.orch.ssm.client.verbale.VerbaleService;
import org.gds.poc.orch.ssm.libreria.ActionResult;
import org.gds.poc.orch.ssm.libreria.BusinessState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class AnnullaVerbaleAction extends AbstractVerbalizzazioneAction{
    private final Logger log = LoggerFactory.getLogger(AnnullaVerbaleAction.class);
    private final VerbaleService verbaleService;

    public AnnullaVerbaleAction( VerbaleService verbaleService) {
        this.verbaleService = verbaleService;

    }


    @Override
    public ActionResult<VerbalizzazioneContext,VerbalizzazioneResult> execute(String uuid, VerbalizzazioneContext businessCtx, BusinessState bs, StateContext<String, String> stateContext) {

        try {
            verbaleService.annullaVerbale(businessCtx.getIdVerbale());
            log.atInfo().setMessage("{} Verbale annullato: {}").addArgument(uuid)
                    .addArgument(businessCtx.getIdVerbale()).log();
        }
        catch (Throwable t){
            log.atInfo().setMessage("{} Errore annullamento verbale ({}): {}")
                    .addArgument(uuid)
                    .addArgument(businessCtx.getIdVerbale())
                    .addArgument(t)
                    .log();
        }
        return new ActionResult<>(uuid,
                VerbalizzazioneStateMachineConfig.VerbalizzazioneEventEnum.CAMBIATO_STATO_VERBALE.name(),businessCtx, BusinessState.FAILED);
    }
}
