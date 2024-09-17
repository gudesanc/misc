package org.gds.poc.orch.ssm.vebalizzazione;

import org.gds.poc.orch.ssm.client.protocollo.DtoCreaProtocollo;
import org.gds.poc.orch.ssm.client.protocollo.ProtocolloService;
import org.gds.poc.orch.ssm.libreria.BusinessStatus;
import org.gds.poc.orch.ssm.libreria.GenericStateMachineAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AnnullaProtocolloAction extends GenericStateMachineAction<VerbalizzazioneContext> {
    private final Logger log = LoggerFactory.getLogger(AnnullaProtocolloAction.class);
    private final ProtocolloService protocolloService;

    public AnnullaProtocolloAction(WebClient.Builder webClientBuilder, ProtocolloService protocolloService) {
        super(webClientBuilder,"http://localhost:8080/verbalizzazioni",VerbalizzazioneContext.class);
        this.protocolloService = protocolloService;

    }



    @Override
    public void execute(String uuid, final VerbalizzazioneContext businessCxt, BusinessStatus bs,StateContext<String, String> stateContext) {

        log.atDebug().setMessage("Inizio annullamento protocollo per uuid {}, businessCxt: {}, bs: {}")
                .addArgument(uuid)
                .addArgument(businessCxt)
                .addArgument(bs)
                .log();
        protocolloService.annullaProtocollo(businessCxt.getProtocollo())
                .doOnSuccess(
                        s -> {
                            log.atInfo().setMessage("Protocollo {} annullato")
                                    .addArgument(s).log();
                            notificaEvento(uuid,
                                    VerbalizzazioneStateMachineConfig.VerbalizzazioneChangeEventEnum.CAMBIATO_STATO_PROTOCOLLO.name(),
                                    businessCxt,
                                    BusinessStatus.FAILED);
                        }
                ).doOnError(
                        e -> {
                            log.atInfo().setMessage("Errore annullamento protocollo {} ")
                                    .addArgument(e).log();
                            notificaEvento(uuid,
                                    VerbalizzazioneStateMachineConfig.VerbalizzazioneChangeEventEnum.CAMBIATO_STATO_PROTOCOLLO.name(),
                                    businessCxt,
                                    BusinessStatus.FAILED);
                        }
                ).subscribe();


    }
}
