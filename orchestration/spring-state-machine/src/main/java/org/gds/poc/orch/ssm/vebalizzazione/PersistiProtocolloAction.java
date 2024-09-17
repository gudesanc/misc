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
public class PersistiProtocolloAction extends GenericStateMachineAction<VerbalizzazioneContext> {
    private final Logger log = LoggerFactory.getLogger(PersistiProtocolloAction.class);
    private final VerbaleService verbaleService;

    public PersistiProtocolloAction(WebClient.Builder webClientBuilder,  VerbaleService verbaleService) {
        super(webClientBuilder,"http://localhost:8080/verbalizzazioni",VerbalizzazioneContext.class);
        this.verbaleService = verbaleService;

    }


    @Override
    public void execute(String uuid, VerbalizzazioneContext businessCtx, BusinessStatus bs, StateContext<String, String> stateContext) {

        log.atDebug().setMessage("Inizio consolidamento protocollo per uuid {}, ctx {},  bs: {}")
                .addArgument(uuid)
                .addArgument(businessCtx)
                .addArgument(bs)
                .log();
        verbaleService.consolidaVerbale(businessCtx.getIdVerbale(), businessCtx.getProtocollo())
                .doOnSuccess( s ->
                        {
                            log.atInfo().setMessage("Verbale consolidato: {}").addArgument(s).log();
                            notificaEvento(uuid, VerbalizzazioneStateMachineConfig.VerbalizzazioneChangeEventEnum.PROTOCOLLO_IMPOSTATO_SU_VERBALE.name(),businessCtx,BusinessStatus.COMPLETED);
                        }
                        )
                .doOnError( e -> {
                    log.atInfo().setMessage("Errore consolidamento verbale : {}").addArgument(e).log();
                    notificaEvento(uuid, VerbalizzazioneStateMachineConfig.VerbalizzazioneChangeEventEnum.PRTOCOLLO_NON_IMPOSTATO_SU_VERBALE.name(),businessCtx,BusinessStatus.RUNNING);
                })
                .subscribe();
    }
}
