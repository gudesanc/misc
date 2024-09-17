package org.gds.poc.orch.ssm.vebalizzazione;

import org.gds.poc.orch.ssm.client.verbale.VerbaleService;
import org.gds.poc.orch.ssm.libreria.BusinessStatus;
import org.gds.poc.orch.ssm.libreria.GenericStateMachineAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AnnullaVerbaleAction extends GenericStateMachineAction<VerbalizzazioneContext> {
    private final Logger log = LoggerFactory.getLogger(AnnullaVerbaleAction.class);
    private final VerbaleService verbaleService;

    public AnnullaVerbaleAction(WebClient.Builder webClientBuilder, VerbaleService verbaleService) {
        super(webClientBuilder,"http://localhost:8080/verbalizzazioni",VerbalizzazioneContext.class);
        this.verbaleService = verbaleService;

    }


    @Override
    public void execute(String uuid, VerbalizzazioneContext businessCtx, BusinessStatus bs, StateContext<String, String> stateContext) {

        log.atDebug().setMessage("Inizio annullamento verbale per uuid {}, ctx {},  bs: {}")
                .addArgument(uuid)
                .addArgument(businessCtx)
                .addArgument(bs)
                .log();

        verbaleService.annullaVerbale(businessCtx.getIdVerbale())
                .doOnSuccess( s ->
                        {
                            log.atInfo().setMessage("Verbale annullato: {}").addArgument(s).log();
                            notificaEvento(uuid, VerbalizzazioneStateMachineConfig.VerbalizzazioneChangeEventEnum.CAMBIATO_STATO_VERBALE.name(),businessCtx,BusinessStatus.FAILED);
                        }
                        )
                .doOnError( e -> {
                    log.atInfo().setMessage("Errore anullamento verbalee : {}").addArgument(e).log();
                    throw new RuntimeException("BBB");
                })
                .subscribe();
    }
}
