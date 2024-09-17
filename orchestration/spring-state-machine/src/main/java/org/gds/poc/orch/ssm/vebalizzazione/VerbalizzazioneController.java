package org.gds.poc.orch.ssm.vebalizzazione;

import jakarta.annotation.Resource;
import org.gds.poc.orch.ssm.client.verbale.DtoCreaVerbale;
import org.gds.poc.orch.ssm.client.verbale.VerbaleService;
import org.gds.poc.orch.ssm.libreria.BusinessStatus;
import org.gds.poc.orch.ssm.libreria.GenericSateMachineController;
import org.gds.poc.orch.ssm.libreria.PersistInMemoryHandler;
import org.gds.poc.orch.ssm.libreria.ProcessType;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/verbalizzazioni")
public class VerbalizzazioneController extends GenericSateMachineController<VerbalizzazioneContext>{

    @Resource
    private final VerbaleService verbaleService;
    public VerbalizzazioneController(VerbaleService verbaleService, PersistInMemoryHandler persistInMemoryHandler) {
        super(persistInMemoryHandler);
        this.verbaleService = verbaleService;
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<DtoAvvioProcesso> verbalizza(DtoCreaVerbale verbaleDaVerbalizzare){
        final DtoAvvioProcesso procInfo = new DtoAvvioProcesso();
        return verbaleService.creaVerbale(verbaleDaVerbalizzare)
                .doOnSuccess( v -> {
                    VerbalizzazioneContext ctx = new VerbalizzazioneContext();
                    ctx.setIdVerbale(v.id());
                    ctx.setOggettoVerbale(verbaleDaVerbalizzare.oggetto());
                    String uuid = createNewProcess(
                            v.id(),
                            VerbalizzazioneStateMachineConfig.VerbalizzazioneStatusEnum.INIT.name(),
                            "machineId",
                            "verbalizzazione",
                            "endpoint",
                            ctx,
                            ProcessType.SYNC_ORCHESTRATION,null);
                    procInfo.setIdVerbale(v.id());
                    procInfo.setUuidProcesso(uuid);
                    notificaEvento(uuid,
                            VerbalizzazioneStateMachineConfig.VerbalizzazioneChangeEventEnum.START.name(),
                            ctx,
                            BusinessStatus.RUNNING
                    );
                })
                .then(Mono.defer(()->  Mono.just(procInfo)));
    }




}